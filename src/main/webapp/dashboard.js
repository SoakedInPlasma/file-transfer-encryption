//Frontend logic for selecting, sending, receiving and downloading files.
//Communicates with the Java backend using REST API requests.

const fileInput = document.getElementById("fileInput");
const sendForm = document.getElementById("sendForm");
const sendMessage = document.getElementById("sendMessage");

let selectedFileBase64 = null;

//cconvert a selected file into Base64
function convertToBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();

        reader.onload = function () {
            const result = reader.result;
            const base64 = result.split(",")[1];
            resolve(base64);
        };

        reader.onerror = function () {
            reject(new Error("Failed to read file"));
        };

        reader.readAsDataURL(file);
    });
}

//handle file selection
//reset any previously selected file data when the selection changes.
fileInput.addEventListener("change", async function () {
    const file = fileInput.files[0];

    selectedFileBase64 = null;
    sendMessage.textContent = "";
    sendMessage.className = "";

    if (!file) {
        return;
    }

    try {
        selectedFileBase64 = await convertToBase64(file);

        sendMessage.textContent =
            "File ready: " + file.name;

        sendMessage.className = "success";

        console.log("File name:", file.name);
        console.log("File size:", file.size, "bytes");
        console.log("Base64 conversion successful");

    } catch (error) {
        sendMessage.textContent = "Error reading file.";
        sendMessage.className = "error";
        console.error(error);
    }
});



//handle file sending through backend encryption service
const recipientSelect = document.getElementById("recipient");



 //request registered usernames from the backend and populate the dropdown
async function loadRecipients() {
    try {
        const response = await fetch("api/auth/users", {
            credentials: "same-origin"
        });

        if (!response.ok) {
            throw new Error("Failed to load users");
        }

        const usernames = await response.json();

        recipientSelect.innerHTML = '<option value="">Select a user</option>';

        usernames.forEach(function (username) {
            const option = document.createElement("option");
            option.value = username;
            option.textContent = username;
            recipientSelect.appendChild(option);
        });

    } catch (error) {
        console.error("Error loading recipients:", error);
        sendMessage.textContent = "Could not load recipients.";
        sendMessage.className = "error";
    }
}

// Load recipients when the dashboard opens
loadRecipients();



sendForm.addEventListener("submit", async function (event) {
    event.preventDefault();

    const file = fileInput.files[0];
    const receiver = recipientSelect.value;

    if (!file || !selectedFileBase64 || !receiver) {
        sendMessage.textContent = "Please select a file and recipient.";
        sendMessage.className = "error";
        return;
    }

    sendMessage.textContent = "Encrypting and sending file...";
    sendMessage.className = "";

    try {
        const response = await fetch("api/files/send", {
            method: "POST",
            credentials: "same-origin",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                receiver: receiver,
                fileName: file.name,
                fileDataBase64: selectedFileBase64
            })
        });

        const result = await response.json();

        if (response.ok) {
            sendMessage.textContent =
                result.message || "File sent successfully!";
            sendMessage.className = "success";

            console.log("File package ID:", result.packageId);

            sendForm.reset();
            selectedFileBase64 = null;
        } else {
            sendMessage.textContent =
                result.error || "Failed to send file.";
            sendMessage.className = "error";
        }

    } catch (error) {
        console.error("File sending error:", error);
        sendMessage.textContent = "Could not connect to the server.";
        sendMessage.className = "error";
    }
});



//end current session through the logout API and return the user to the login page.
const logoutBtn = document.getElementById("logoutBtn");

logoutBtn.addEventListener("click", async function () {
    try {
        const response = await fetch("api/auth/logout", {
            method: "POST",
            credentials: "same-origin"
        });

        if (response.ok) {
            window.location.href = "index.html";
        } else {
            alert("Logout failed. Please try again.");
        }
    } catch (error) {
        console.error("Logout error:", error);
        alert("Could not connect to the server.");
    }
});


 //display encrypted files received by user
const refreshBtn = document.getElementById("refreshBtn");
const inbox = document.getElementById("inbox");
const decryptMessage = document.getElementById("decryptMessage");

async function loadInbox() {
    inbox.textContent = "Loading received files...";

    try {
        const response = await fetch("api/files/inbox", {
            method: "GET",
            credentials: "same-origin"
        });

        if (!response.ok) {
            throw new Error("Failed to retrieve inbox.");
        }

        const files = await response.json();
        inbox.replaceChildren();

        if (files.length === 0) {
            inbox.textContent = "No files received yet.";
            return;
        }

        files.forEach(function (file) {
            const item = document.createElement("div");
            item.className = "inbox-item";

            const details = document.createElement("p");
            details.textContent =
                file.fileName + " — From: " + file.sender;

            const downloadBtn = document.createElement("button");
            downloadBtn.type = "button";
            downloadBtn.textContent = "Decrypt & Download";

            downloadBtn.addEventListener("click", function () {
                downloadFile(file.id);
            });

            item.appendChild(details);
            item.appendChild(downloadBtn);
            inbox.appendChild(item);
        });

    } catch (error) {
        console.error("Inbox error:", error);
        inbox.textContent = "Could not load received files.";
    }
}

//refresh the inbox when the user clicks the button
refreshBtn.addEventListener("click", loadInbox);

//load received files when the dashboard opens
loadInbox();


//retrieves decrypts and downloads a received file
async function downloadFile(packageId) {
    decryptMessage.textContent = "Decrypting and verifying file...";
    decryptMessage.className = "";

    try {
        const response = await fetch(
            "api/files/download/" + encodeURIComponent(packageId),
            {
                method: "GET",
                credentials: "same-origin"
            }
        );

        const result = await response.json();

        if (!response.ok) {
            throw new Error(result.error || "Download failed.");
        }

        // Convert the returned base64 content back into file bytes
        const binaryString = atob(result.fileDataBase64);
        const bytes = new Uint8Array(binaryString.length);

        for (let i = 0; i < binaryString.length; i++) {
            bytes[i] = binaryString.charCodeAt(i);
        }

        // Create a downloadable file in the browser
        const blob = new Blob([bytes], {
            type: "application/octet-stream"
        });

        const downloadUrl = URL.createObjectURL(blob);
        const link = document.createElement("a");

        link.href = downloadUrl;
        link.download = result.fileName;
        document.body.appendChild(link);
        link.click();
        link.remove();

        URL.revokeObjectURL(downloadUrl);

        decryptMessage.textContent =
            "File decrypted and downloaded successfully!";
        decryptMessage.className = "success";

    } catch (error) {
        console.error("Download error:", error);

        decryptMessage.textContent = error.message;
        decryptMessage.className = "error";
    }
}



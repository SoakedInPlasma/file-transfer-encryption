
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

//Prevent submission until backend is connected
sendForm.addEventListener("submit", function (event) {
    event.preventDefault();

    sendMessage.textContent =
        "File sending is not connected yet.";

    sendMessage.className = "";
});



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

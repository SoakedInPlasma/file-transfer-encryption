//sandles the login and registration interface.
//sends authentication requests to the Java backend.

const form = document.getElementById("authForm");
const formTitle = document.getElementById("formTitle");
const submitBtn = document.getElementById("submitBtn");
const toggleBtn = document.getElementById("toggleBtn");
const toggleText = document.getElementById("toggleText");
const message = document.getElementById("message");

let isRegistering = false;

//switch between login and registration
toggleBtn.addEventListener("click", function () {
    isRegistering = !isRegistering;

    formTitle.textContent = isRegistering ? "Register" : "Login";
    submitBtn.textContent = isRegistering ? "Register" : "Login";

    toggleText.textContent = isRegistering
        ? "Already have an account?"
        : "Don't have an account?";

    toggleBtn.textContent = isRegistering ? "Login" : "Register";

    message.textContent = "";
    message.className = "";
    form.reset();
});

//sends form info to java backend
form.addEventListener("submit", async function (event) {
    event.preventDefault();

    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value;

    const endpoint = isRegistering ? "register" : "login";

    const userData = {
        userName: username,
        password: password
    };

    message.textContent = "";
    message.className = "";
    submitBtn.disabled = true;

    try {
        const response = await fetch("api/auth/" + endpoint, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            credentials: "same-origin",
            body: JSON.stringify(userData)
        });

        //read backend response
        const responseText = await response.text();

        let result;
        try {
            result = JSON.parse(responseText);
        } catch {
            result = {};
        }

        if (response.ok) {
            message.textContent = result.message || "Success!";
            message.className = "success";
            form.reset();

            if (!isRegistering) {
                window.location.href = "dashboard.html";
            }
        } else {
            message.textContent = result.error ||
                "Request failed. Please try again.";
            message.className = "error";
        }

    } catch (error) {
        message.textContent = "Cannot connect to the server.";
        message.className = "error";
        console.error("Request failed:", error);
    } finally {
        submitBtn.disabled = false;
    }
});

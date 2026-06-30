// Global state variables
var stompClient = null;
var roomId = null;
var currentUsername = null;

// Combined Initialization: Triggers automatically when HTML is fully parsed
document.addEventListener("DOMContentLoaded", function() {
    // Read the Thymeleaf values cleanly from the hidden DOM container
    var configEl = document.getElementById("chatConfig");
    if (configEl) {
        var rawRoomId = configEl.getAttribute("data-room-id");
        // Ensures that an empty or whitespace string defaults cleanly back to null
        roomId = (rawRoomId && rawRoomId.trim() !== "") ? rawRoomId : null;
        currentUsername = configEl.getAttribute("data-username") || null;
    }

    // Initialize the WebSocket connection
    connect();

    // Handle initial scroll position down to bottom
    var messagesArea = document.getElementById("messagesArea");
    if (messagesArea) {
        messagesArea.scrollTop = messagesArea.scrollHeight;
    }

    // Safely bind the form intercept event listener
    var msgForm = document.getElementById("messageForm");
    if (msgForm) {
        msgForm.addEventListener("submit", handleFormSubmit);
        console.log("Event Listener Added");
    }
});

function toggleSidebar() {
    var sidebar = document.getElementById("mySidebar");
    if (!sidebar) return;
    if (sidebar.style.display === "block") {
        sidebar.style.display = "none";
    } else {
        sidebar.style.display = "block";
    }
}

function connect() {
    if (!roomId) return;

    var socket = new SockJS('/ws-chat');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected to WebSocket: ' + frame);

        stompClient.subscribe('/topic/room/' + roomId, function (response) {
            var messagePayload = JSON.parse(response.body);
            renderNewMessage(messagePayload);
        });
    });
}

function renderNewMessage(message) {
    var messagesArea = document.getElementById("messagesArea");
    if (!messagesArea) return;

    var isMe = message.sender === currentUsername;
    var rowClass = isMe ? "message-row sent" : "message-row received";
    var boxClass = isMe ? "message-box w3-blue" : "message-box w3-light-gray";

    var messageRow = document.createElement("div");
    messageRow.className = rowClass;

    var messageBox = document.createElement("div");
    messageBox.className = boxClass;

    if (!isMe) {
        var usernameSpan = document.createElement("span");
        usernameSpan.className = "username w3-text-blue";
        usernameSpan.innerText = message.sender;
        messageBox.appendChild(usernameSpan);
    }

    var textParagraph = document.createElement("p");
    textParagraph.className = "text";
    textParagraph.innerText = message.content;

    messageBox.appendChild(textParagraph);
    messageRow.appendChild(messageBox);
    messagesArea.appendChild(messageRow);

    messagesArea.scrollTop = messagesArea.scrollHeight;
}

// Separated form handler logic
function handleFormSubmit(e) {
    e.preventDefault();

    var inputField = document.getElementById("messageInput");
    if (!inputField) return;

    var messageContent = inputField.value.trim();

    if (messageContent && stompClient) {
        stompClient.send("/app/chat/" + roomId, {}, JSON.stringify({ 'content': messageContent }));
        inputField.value = "";
    }
}
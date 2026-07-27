(function () {
  "use strict";

  var messagesElement = document.getElementById("messages");
  var historyNote = document.getElementById("historyNote");
  var form = document.getElementById("messageForm");
  var input = document.getElementById("textInput");
  var sendButton = document.getElementById("sendButton");
  var statusElement = document.getElementById("connectionStatus");
  var chatNameElement = document.getElementById("chatName");
  var chatDescriptionElement = document.getElementById("chatDescription");

  var socket;
  var userId = -1;
  var userName = "";
  var currentMessageCount = 0;
  var moreHistoryAvailable = true;
  var historyLoading = false;
  var reconnectAttempts = 0;

  function connect() {
    var protocol = window.location.protocol === "https:" ? "wss:" : "ws:";
    socket = new WebSocket(protocol + "//" + window.location.host + "/The_Chat");
    setStatus("Connecting", false);

    socket.addEventListener("open", function () {
      reconnectAttempts = 0;
      input.disabled = false;
      sendButton.disabled = false;
      setStatus("Connected", true);
      input.focus();
    });

    socket.addEventListener("message", handleSocketMessage);
    socket.addEventListener("close", function () {
      input.disabled = true;
      sendButton.disabled = true;
      setStatus("Disconnected", false);
      if (reconnectAttempts < 3 && document.visibilityState !== "hidden") {
        reconnectAttempts += 1;
        window.setTimeout(connect, reconnectAttempts * 1500);
      }
    });
    socket.addEventListener("error", function () {
      setStatus("Connection issue", false);
    });
  }

  function setStatus(label, online) {
    statusElement.textContent = label;
    statusElement.classList.toggle("is-online", online);
  }

  function handleSocketMessage(event) {
    var type = event.data.charAt(0);
    var payload = event.data.substring(1);

    if (type === "l") {
      var initialMessages = JSON.parse(payload);
      currentMessageCount = initialMessages.length;
      moreHistoryAvailable = initialMessages.length >= 100;
      renderMessageList(initialMessages, true);
    } else if (type === "u") {
      var user = JSON.parse(payload);
      userId = user.id;
      userName = user.username;
      input.placeholder = "Write as " + userName + "…";
    } else if (type === "c") {
      var chat = JSON.parse(payload);
      chatNameElement.textContent = chat.name;
      chatDescriptionElement.textContent = chat.description || "No description was left for this room.";
      document.title = chat.name + " — AnonymChat";
    } else if (type === "m") {
      appendMessage(JSON.parse(payload), true);
      currentMessageCount += 1;
    } else if (type === "b") {
      appendBotMessage(payload);
    }
  }

  function renderMessageList(messages, moveToBottom) {
    messagesElement.innerHTML = "";
    var note = document.createElement("p");
    note.className = "history-note";
    note.id = "historyNote";
    note.textContent = moreHistoryAvailable
      ? "Scroll up for earlier messages"
      : "This is the beginning of the room";
    messagesElement.appendChild(note);
    historyNote = note;
    messages.forEach(function (message) {
      appendMessage(message, false);
    });
    if (moveToBottom) {
      messagesElement.scrollTop = messagesElement.scrollHeight;
    }
  }

  function appendMessage(message, moveToBottom) {
    var article = document.createElement("article");
    article.className = "message";
    if (Number(message.userId) === Number(userId)) {
      article.classList.add("is-mine");
    }

    var avatar = document.createElement("div");
    avatar.className = "message-avatar";
    avatar.setAttribute("aria-hidden", "true");
    avatar.textContent = (message.userName || "?").charAt(0).toUpperCase();

    var content = document.createElement("div");
    content.className = "message-content";
    var meta = document.createElement("p");
    meta.className = "message-meta";
    var author = document.createElement("span");
    author.className = "message-author";
    author.textContent = message.userName || "Unknown";
    var time = document.createElement("time");
    time.className = "message-time";
    time.textContent = formatTime(message.creationDate);
    var text = document.createElement("p");
    text.className = "message-text";
    text.textContent = message.content;

    meta.appendChild(author);
    meta.appendChild(time);
    content.appendChild(meta);
    content.appendChild(text);
    article.appendChild(avatar);
    article.appendChild(content);
    messagesElement.appendChild(article);

    if (moveToBottom) {
      messagesElement.scrollTop = messagesElement.scrollHeight;
    }
  }

  function appendBotMessage(text) {
    var article = document.createElement("article");
    article.className = "message is-bot";
    var message = document.createElement("p");
    message.className = "message-text";
    message.textContent = "ROOM BOT / " + text;
    article.appendChild(message);
    messagesElement.appendChild(article);
    messagesElement.scrollTop = messagesElement.scrollHeight;
  }

  function formatTime(value) {
    if (!value) {
      return "now";
    }
    var parts = value.split(" ");
    return parts.length > 1 ? parts[1].slice(0, 5) : value;
  }

  function sendMessage(content) {
    if (!socket || socket.readyState !== WebSocket.OPEN) {
      return;
    }
    socket.send(JSON.stringify({content: content}));
  }

  function loadEarlierMessages() {
    if (historyLoading || !moreHistoryAvailable) {
      return;
    }
    historyLoading = true;
    var previousHeight = messagesElement.scrollHeight;
    var requested = Math.min(1000, currentMessageCount + 100);

    fetch("/OldMessages", {
      method: "POST",
      headers: {"Content-Type": "application/x-www-form-urlencoded"},
      body: "numMessage=" + requested
    })
      .then(function (response) {
        if (!response.ok) {
          throw new Error("History unavailable");
        }
        return response.json();
      })
      .then(function (messages) {
        moreHistoryAvailable = messages.length > currentMessageCount;
        currentMessageCount = messages.length;
        renderMessageList(messages, false);
        messagesElement.scrollTop = messagesElement.scrollHeight - previousHeight;
      })
      .catch(function () {
        historyNote.textContent = "Earlier messages are unavailable";
      })
      .finally(function () {
        historyLoading = false;
      });
  }

  form.addEventListener("submit", function (event) {
    event.preventDefault();
    var content = input.value.trim();
    if (!content) {
      return;
    }
    sendMessage(content);
    input.value = "";
    input.focus();
  });

  document.querySelectorAll("[data-command]").forEach(function (button) {
    button.addEventListener("click", function () {
      input.value = button.getAttribute("data-command");
      input.focus();
    });
  });

  messagesElement.addEventListener("scroll", function () {
    if (messagesElement.scrollTop <= 20) {
      loadEarlierMessages();
    }
  });

  connect();
}());

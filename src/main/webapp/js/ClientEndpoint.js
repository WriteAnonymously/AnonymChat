
var socket = new WebSocket('ws://localhost:8080/The_Chat');

var userName = 'NULL';
var userID = -1;
var chatID = -1;
var chatName = 'NO Name';
var chatDescript = 'No Description';

socket.onopen = function (ev) {
    console.log("Connected");
};

document.getElementById("sendButton").addEventListener("click", function (ev) {
    var input = document.getElementById("textInput").value;
    sendMessage(input);
    document.getElementById('textInput').value = '';
});

function updateChatInfo() {
    console.log("updating");
    document.getElementById("chat-name").innerText = "Chat Name :"+chatName;
    document.getElementById("descr").innerText = "Description :"+chatDescript;

}


function displayMessage(message){
    displayText(message.userName + ":" + message.content + "("+message.creationDate+")");
}

function displayText(input) {
    var para = document.createElement("P");
    var messagesDiv = document.getElementById("messages");
    messagesDiv.appendChild(para);
    var t = document.createTextNode(input);
    para.appendChild(t);
    gotoBottom("messages");
}

function gotoBottom(id){
    var element = document.getElementById(id);
    element.scrollTop = element.scrollHeight - element.clientHeight;
}


function displayOldMessages(oldMessages){
    var parsedJSON = JSON.parse(oldMessages);
    for (var i=0;i<parsedJSON.length;i++) {
       displayMessage(parsedJSON[i]);
    }
}

socket.onmessage = function (ev) {
    var type = ev.data.toString().charAt(0);
    var messageReceived = ev.data.toString().substring(1);
    console.log('message from server is :', messageReceived);
    if (type === 'l'){
        console.log('recieved list');
        displayOldMessages(messageReceived);
    } else if (type === 'u'){
        var userInfo = JSON.parse(messageReceived);
        userName =  userInfo.username.valueOf();
        chatID = userInfo.chatId;
        userID = userInfo.id;
        console.log(chatID);
        console.log(userID);
    } else if (type === 'c'){
        var chatInfo = JSON.parse(messageReceived);
        chatDescript = chatInfo.description;
        chatName = chatInfo.name;
        updateChatInfo();
    } else if (type === 'm'){
        console.log('received message');
        var message = JSON.parse(messageReceived);
        displayMessage(message);
    } else if (type === 'b'){
        displayText("BOT: "+messageReceived);
    }
};

function sendMessage(input){
    var message = JSON.stringify({
        "chatId" : chatID,
        "userId" : userID,
        "userName" : userName,
        "content": input,
        "creationDate": "now"
    });
    socket.send(message);
}



var socket = new WebSocket('ws://localhost:8080/The_Chat');

var userName = 'NULL';
var userID = -1;
var chatID = -1;

socket.onopen = function (ev) {
    console.log("Connected");
};

document.getElementById("sendButton").addEventListener("click", function (ev) {
    var input = document.getElementById("textInput").value;
    sendMessage(input);
    document.getElementById('textInput').value = '';
});



function displayMessage(message){
    var para = document.createElement("P");
    var messagesDiv = document.getElementById("messages");
    messagesDiv.appendChild(para);
    var t = document.createTextNode(message.userName + ":" + message.content + "("+message.creationDate+")");
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
    } else if (userID === -1 && userName.valueOf() === 'NULL'){
        var firstMessage = JSON.parse(messageReceived);
        if (firstMessage.content.valueOf() === 'n'){
            userName =  firstMessage.userName.valueOf();
            chatID = firstMessage.chatId;
            userID = firstMessage.userId;
            console.log(firstMessage.userName);
            console.log(firstMessage.content);
            console.log(chatID);
            console.log(userID);
        } else {
            console.log("Error somewhere");
        }
    } else if (type === 'm'){
        console.log('received message');
        var message = JSON.parse(messageReceived);
        displayMessage(message);
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


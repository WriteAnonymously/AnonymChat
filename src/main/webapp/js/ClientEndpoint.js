
var socket = new WebSocket('ws://localhost:8080/The_Chat');

var userName = makeid(10);
var userID = Math.floor(Math.random() * Math.floor(100));

function makeid(length) {
        var result           = '';
        var characters       = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        var charactersLength = characters.length;
        for ( var i = 0; i < length; i++ ) {
            result += characters.charAt(Math.floor(Math.random() * charactersLength));
        }
        return result;
}

socket.onopen = function (ev) {
    var message = JSON.stringify({
        "chatId" : 1,
        "userId" : userID,
        "userName" : "tamro",
        "content": "Hii!",
        "creationDate": "now"
    });
    socket.send(message);
    console.log("Connected");
};

document.getElementById("sendButton").addEventListener("click", function (ev) {
    var input = document.getElementById("textInput").value;
    sendMessage(input);
    document.getElementById('textInput').value = '';
});


/*

function getRandomColor() {
 return '#' + Math.floor(Math.random() * 0xFFFFFF).toString(16);
}
* */

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
    //   console.log(parsedJSON[i].content);
    }
}

socket.onmessage = function (ev) {
    var type = ev.data.toString().charAt(0);
    var messageReceived = ev.data.toString().substring(1);
    console.log('message from server is :', messageReceived);
    if (type === 'm'){
        console.log('received message');
        var message = JSON.parse(messageReceived);
        displayMessage(message);
    } else if (type === 'l'){
        console.log('recieved list');
        displayOldMessages(messageReceived);
    }
};

function sendMessage(input){
    var message = JSON.stringify({
        "chatId" : 1,
        "userId" : 1,
        "userName" : "tamro",
        "content": input,
        "creationDate": "now"
    });
    socket.send(message);

}


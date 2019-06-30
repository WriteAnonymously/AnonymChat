
var socket = new WebSocket('ws://localhost:8080/Chat_Web/The_Chat');

socket.onopen = function (ev) {
    socket.send("Hi from client!");
    console.log("Connected");
};

socket.onmessage = function (ev) {
    var message = JSON.parse(ev.data);
    console.log('message from server is :', message.getContent);
};

function sendMessage(){
    var msg = "new message from client!";
    socket.send(msg);
}


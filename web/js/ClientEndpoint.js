
var socket = new WebSocket('ws://localhost:8080/The_Chat');

socket.onopen = function (ev) {
    var message = JSON.parse('{"content": "message!"}');
    socket.send(message);
    console.log("Connected");
};

socket.onmessage = function (ev) {
    console.log('message from server is :', ev.data.toString());
    var para = document.createElement("P");
    var message = JSON.parse(ev.data);
    var t = document.createTextNode(message.content);
    para.appendChild(t);
    document.getElementById("messages").appendChild(para);
};

function sendMessage(){
    var message = JSON.stringify('{"content":"message!"}');
    socket.send(message);
}



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
        "content": "Hii!",
        "creationDate": "now"
    });
    socket.send(message);
    console.log("Connected");
};


socket.onmessage = function (ev) {
    console.log('message from server is :', ev.data.toString());
    var para = document.createElement("P");
    var message = JSON.parse(ev.data);
    var t = document.createTextNode(userName +":"+ message.content + "("+message.creationDate+")");
    para.appendChild(t);
    document.getElementById("messages").appendChild(para);
};

function sendMessage(){
   // var newMessage = document.getElementById("inp").value;
    //console.log(newMessage);
    var message = JSON.stringify({
        "chatId" : 1,
        "userId" : userID,
        "content": "message to server!",
        "creationDate": "now"
    });
    socket.send(message);
}


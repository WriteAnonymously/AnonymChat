
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


/*
    var t = document.createTextNode(userName +":"+ message.content + "("+message.creationDate+")");
    para.appendChild(t);
*
* */

function displayMessage(message){

}

function displayOldMessages(oldMessages){
    var parsedJSON = JSON.parse(oldMessages);
    for (var i=0;i<parsedJSON.length;i++) {
        console.log(parsedJSON[i].content);
    }
}

socket.onmessage = function (ev) {
    var type = ev.data.toString().charAt(0);
    var messageReceived = ev.data.toString().substring(1);
    console.log('message from server is :', messageReceived);
    if (type === 'm'){
        console.log('received message');
        displayMessage(messageReceived);
    } else if (type === 'l'){
        console.log('recieved list');
        displayOldMessages(messageReceived);
    }
    var para = document.createElement("P");
  //  var message = JSON.parse(ev.data);
 //   document.getElementById("messages").appendChild(para);
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


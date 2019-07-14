
var socket = new WebSocket('ws://localhost:8080/The_Chat');

var userName = 'NULL';
var userID = -1;
var chatID = -1;
var chatName = 'NO Name';
var chatDescript = 'No Description';
var curMessages = 100;
var curScrollHeight = 0;

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
    displayText(false,message.userName + ":" + message.content + "("+message.creationDate+")");
}

function addBotImg(){
    var botImg = document.createElement("img");
    botImg.src = 'images/bot.png';
    botImg.setAttribute("height", "20");
    botImg.setAttribute("width", "20");
    botImg.setAttribute("alt", "BOT:");
    return botImg;
}

function displayText(bot, input) {
    var para = document.createElement("P");
    var messagesDiv = document.getElementById("messages");
    messagesDiv.appendChild(para);
    var t = document.createTextNode(input);

    if (bot === true){para.appendChild(addBotImg())}
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
        displayOldMessages(messageReceived);
    } else if (type === 'u'){
        var userInfo = JSON.parse(messageReceived);
        userName =  userInfo.username.valueOf();
        chatID = userInfo.chatId;
        userID = userInfo.id;
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
        displayText(true, messageReceived);
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

function requestMessages(){
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            console.log(this.responseText);
            var messageReceived = this.responseText.substring(1);
            document.getElementById("messages").innerHTML = "";
            displayOldMessages(messageReceived);
        }
    };
    xhttp.open("post", "/OldMessages", true);
    console.log(curMessages);
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("numMessage="+curMessages + "&chatId="+chatID);
}


 $(document).ready(function(){
      $("#messages").scroll(function(){
        var scrollHeight = document.getElementById('messages').scrollHeight;
        var curHeight = scrollHeight-$("#messages").scrollTop();
        if (curHeight === scrollHeight){
        //  if (curHeight === scrollHeight * 0.85){
            console.log("Asqrola");
            curScrollHeight = curHeight;
            requestMessages();
        }
      });
 });



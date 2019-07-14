function sendRequest(method, url, ind, id, callback) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            callback(xhttp.responseText);
        }
    };

    if (id !== null) {
        var toPass = "id="+id;
        xhttp.open(method, url, ind);
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhttp.send(toPass);
    } else {
        xhttp.open(method, url, ind);
        xhttp.send();
    }
}

function writeInfo(e) {
    var roomDiv = e.target;
    var id = roomDiv.id;
    sendRequest("post", "chatInfo", true, id, writeOnTheWall);
}

function writeOnTheWall(roomInfo) {
    var txtDiv = document.getElementById("chatInfo");
    while(txtDiv.hasChildNodes()) {
        txtDiv.removeChild(txtDiv.lastChild);
    }
    var info = roomInfo.substr(1, roomInfo.length - 3);
    var res = info.split(",");
    for (k = 0; k < res.length; k++) {
        var txt = document.createElement("h5");
        txt.appendChild(document.createTextNode(res[k]));
        txtDiv.appendChild(txt);
    }
}

function makeRoom(name, id) {
    var roomsSpace = document.getElementById("roomsDiv");
    var roomDiv = document.createElement("div");
    roomDiv.classList.add("chatRoom");
    var roomImg = document.createElement("img");
    roomImg.src = "../images/door.jpg";
    // roomImg.src = "store-images/TShirt.jpg";
    roomImg.id = id;
    roomImg.name = name;
    roomImg.classList.add("chatImg");
    var chatName = document.createElement("h5");
    chatName.appendChild(document.createTextNode(name));
    chatName.classList.add("centered-text");
    roomDiv.appendChild(roomImg);
    roomDiv.appendChild(chatName);
    roomsSpace.appendChild(roomDiv);
    roomDiv.addEventListener('mouseover', writeInfo);
}

function drawRooms(rooms) {
    var rooms = rooms.substr(1, rooms.length - 3);
    var roomsInfos = rooms.split(",");
    for (k = 0; k < roomsInfos.length/2; k++) {
        var name = roomsInfos[k];
        var id = roomsInfos[k + (roomsInfos.length/2)];
        makeRoom(name, id.substr(1));
    }
}

function makeEnvironment() {
    sendRequest("get", "topRooms", true, null, drawRooms);
}
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Welcome to the real world</title>
    <link rel="stylesheet" href="../Style/WelcomePageStyle.css">
    <%@ page import = "java.util.ArrayList"%>
    <%@ page session="true" %>
    <%!	
    	private int ind = 0;
   	 	private String id = "";
    	private String name = "";
   	 	ArrayList<String> names = new ArrayList<String>();
    	ArrayList<String> ids = new ArrayList<String>();
    %>
    
    <script> 
    function sendRequest(method, url, ind, id) {
    	var xhttp = new XMLHttpRequest();
    	xhttp.onreadystatechange = function() {
    		if (this.readyState == 4 && this.status == 200) {
    			if (id !== null) {
    				writeInfo(this);
    			}
    		}
    	};
    	if (id !== null) {
    		var toPass = "num=" + id;
    		xhttp.open(method, url, ind);
    		xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    		xhttp.send(toPass);
    	} else {
    		xhttp.open(method, url, ind);
        	xhttp.send();	
    	}	
    }
    
    function writeInfo(request) {
    	var txtDiv = document.getElementById("chatInfo");
    	while(txtDiv.hasChildNodes()) {
            txtDiv.removeChild(txtDiv.lastChild);
        }
    	var txt = document.createElement("h3");
    	txt.appendChild(document.createTextNode(request.responseText));
    	txtDiv.appendChild(txt);
    }
    
    function makeRoom(name, id) {
    	var roomsSpace = document.getElementById("roomsDiv");
    	var roomDiv = document.createElement("div");
    	roomDiv.classList.add("chatRoom");
    	var roomImg = document.createElement("img");
    	roomImg.src = "../images/door.jpg"; 
    	//roomImg.src = "store-images/TShirt.jpg";
    	roomImg.id = id;
    	roomImg.name = name;
    	roomImg.classList.add("chatImg");
		var chatName = document.createElement("h5");
		chatName.appendChild(document.createTextNode(name));
		chatName.classList.add("centered-text");
    	roomDiv.appendChild(roomImg);
    	roomDiv.appendChild(chatName);
    	roomsSpace.appendChild(roomDiv);
    	roomImg.eventListener("mouseover", sendRequest("post", "chatInfo", true, id));
    }
	</script>
</head>

<body>
    <form action="PublicChatFields.html">
        <input type="submit" value="New Public Chat" class="button">
    </form>
    <form action="PrivateChatFields.html">
        <input type="submit" value="New Private Chat" class="button">
    </form>
    
    <div id="roomsDiv" class="rooms"></div>
    <div id="chatInfo" style="width:300px;height:270px;border:1px solid #000; float: right; margin-top: 220px">
      
    </div>
    
    <script>sendRequest("get", "topRooms", false, null);</script>
	<%
		names = (ArrayList)(session.getAttribute("names"));
		ids = (ArrayList)(session.getAttribute("ids"));
		for (int k = 0; k < 10; k++) {
			name = names.get(k);
			id = ids.get(k);
	%>
		<script>
			makeRoom("<%out.print(name);%>","<%out.print(id);%>");
		</script>
	<%
	   	}	
	%> 
	
</body>
</html>

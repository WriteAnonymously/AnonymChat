<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>New Private Chat</title>
    <script type="text/javascript">
        function addMembers(){
            var numMembers = document.getElementById("numMembers").value;
            var list = document.getElementById("memberList");
            while(list.hasChildNodes()) {
                list.removeChild(list.lastChild);
            }
            for (k=0; k<numMembers; k++) {
                list.appendChild(document.createTextNode("Member " + (k+1)));
                var newMember = document.createElement("input");
                newMember.type = "text";
                newMember.name ="Member" + k;
                list.appendChild(newMember);
                list.appendChild(document.createElement("br"));
            }
            var button = document.createElement("input");
            button.type="submit";
            button.name="enterButton";
            button.value="Create";
            button.style="margin-left:60px";
            list.appendChild(button);
        }
    </script>
</head>
<body>
    <h1 style="padding-bottom: 50px"> Create New Private Chat </h1>
    <h4> <%=request.getParameter("errorMsg")%> </h4>
    <form action="newPrivateServlet" method="post">
        Chat Name* <input type="text" name="name" style="display:block; margin-left:60px"><br>
        Chat Description <input type="text" name="description" style="display:block; margin-left:60px"><br>
        Number of Chat Members* <input type="text" id="numMembers" style="display:block; margin-left:60px"><br>
        <a href="#" onclick="addMembers()">Add Members</a>
        <div id="memberList"/>
        <input type="submit" name="enterButton" value="Create" style="margin-left:60px">
    </form>
</body>
</html>
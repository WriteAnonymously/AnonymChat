<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>New Public Chat</title>
</head>
<body>
    <h1 style="padding-bottom: 50px"> Create New Public Chat </h1>
    <h4> <%=request.getParameter("errorMsg")%> </h4>
    <form action="newPublicServlet" method="post">
        Chat Name*<input type="text" name="name" style="display:block; margin-left:60px"><br>
        Chat Description <input type="text" name="description" style="display:block; margin-left:60px"><br>
        Maximum Number of Members <input type="text" name="limit" style="display:block; margin-left:60px"><br>
        Hashtags <input type="text" name="tags" style="display:block; margin-left:60px"> <br>
        <input type="submit" name="enterButton" value="Create" style="margin-left:60px"><br>
    </form>
</body>
</html>
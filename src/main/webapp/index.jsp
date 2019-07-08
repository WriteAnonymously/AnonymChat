<%--
  Created by IntelliJ IDEA.
  User: enqidu
  Date: 7/8/19
  Time: 9:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:forward page="Models/ChatPage.html" />
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <p>Jsp </p>
    <p id="messages">Bonjour, monsieur, click the button to send your dark thoughts away</p>
    <button onclick="sendMessage()">Submit</button>

</body>
    <script src ="js/ClientEndpoint.js"></script>
</html>

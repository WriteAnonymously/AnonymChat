function getMessagesFromServlet() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            window.alert("aq shemovida da napovni aris??????")
            callback(xhttp.responseText);
        }
    };
}



function displayMessage(id,user,content,date){

}
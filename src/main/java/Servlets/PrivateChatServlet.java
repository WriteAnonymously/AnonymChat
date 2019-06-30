package Servlets;

import DB.DBtranslator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

@WebServlet("/PrivateChatServlet")
class PrivateChatServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // preparing parameters
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        int numMembers = Integer.parseInt(request.getParameter("members"));
        String id = "placeholder";
        Date currentDate = new Date(System.currentTimeMillis());
        for(int i = 0; i < numMembers; i++){
            String member = request.getParameter(Integer.toString(i));
            // jer ar vici eseni sad wavigo
        }
        // insert into DB
        DBtranslator translator = (DBtranslator) getServletContext().getAttribute(DBtranslator.ATTRIBUTE_NAME);
        try {
            translator.insertChatDB(id,name,description, DBtranslator.INVISIBLE, numMembers, currentDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // forward to another page
        RequestDispatcher dispatch = request.getRequestDispatcher("privateChatPage.html");
        dispatch.forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

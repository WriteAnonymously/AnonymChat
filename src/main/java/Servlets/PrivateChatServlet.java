package Servlets;

import DB.ChatInfoDAO;
import DB.PrepareDB;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;

@WebServlet(name = "PrivateChatServlet", urlPatterns = {"/PrivateChatServlet"})
public class PrivateChatServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // preparing parameters
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String numMembers = request.getParameter("Members");
        int members = 0;
        try{
            members = Integer.parseInt(numMembers);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < members; i++){
            String member = request.getParameter("Member" + i);
        }
        ChatInfoDAO dao = (ChatInfoDAO) getServletContext().getAttribute(ChatInfoDAO.ATTRIBUTE);
        try {
            long id = dao.addChat(name, ChatInfoDAO.PRIVATE, description, members);
            request.setAttribute("id", id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // forward to another page
        RequestDispatcher dispatch = request.getRequestDispatcher("/Models/Homepage.html");
        dispatch.forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

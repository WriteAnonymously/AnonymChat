package Servlets;

import DB.ChatInfoDAO;
import DB.ConnectionPool;
import DB.MessageInfoDAO;
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
import java.sql.Connection;
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
        ChatInfoDAO dao = null;
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute(ConnectionPool.ATTRIBUTE);
        Connection con = null;
        try {
            con = connectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dao = new ChatInfoDAO(con);
        try {
            long id = dao.addChat(name, ChatInfoDAO.PRIVATE, description, members);
            request.setAttribute("id", id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // forward to another page
        RequestDispatcher dispatch = request.getRequestDispatcher("/Models/Homepage.html");
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dispatch.forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

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
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;


@WebServlet(name = "PublicChatServlet", urlPatterns = {"/PublicChatServlet"})
public class PublicChatServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // preparing parameters
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String limit = request.getParameter("limit");
        int lim = 100;
        try{
            lim = Integer.parseInt(limit);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        // insert into DB
        ChatInfoDAO dao = null;
        MessageInfoDAO messageInfoDAO = null;
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute(ConnectionPool.ATTRIBUTE);
        Connection con = null;
        try {
            con = connectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dao = new ChatInfoDAO(con);
        try {
            long id = dao.addChat(name, ChatInfoDAO.PUBLIC, description, lim);
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

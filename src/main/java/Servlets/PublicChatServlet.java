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
        ChatInfoDAO dao = (ChatInfoDAO) getServletContext().getAttribute(ChatInfoDAO.ATTRIBUTE);
        try {
            long id = dao.addChat(name, ChatInfoDAO.PUBLIC, description, lim);
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

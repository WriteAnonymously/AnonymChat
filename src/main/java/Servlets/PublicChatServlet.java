package Servlets;

import Classes.Constants;
import Classes.TextParser;
import DB.*;

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
import java.util.ArrayList;


@WebServlet(name = "PublicChatServlet", urlPatterns = {"/PublicChatServlet"})
public class PublicChatServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // preparing parameters
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String limit = request.getParameter("limit");
        String hashtags = request.getParameter("tags");
        int lim = ChatInfoDAO.DEFAULT_LIMIT;
        try{
            lim = Integer.parseInt(limit);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        // insert into DB
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute(ConnectionPool.ATTRIBUTE);
        Connection con = null;
        try {
            con = connectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ChatInfoDAO chatDao = new ChatInfoDAO(con);
        TagsDAO tagDao = new TagsDAO(con);
        try {
            long id = chatDao.addChat(name, ChatInfoDAO.PUBLIC, description, lim);
            request.setAttribute(Constants.CHAT_ID, id);
            ArrayList<String> tags = TextParser.parseForAddition(hashtags, name, description);
            tagDao.addTags(tags, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // forward to another page
        RequestDispatcher dispatch = request.getRequestDispatcher("/PrivateChatServlet");
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

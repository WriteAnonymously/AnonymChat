package Servlets;

import Classes.Chat;
import Classes.TextParser;
import DB.ChatInfoDAO;
import DB.ConnectionPool;
import DB.TagsDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "SearchServlet", urlPatterns = {"/SearchServlet"})
public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute(ConnectionPool.ATTRIBUTE);
        Connection con = null;

        response.setHeader("Content-Type: \"text/plain\"", "Success");
        String line = request.getParameter("line");
        TagsDAO dao = null;
        try {
            con = connectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dao = new TagsDAO(con);

        ArrayList<String> res = new ArrayList<String>();
        ArrayList<Chat> chats = null;
        ArrayList<String> tags = TextParser.parseForSearch(line);
        try {
            chats = (ArrayList<Chat>) dao.searchChats(tags);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int k = 0; k < chats.size(); k++) {
            Chat curChat = chats.get(k);
            res.add(k, curChat.getName());
            res.add(curChat.getID() + "");
        }
        PrintWriter out = response.getWriter();
        out.print(res);
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

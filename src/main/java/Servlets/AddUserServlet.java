package Servlets;

import Classes.Constants;
import Classes.NameGenerator;
import DB.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "ChatRoom", urlPatterns = {"/ChatRoom"})
public class AddUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("adding User in servlet");

        String ID = request.getParameter(Constants.CHAT_ID);
        if (ID == null){
            response.sendRedirect("/WelcomeServlet");
            return;
        }
        HttpSession session = request.getSession();
        String username = "Anon";
        UserInfoDAO dao = null;

        MessageInfoDAO messageInfoDAO = null;
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute(ConnectionPool.ATTRIBUTE);
        Connection con = null;
        try {
            con = connectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dao = new UserInfoDAO(con);

        long chatID = -1;
        try{
            chatID = Long.parseLong(ID);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        ChatInfoDAO chatInfoDAO = new ChatInfoDAO(con);
        UsernameDAO usernameDAO = new UsernameDAO(con);
        NameGenerator ng = new NameGenerator(chatInfoDAO, usernameDAO);
        try {
            username = ng.generateName(chatID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            long id = dao.addUser(chatID, username);
            System.out.println("new user id = " + id);
            session.setAttribute(Constants.USER_ID, id);
            session.setAttribute(Constants.CHAT_ID, chatID);
            session.setAttribute(Constants.USERNAME, username);
            session.setAttribute("status","loggedin");
            System.out.println("Attributes set");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String path = "/Models/ChatPage.html";
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        RequestDispatcher dispatch = request.getRequestDispatcher(path);
        dispatch.forward(request, response);
    }
}

package Servlets;

import Classes.Constants;
import Classes.NameGenerator;
import Classes.User;
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
        System.out.println("aq shemovida she chema");
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("adding User in servlet");
        String chatPath = "/Models/ChatPage.html";
        response.setHeader("Cache-Control", "private,no-store,no-cache");


        String ID = request.getParameter(Constants.CHAT_ID);

        if (ID == null){
            response.sendRedirect("/WelcomeServlet");
            return;
        }
        long chatID = -1;
        try{
            chatID = Long.parseLong(ID);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        HttpSession session = request.getSession();
        if (session.getAttribute(ID) != null){
            System.out.println("Already been");
            RequestDispatcher dispatch = request.getRequestDispatcher(chatPath);
            dispatch.forward(request, response);
            return;
        }

        String username = "Anon";
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute(ConnectionPool.ATTRIBUTE);
        Connection con = null;
        try {
            con = connectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        UserInfoDAO userInfoDAO = new UserInfoDAO(con);
        ChatInfoDAO chatInfoDAO = new ChatInfoDAO(con);
        UsernameDAO usernameDAO = new UsernameDAO(con);
        NameGenerator ng = new NameGenerator(chatInfoDAO, usernameDAO);
        try {
            username = ng.generateName(chatID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            long id = userInfoDAO.addUser(chatID, username);
            User user = new User(id, username, chatID);
            System.out.println("new user id = " + id);
            session.setAttribute(ID, user);
            session.setAttribute(Constants.CHAT_ID, ID);
            System.out.println("Attributes set");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        RequestDispatcher dispatch = request.getRequestDispatcher(chatPath);
        dispatch.forward(request, response);
    }
}

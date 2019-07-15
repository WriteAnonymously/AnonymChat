package Servlets;

import Classes.*;
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
        String chatPath = "/Models/ChatPage.html";
        response.setHeader("Cache-Control", "private,no-store,no-cache");
        response.setHeader("Pragma", "no-cache");

        String ID = request.getParameter(Constants.CHAT_ID);
        String param = request.getParameter(Constants.RANDOM_PARAMETER);

        if (ID == null && param == null){
            response.sendRedirect("/WelcomeServlet");
            return;
        }
        long chatID = -1;
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute(ConnectionPool.ATTRIBUTE);
        Connection con = null;
        try {
            con = connectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Pair privateChat = null;
        ChatInfoDAO chatInfoDAO = new ChatInfoDAO(con);

        if (param != null){
            privateChat = privateUserCall(request, response, con);
            if (privateChat == null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                response.sendRedirect("/WelcomeServlet");
                return;
            }
            ID = String.valueOf(privateChat.chatId);
        } else {
            try{
                Chat chat = chatInfoDAO.getChatInfo(Long.parseLong(ID));
                if (chat instanceof PrivateChat){
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    response.sendRedirect("/WelcomeServlet");
                    return;
                }
                chatID = Long.parseLong(ID);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        HttpSession session = request.getSession();
        if (session.getAttribute(ID) != null){
            System.out.println("You were here "+((User)session.getAttribute(ID)).getUsername());
            session.setAttribute(Constants.CHAT_ID, ID);
            session.setAttribute(Constants.FIRST_LOGIN, false);
            session.setAttribute(Constants.CHAT_ID, ID);
            RequestDispatcher dispatch = request.getRequestDispatcher(chatPath);
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dispatch.forward(request, response);
            return;
        }

        String username = "Anon";
        UserInfoDAO userInfoDAO = new UserInfoDAO(con);
        UsernameDAO usernameDAO = new UsernameDAO(con);

        Chat chatInfo = null;
        NameGenerator ng = new NameGenerator(chatInfoDAO, usernameDAO);
        if (param == null) {
            try {
                username = ng.generateName(chatID);
                chatInfo = chatInfoDAO.getChatInfo(chatID);
                System.out.println(chatInfo.getName() + "nqqq");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            long id = userInfoDAO.addUser(chatID, username);
            User user = new User(id, username, chatID);
            System.out.println("new user id = " + id);
            session.setAttribute(ID, user);
            session.setAttribute(Constants.FIRST_LOGIN, true);
            session.setAttribute(Constants.CHAT_INFO, chatInfo);
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

    private Pair privateUserCall(HttpServletRequest request, HttpServletResponse response, Connection con) {
        String param = request.getParameter(Constants.RANDOM_PARAMETER);
        Object attr = request.getSession().getAttribute(Constants.CHAT_ID);
        System.out.println("es aris attr ----- " + attr);
        if (attr == null){
            return firstAttempt(request, response, con);
        } else {
            RandomIdentificatorsDAO dao = new RandomIdentificatorsDAO(con);
            RandomIdentificatorsDAO.IdsInfo idsInfo = null;
            try {
                idsInfo = dao.containsUsedRandomIdentificator(param);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (idsInfo == null || idsInfo.getChatId() != Long.parseLong((String)attr)){
                return null;
            }
            Pair ans = new Pair();
            ans.chatId = idsInfo.getChatId();
            ans.userId = idsInfo.getUserId();
            return ans;
        }
    }

    private Pair firstAttempt(HttpServletRequest request, HttpServletResponse response, Connection con) {
        String param = request.getParameter(Constants.RANDOM_PARAMETER);
        RandomIdentificatorsDAO dao = new RandomIdentificatorsDAO(con);
        long chatId = -1;
        try {
            chatId = dao.containsNotUsedRandomIdentificator(param);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (chatId == -1) {
            return null;
        }
        try {
            dao.removeNotUsedRandomIdentificator(param);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String username = "Anon";
        UserInfoDAO userInfoDAO = new UserInfoDAO(con);
        ChatInfoDAO chatInfoDAO = new ChatInfoDAO(con);
        UsernameDAO usernameDAO = new UsernameDAO(con);

        NameGenerator ng = new NameGenerator(chatInfoDAO, usernameDAO);
        long userId = -1;
        try {
            username = ng.generateName(chatId);
            userId = userInfoDAO.addUser(chatId, username);
            dao.addUsedRandomIdentificator(param, chatId, userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Pair ans = new Pair();
        ans.chatId = chatId;
        ans.userId = userId;
        request.getSession().setAttribute(Constants.CHAT_ID, String.valueOf(chatId));
        User user = null;
        Chat chat = null;
        try {
            user = userInfoDAO.getUser(userId);
            chat = chatInfoDAO.getChatInfo(chatId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        request.getSession().setAttribute(String.valueOf(chatId), user);
        request.getSession().setAttribute(Constants.CHAT_INFO, chat);
        System.out.println("dasetiliaaaaaaaaaaaaaaa   " + request.getSession().getAttribute(Constants.CHAT_ID));
        return ans;
    }

    private class Pair{
        public long chatId, userId;
        public User user = null;
    }
}
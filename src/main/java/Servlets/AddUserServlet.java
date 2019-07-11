package Servlets;

import DB.ConnectionPool;
import DB.MessageInfoDAO;
import DB.UserInfoDAO;

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
        // preparing parameters
        System.out.println("adding User in servlet");
        String ID = request.getParameter("chatId");
        if (ID == null){
            return;
        }
        String username = "Shota";
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
        System.out.println("In AdduSer, chatId = " + chatID);
        // generating username
//        if(username == null){
//            try {
//                username = dao.generateName(chatID);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
        // insert into DB
        try {
            long id = dao.addUser(chatID, username);
            System.out.println("new user id = " + id);
            HttpSession session = request.getSession();
            session.setAttribute("id", id);
            session.setAttribute("chatId", chatID);
            session.setAttribute("username", username);
            System.out.println("Attributes set");
        } catch (SQLException e) {
            e.printStackTrace();
        }
       RequestDispatcher dispatch = request.getRequestDispatcher("/Models/ChatPage.html");
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dispatch.forward(request, response);
    }
}

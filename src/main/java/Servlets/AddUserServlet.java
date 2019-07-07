package Servlets;

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
import java.sql.SQLException;

@WebServlet(name = "AddUserServlet", urlPatterns = {"/AddUserServlet"})
public class AddUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // preparing parameters
        UserInfoDAO dao = (UserInfoDAO) getServletContext().getAttribute(UserInfoDAO.ATTRIBUTE);
        String ID = request.getParameter("chatID");
        String username = request.getParameter("username");
        long chatID = -1;
        try{
            chatID = Long.parseLong(ID);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        // generating username
        if(username == null){
            try {
                username = dao.generateName(chatID);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // insert into DB
        try {
            long id = dao.addUser(chatID, username);
            HttpSession session = request.getSession();
            session.setAttribute("id", id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        RequestDispatcher dispatch = request.getRequestDispatcher("/Models/Homepage.html");
        dispatch.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

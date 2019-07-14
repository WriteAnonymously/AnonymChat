package Servlets;

import Classes.Constants;
import Classes.MailSenderWorker;
import DB.*;

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@WebServlet(name = "PrivateChatServlet", urlPatterns = {"/PrivateChatServlet"})
public class PrivateChatServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // don't cache browser you ass
        response.setHeader("Cache-Control", "private,no-store,no-cache");
        response.setHeader("Pragma", "no-cache");

        // preparing parameters
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String numMembers = request.getParameter("numMembers");
        System.out.println("aris aq yvelaferi" + numMembers + "l");
        System.out.println(name + numMembers + numMembers);

        int members = 0;
        try{
            members = Integer.parseInt(numMembers);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (members == 0){
            members = 2;
        }
        Set <String> mails = new HashSet<String>();
        for(int i = 1; i < members; i++){
            String member = request.getParameter("member" + i);
            if (member != null && member.indexOf("@") != -1 && member.indexOf(".") != -1) {
                System.out.println(member);
                mails.add(member);
            }
        }
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute(ConnectionPool.ATTRIBUTE);
        Connection con = null;
        try {
            con = connectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long chatId = addChat(con, name, description, members, request);
        ArrayList <String> randomStrings = generateRandomStrings(chatId, members, con);

        String s = randomStrings.get(randomStrings.size() - 1);
        randomStrings.remove(randomStrings.size() - 1);

        MailSenderWorker worker = new MailSenderWorker(mails, randomStrings, Constants.WEBPAGE + "/ChatRoom?" + Constants.RANDOM_PARAMETER + "=");
        worker.start();

//        request.getSession().setAttribute(Constants.RANDOM_PARAMETER, s);

        String chatPath = "/ChatRoom?" + Constants.RANDOM_PARAMETER + "=" + s;

        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        response.sendRedirect(chatPath);

    }

    private ArrayList<String> generateRandomStrings(long chatId, int members, Connection con) {
        ArrayList <String> randomSet = new ArrayList<String>();
        RandomIdentificatorsDAO dao = new RandomIdentificatorsDAO(con);
        while (randomSet.size() != members){
            String s = UUID.randomUUID().toString();
            try {
                dao.addNotUsedRandomIdentificator(s, chatId);
                randomSet.add(s);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return randomSet;
    }

    private long addChat(Connection con, String name, String description, int members, HttpServletRequest request) {
        ChatInfoDAO dao = null;
        dao = new ChatInfoDAO(con);
        try {
            long id = dao.addChat(name, ChatInfoDAO.PRIVATE, description, members);
//            request.setAttribute("id", id);
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

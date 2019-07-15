package Servlets;

import Classes.Constants;
import Classes.Message;
import DB.ConnectionPool;
import DB.MessageInfoDAO;
import Encode_Decode.SocketInfoMessage;
import Encode_Decode.SocketInfoMessageEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.EncodeException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


@WebServlet(name = "OldMessages", urlPatterns = {"/OldMessages"})
public class SendOldMessagesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         long num = Long.parseLong(req.getParameter(Constants.NUM_MESSAGE));
         long chatId = Long.parseLong(req.getParameter(Constants.CHAT_ID));
        ConnectionPool connectionPool = (ConnectionPool) req.getServletContext().getAttribute(ConnectionPool.ATTRIBUTE);
        Connection con = null;
        try {
            con = connectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MessageInfoDAO messageInfoDAO = new MessageInfoDAO(con);
        List<Message> list = null;
        try {
             list = messageInfoDAO.getLastNMessages((int)num, chatId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resp.setHeader("Content-Type: \"text/plain\"", "Success");
        PrintWriter writer = resp.getWriter();
        SocketInfoMessageEncoder encoder = new SocketInfoMessageEncoder();
        try {
            writer.println(encoder.encode(new SocketInfoMessage(Constants.SOCKET_INFO_OLD_MESSAGES, list)));
        } catch (EncodeException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}

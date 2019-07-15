package Servlets;

import Classes.Constants;
import DB.ChatInfoDAO;
import Classes.Chat;
import Classes.PublicChat;
import DB.ConnectionPool;

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
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * Servlet implementation class roomInfo
 */
@WebServlet(name = "topRooms", urlPatterns = {"/topRooms"})
public class topRooms extends HttpServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute(ConnectionPool.ATTRIBUTE);
		Connection con = null;
		ChatInfoDAO dao = null;
		response.setHeader("Content-Type: \"text/plain\"", "Success");
		try {
			con = connectionPool.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dao = new ChatInfoDAO(con);


		ArrayList<String> res = new ArrayList<String>();
		ArrayList<Chat> chats = null;
		try {
			chats = (ArrayList<Chat>) dao.getTopNChats(15);
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
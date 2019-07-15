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
@WebServlet(name = "chatInfo", urlPatterns = {"/chatInfo"})
public class chatInfo extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute(ConnectionPool.ATTRIBUTE);
		String name = "Chat Name : ";
		String desc = "Chat Description : ";
		String date = "Creation Date : ";
		Connection con = null;
		ChatInfoDAO dao = null;	
		try {
			con = connectionPool.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dao = new ChatInfoDAO(con);
		Chat curChat = null;
		try {
			curChat = dao.getChatInfo(Long.parseLong(id));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ArrayList<String> res = new ArrayList<String>();
		res.add(name + curChat.getName());
		res.add(desc + curChat.getDescription());
		res.add(date + curChat.getCreationTime());
		PrintWriter out = response.getWriter();
		out.println(res);
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

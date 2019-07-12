package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class chatInfo
 */
@WebServlet("/chatInfo")
public class chatInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext cntx = request.getServletContext();
		Statement stmt = (Statement)cntx.getAttribute("statement");
		String id = (String)(request.getParameter("num"));
		String res = "";
		String name = "Chat Name : ";
		String desc = "Chat Description : ";
		String status = "Chat Status : ";
		String maxNum = "Users Limit : ";
		String date = "Creation Date : ";		
		   try {
			stmt.executeQuery("USE university");
			ResultSet rs = stmt.executeQuery("select * from chats where id=" + id);
			   while(rs.next()) {
				   name += rs.getString("name");
				   desc += rs.getString("description");
				   status += rs.getString("status");
				   maxNum += rs.getString("max_users_number"); 
				   date += rs.getString("Creation Date : ");
			   }	
			res += name + "\r" + desc + "\r" + status + "\r" + maxNum + "\r" + date;
			PrintWriter out = response.getWriter();
			out.println(res);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

}

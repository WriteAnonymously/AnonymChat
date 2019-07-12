package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class roomInfo
 */
@WebServlet("/topRooms")
public class topRooms extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext cntx = request.getServletContext();
		Statement stmt = (Statement)cntx.getAttribute("statement");
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> ids = new ArrayList<String>();
		   try {
			stmt.executeQuery("USE university");
			ResultSet rs = stmt.executeQuery("select * from chats");
			   while(rs.next()) {
				 String name = rs.getString("name");
				 String id = rs.getString("id");
				 ids.add(id);
				 names.add(name);
			   }	
	    HttpSession ses = request.getSession();
	    ses.setAttribute("names", names);
	    ses.setAttribute("ids", ids);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

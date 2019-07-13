package Servlets;

import Classes.Constants;
import DB.ConnectionPool;
import DB.DBInfo;
import org.junit.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.plaf.nimbus.State;

import java.io.IOException;
import java.sql.*;

import static org.mockito.Mockito.*;

public class AddUserServletTest {
    @Test
    public void addUserTest() throws SQLException, ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        ConnectionPool pool = mock(ConnectionPool.class);
        HttpSession session = mock(HttpSession.class);
        ServletContext context = mock(ServletContext.class);
        Connection connection = mock(Connection.class);
        PreparedStatement pStatement = mock(PreparedStatement.class);
        Statement statement = mock(Statement.class);
        ResultSet set = mock(ResultSet.class);

        when(request.getParameter(Constants.CHAT_ID)).thenReturn("1");
        when(request.getSession()).thenReturn(session);
        when(request.getServletContext()).thenReturn(context);
        when(context.getAttribute(ConnectionPool.ATTRIBUTE)).thenReturn(pool);
        when(pool.getConnection()).thenReturn(connection);
        when(connection.prepareStatement("insert into " + DBInfo.USERS_TABLE
                + " (chatid, username, creation_date) value "
                + "(?, ?, now(4));")).thenReturn(pStatement);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("select LAST_INSERT_ID();")).thenReturn(set);
        when(set.getString(1)).thenReturn("1");
        when(request.getRequestDispatcher("/Models/ChatPage.html")).thenReturn(dispatcher);

        AddUserServlet servlet = new AddUserServlet();
        servlet.doGet(request, response);

        verify(pStatement).setLong(1, 1);
        verify(set).last();
        verify(session).setAttribute(Constants.USER_ID, new Long(1));
        verify(session).setAttribute(Constants.CHAT_ID, new Long(1));
        verify(session).setAttribute(Constants.USERNAME, "Shota");
        verify(session).setAttribute("status", "loggedin");
        verify(dispatcher).forward(request, response);
    }
}

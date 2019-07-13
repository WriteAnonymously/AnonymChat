package Servlets;

import DB.ChatInfoDAO;
import DB.ConnectionPool;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class PrivateChatServletTest extends Mockito {
    private static final String PAGE = "/Models/Homepage.html";

    @Test
    public void correctInput() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        final ServletContext servletContext = mock(ServletContext.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        ConnectionPool pool = mock(ConnectionPool.class);
        Connection connection = mock(Connection.class);

        ChatInfoDAO dao = mock(ChatInfoDAO.class);

        PrivateChatServlet serv = new PrivateChatServlet(){
            public ServletContext getServletContext() {
                return servletContext;
            }
        };


        when(request.getParameter("name")).thenReturn("saxeli");
        when(request.getParameter("description")).thenReturn("agwera");
        when(request.getParameter("Members")).thenReturn("2");
        when(request.getParameter("Member0")).thenReturn("saba");
        when(request.getParameter("Member1")).thenReturn("givi");
        when(servletContext.getAttribute(ConnectionPool.ATTRIBUTE)).thenReturn(pool);
        when(request.getRequestDispatcher(PAGE)).thenReturn(dispatcher);
        when(pool.getConnection()).thenReturn(connection);

        serv.doPost(request, response);

        verify(dispatcher).forward(request,response);
        verify(dao, atLeast(1)).addChat("saxeli", ChatInfoDAO.PRIVATE, "agwera", 2);
        verify(request, atLeast(1)).getParameter("Member0");
        verify(request, atLeast(1)).getParameter("Member1");
    }

    @Test
    public void wrongInput() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("name")).thenReturn("saxeli");
        when(request.getParameter("description")).thenReturn("agwera");
        when(request.getParameter("Members")).thenReturn("");

        final ServletContext servletContext = mock(ServletContext.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        ChatInfoDAO dao = mock(ChatInfoDAO.class);

        when(servletContext.getAttribute(ChatInfoDAO.ATTRIBUTE)).thenReturn(dao);
        when(request.getRequestDispatcher(PAGE)).thenReturn(dispatcher);

        PrivateChatServlet serv = new PrivateChatServlet(){
            public ServletContext getServletContext() {
                return servletContext;
            }
        };
        serv.doPost(request, response);

        verify(dispatcher).forward(request,response);
        verify(dao, atLeast(1)).addChat("saxeli", ChatInfoDAO.PRIVATE, "agwera", 0);
        verify(dao, atLeast(0)).addChat("saxeli", ChatInfoDAO.PUBLIC, "agwera", 0);

    }

    @Test
    public void noDescription() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("name")).thenReturn("saxeli");
        when(request.getParameter("description")).thenReturn("");
        when(request.getParameter("Members")).thenReturn("2");
        when(request.getParameter("Member0")).thenReturn("saba");
        when(request.getParameter("Member1")).thenReturn("givi");


        final ServletContext servletContext = mock(ServletContext.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        ChatInfoDAO dao = mock(ChatInfoDAO.class);

        when(servletContext.getAttribute(ChatInfoDAO.ATTRIBUTE)).thenReturn(dao);
        when(request.getRequestDispatcher(PAGE)).thenReturn(dispatcher);

        PrivateChatServlet serv = new PrivateChatServlet(){
            public ServletContext getServletContext() {
                return servletContext;
            }
        };
        serv.doPost(request, response);

        verify(dispatcher).forward(request,response);
        verify(dao, atLeast(1)).addChat("saxeli", ChatInfoDAO.PRIVATE, "", 2);
        verify(request, atLeast(1)).getParameter("Member0");
        verify(request, atLeast(1)).getParameter("Member1");
    }


}

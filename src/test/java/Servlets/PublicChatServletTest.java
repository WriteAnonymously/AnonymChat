package Servlets;

import DB.ChatInfoDAO;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PublicChatServletTest extends Mockito {
    private static final String PAGE = "/Models/Homepage.html";

    @Test
    public void correctInput() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("name")).thenReturn("maksi");
        when(request.getParameter("description")).thenReturn("yvela makseli");
        when(request.getParameter("limit")).thenReturn("50");

        final ServletContext servletContext = mock(ServletContext.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        ChatInfoDAO dao = mock(ChatInfoDAO.class);

        when(request.getRequestDispatcher(PAGE)).thenReturn(dispatcher);
        when(servletContext.getAttribute(ChatInfoDAO.ATTRIBUTE)).thenReturn(dao);

        PublicChatServlet serv = new PublicChatServlet(){
            public ServletContext getServletContext() {
                return servletContext;
            }
        };
        serv.doPost(request, response);

        verify(dispatcher).forward(request,response);
        verify(dao, atLeast(1)).addChat("maksi", ChatInfoDAO.PUBLIC, "yvela makseli", 50);
    }

    @Test
    public void wrongLimit() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("name")).thenReturn("Veshapebi");
        when(request.getParameter("description")).thenReturn("Veshapebis chati");
        when(request.getParameter("limit")).thenReturn("araricxvi");

        final ServletContext servletContext = mock(ServletContext.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        ChatInfoDAO dao = mock(ChatInfoDAO.class);

        when(request.getRequestDispatcher(PAGE)).thenReturn(dispatcher);
        when(servletContext.getAttribute(ChatInfoDAO.ATTRIBUTE)).thenReturn(dao);

        PublicChatServlet serv = new PublicChatServlet(){
            public ServletContext getServletContext() {
                return servletContext;
            }
        };
        serv.doPost(request, response);

        verify(dispatcher).forward(request,response);
        verify(dao, atLeast(1)).addChat("Veshapebi", ChatInfoDAO.PUBLIC, "Veshapebis chati", 100);
        verify(dao, atLeast(0)).addChat("Veshapebi", ChatInfoDAO.PRIVATE, "Veshapebis chati", 100);
    }

    @Test
    public void noDescription() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("name")).thenReturn("saxeli");
        when(request.getParameter("description")).thenReturn("");
        when(request.getParameter("limit")).thenReturn("10");
        final ServletContext servletContext = mock(ServletContext.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        ChatInfoDAO dao = mock(ChatInfoDAO.class);

        when(request.getRequestDispatcher(PAGE)).thenReturn(dispatcher);
        when(servletContext.getAttribute(ChatInfoDAO.ATTRIBUTE)).thenReturn(dao);

        PublicChatServlet serv = new PublicChatServlet(){
            public ServletContext getServletContext() {
                return servletContext;
            }
        };
        serv.doPost(request, response);

        verify(dao, atLeast(1)).addChat("saxeli", ChatInfoDAO.PUBLIC, "", 10);
        verify(dispatcher).forward(request,response);
    }
}

package Servlets;

import DB.ChatInfoDAO;
import DB.MessageInfoDAO;
import DB.PrepareDB;
import DB.UserInfoDAO;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.sql.Connection;
import java.sql.SQLException;

@WebListener()
public class Listener implements ServletContextListener,
        HttpSessionListener/*, HttpSessionAttributeListener */{

    // Public constructor is required by servlet spec
    public Listener() {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
      /* This method is called when the servlet context is
         initialized(when the Web application is deployed).
         You can initialize servlet context related data here.
      */

        try {
            Connection con = PrepareDB.getInstance();
            ChatInfoDAO chat = new ChatInfoDAO(con);
            UserInfoDAO user = new UserInfoDAO(con);
            MessageInfoDAO message = new MessageInfoDAO(con);
            sce.getServletContext().setAttribute(ChatInfoDAO.ATTRIBUTE, chat);
            sce.getServletContext().setAttribute(UserInfoDAO.ATTRIBUTE, user);
            sce.getServletContext().setAttribute(MessageInfoDAO.ATTRIBUTE, message);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
      /* This method is invoked when the Servlet Context
         (the Web application) is undeployed or
         Application Server shuts down.
      */
    }

    // -------------------------------------------------------
    // HttpSessionListener implementation
    // -------------------------------------------------------
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }
}
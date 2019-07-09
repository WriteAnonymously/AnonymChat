package Servlets;

import DB.*;

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
      //  System.out.println("Starting initializing connection;");
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        sce.getServletContext().setAttribute(ConnectionPool.ATTRIBUTE, connectionPool);
        Connection con = null;
        try {
            con = connectionPool.getConnection();
            System.out.println("daemata");
            PrepareDB.addInfo(con);
            System.out.println("ukve Shig aris");
        } catch (SQLException e) {
            e.printStackTrace();
        }
     //   System.out.println("done initializing connection;");
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
        System.out.println("Session created");
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }
}
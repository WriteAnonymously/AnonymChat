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
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        sce.getServletContext().setAttribute(ConnectionPool.ATTRIBUTE, connectionPool);
        SQLException lastFailure = null;
        for (int attempt = 1; attempt <= 12; attempt++) {
            try (Connection connection = connectionPool.getConnection()) {
                sce.getServletContext().log("Database connection established");
                return;
            } catch (SQLException exception) {
                lastFailure = exception;
                sce.getServletContext().log(
                        "Database is not ready (attempt " + attempt + " of 12)");
                if (attempt < 12) {
                    try {
                        Thread.sleep(2_500);
                    } catch (InterruptedException interrupted) {
                        Thread.currentThread().interrupt();
                        throw new IllegalStateException(
                                "Interrupted while waiting for the database", interrupted);
                    }
                }
            }
        }
        throw new IllegalStateException("Database is unavailable after 30 seconds", lastFailure);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionPool connectionPool =
                (ConnectionPool) sce.getServletContext().getAttribute(ConnectionPool.ATTRIBUTE);
        if (connectionPool != null) {
            try {
                connectionPool.close();
            } catch (SQLException exception) {
                sce.getServletContext().log("Could not close the database pool", exception);
            }
        }
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

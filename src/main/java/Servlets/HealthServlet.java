package Servlets;

import DB.ConnectionPool;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "HealthServlet", urlPatterns = {"/health"})
public class HealthServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            if (!connection.isValid(2)) {
                throw new SQLException("Database validation failed");
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print("{\"status\":\"ok\"}");
        } catch (SQLException exception) {
            response.sendError(
                    HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Database unavailable");
        }
    }
}

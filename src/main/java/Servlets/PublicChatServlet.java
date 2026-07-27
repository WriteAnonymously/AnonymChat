package Servlets;

import Classes.Constants;
import Classes.TextParser;
import DB.ChatInfoDAO;
import DB.ConnectionPool;
import DB.TagsDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "PublicChatServlet", urlPatterns = {"/PublicChatServlet"})
public class PublicChatServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = clean(request.getParameter("name"));
        String description = clean(request.getParameter("description"));
        String hashtags = clean(request.getParameter("tags"));
        Integer limit = parseLimit(request.getParameter("limit"));

        if (name.isEmpty() || name.length() > Constants.MAX_ROOM_NAME_LENGTH
                || description.isEmpty()
                || description.length() > Constants.MAX_ROOM_DESCRIPTION_LENGTH
                || hashtags.length() > 300
                || limit == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Check the room details.");
            return;
        }

        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            connection.setAutoCommit(false);
            try {
                long chatId = new ChatInfoDAO(connection)
                        .addChat(name, ChatInfoDAO.PUBLIC, description, limit);
                new TagsDAO(connection).addTags(
                        TextParser.parseForAddition(hashtags, name, description),
                        chatId);
                connection.commit();
                response.sendRedirect(
                        request.getContextPath() + "/ChatRoom?"
                                + Constants.CHAT_ID + "=" + chatId);
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException exception) {
            getServletContext().log("Could not create public room", exception);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

    private Integer parseLimit(String value) {
        try {
            int parsed = Integer.parseInt(value);
            return parsed >= 2 && parsed <= 100 ? parsed : null;
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private String clean(String value) {
        return value == null ? "" : value.strip();
    }
}

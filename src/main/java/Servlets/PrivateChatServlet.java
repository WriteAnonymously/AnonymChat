package Servlets;

import Classes.Constants;
import Classes.MailSenderWorker;
import DB.ChatInfoDAO;
import DB.ConnectionPool;
import DB.DBInfo;
import DB.RandomIdentificatorsDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

@WebServlet(name = "PrivateChatServlet", urlPatterns = {"/PrivateChatServlet"})
public class PrivateChatServlet extends HttpServlet {
    private static final Pattern EMAIL =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Cache-Control", "private, no-store, no-cache");
        String name = clean(request.getParameter("name"));
        String description = clean(request.getParameter("description"));
        Integer members = parseMembers(request.getParameter("numMembers"));

        if (name.isEmpty() || name.length() > Constants.MAX_ROOM_NAME_LENGTH
                || description.isEmpty()
                || description.length() > Constants.MAX_ROOM_DESCRIPTION_LENGTH
                || members == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Check the room details.");
            return;
        }

        Set<String> emails = new LinkedHashSet<>();
        for (int index = 1; index < members; index++) {
            String email = clean(request.getParameter("member" + index)).toLowerCase();
            if (email.length() > 254 || !EMAIL.matcher(email).matches()) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Every guest needs a valid email address.");
                return;
            }
            emails.add(email);
        }
        if (emails.size() != members - 1) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Each invitation needs a different email address.");
            return;
        }

        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            connection.setAutoCommit(false);
            try {
                long chatId = new ChatInfoDAO(connection)
                        .addChat(name, ChatInfoDAO.PRIVATE, description, members);
                ArrayList<String> tokens = createTokens(chatId, members, connection);
                String creatorToken = tokens.remove(tokens.size() - 1);
                connection.commit();

                String linkTemplate = DBInfo.APP_BASE_URL + request.getContextPath()
                        + "/ChatRoom?" + Constants.RANDOM_PARAMETER + "=$";
                new MailSenderWorker(emails, tokens, linkTemplate).start();
                response.sendRedirect(
                        request.getContextPath() + "/ChatRoom?"
                                + Constants.RANDOM_PARAMETER + "=" + creatorToken);
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException exception) {
            getServletContext().log("Could not create private room", exception);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

    private ArrayList<String> createTokens(long chatId, int members, Connection connection)
            throws SQLException {
        ArrayList<String> tokens = new ArrayList<>();
        RandomIdentificatorsDAO invitations = new RandomIdentificatorsDAO(connection);
        while (tokens.size() < members) {
            String token = UUID.randomUUID().toString();
            invitations.addNotUsedRandomIdentificator(token, chatId);
            tokens.add(token);
        }
        return tokens;
    }

    private Integer parseMembers(String value) {
        try {
            int parsed = Integer.parseInt(value);
            return parsed >= 2 && parsed <= 12 ? parsed : null;
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private String clean(String value) {
        return value == null ? "" : value.strip();
    }
}

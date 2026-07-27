package Servlets;

import Classes.Chat;
import Classes.Constants;
import Classes.NameGenerator;
import Classes.PrivateChat;
import Classes.User;
import DB.ChatInfoDAO;
import DB.ConnectionPool;
import DB.RandomIdentificatorsDAO;
import DB.UserInfoDAO;
import DB.UsernameDAO;
import Sockets.ChatEndpoint;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLException;

@WebServlet(name = "ChatRoom", urlPatterns = {"/ChatRoom"})
public class AddUserServlet extends HttpServlet {
    private static final String CHAT_PAGE = "/Models/ChatPage.html";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Cache-Control", "private, no-store, no-cache");
        response.setHeader("Pragma", "no-cache");

        String publicRoomId = request.getParameter(Constants.CHAT_ID);
        String invitationToken = request.getParameter(Constants.RANDOM_PARAMETER);
        if (publicRoomId == null && invitationToken == null) {
            redirectToLobby(request, response, "invalid");
            return;
        }

        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            JoinResult result = invitationToken == null
                    ? joinPublicRoom(request, connection, publicRoomId)
                    : joinPrivateRoom(request, connection, invitationToken);

            if (result == null) {
                redirectToLobby(request, response, "invalid");
                return;
            }
            if (result.full) {
                redirectToLobby(request, response, "full");
                return;
            }

            HttpSession session = request.getSession(true);
            String roomKey = Long.toString(result.chat.getID());
            session.setAttribute(roomKey, result.user);
            session.setAttribute(Constants.CHAT_ID, roomKey);
            session.setAttribute(Constants.CHAT_INFO, result.chat);
            session.setAttribute(Constants.FIRST_LOGIN, result.firstLogin);

            RequestDispatcher dispatcher = request.getRequestDispatcher(CHAT_PAGE);
            dispatcher.forward(request, response);
        } catch (NumberFormatException exception) {
            redirectToLobby(request, response, "invalid");
        } catch (SQLException exception) {
            getServletContext().log("Could not join room", exception);
            response.sendError(
                    HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "The room is temporarily unavailable.");
        }
    }

    private JoinResult joinPublicRoom(
            HttpServletRequest request,
            Connection connection,
            String idValue) throws SQLException {
        long chatId = Long.parseLong(idValue);
        Chat chat = new ChatInfoDAO(connection).getChatInfo(chatId);
        if (chat == null || chat instanceof PrivateChat) {
            return null;
        }

        User existing = existingUser(request.getSession(false), chatId);
        if (existing != null) {
            return new JoinResult(chat, existing, false, false);
        }
        if (ChatEndpoint.getActiveUserCount(chatId) >= chat.getLimit()) {
            return new JoinResult(chat, null, false, true);
        }

        User user = createUser(connection, chatId);
        return new JoinResult(chat, user, true, false);
    }

    private JoinResult joinPrivateRoom(
            HttpServletRequest request,
            Connection connection,
            String token) throws SQLException {
        if (token == null || token.length() > 100) {
            return null;
        }

        RandomIdentificatorsDAO invitations = new RandomIdentificatorsDAO(connection);
        RandomIdentificatorsDAO.IdsInfo claimed =
                invitations.containsUsedRandomIdentificator(token);
        if (claimed != null) {
            User existing = existingUser(request.getSession(false), claimed.getChatId());
            if (existing == null || existing.getId() != claimed.getUserId()) {
                return null;
            }
            Chat chat = new ChatInfoDAO(connection).getChatInfo(claimed.getChatId());
            return chat == null ? null : new JoinResult(chat, existing, false, false);
        }

        connection.setAutoCommit(false);
        try {
            long chatId = invitations.containsNotUsedRandomIdentificator(token);
            if (chatId < 0) {
                connection.rollback();
                return null;
            }
            Chat chat = new ChatInfoDAO(connection).getChatInfo(chatId);
            if (!(chat instanceof PrivateChat)) {
                connection.rollback();
                return null;
            }

            User user = createUser(connection, chatId);
            invitations.removeNotUsedRandomIdentificator(token);
            invitations.addUsedRandomIdentificator(token, chatId, user.getId());
            connection.commit();
            return new JoinResult(chat, user, true, false);
        } catch (SQLException exception) {
            connection.rollback();
            throw exception;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private User createUser(Connection connection, long chatId) throws SQLException {
        ChatInfoDAO chats = new ChatInfoDAO(connection);
        NameGenerator generator = new NameGenerator(chats, new UsernameDAO(connection));
        UserInfoDAO users = new UserInfoDAO(connection);
        for (int attempt = 0; attempt < 5; attempt++) {
            String username = generator.generateName(chatId);
            try {
                long userId = users.addUser(chatId, username);
                return new User(userId, username, chatId);
            } catch (SQLIntegrityConstraintViolationException collision) {
                // Another guest claimed the same generated name concurrently.
            }
        }
        throw new SQLException("Could not allocate a unique temporary name");
    }

    private User existingUser(HttpSession session, long chatId) {
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(Long.toString(chatId));
        return value instanceof User ? (User) value : null;
    }

    private void redirectToLobby(
            HttpServletRequest request,
            HttpServletResponse response,
            String error) throws IOException {
        response.sendRedirect(request.getContextPath() + "/WelcomeServlet?error=" + error);
    }

    private static class JoinResult {
        private final Chat chat;
        private final User user;
        private final boolean firstLogin;
        private final boolean full;

        private JoinResult(Chat chat, User user, boolean firstLogin, boolean full) {
            this.chat = chat;
            this.user = user;
            this.firstLogin = firstLogin;
            this.full = full;
        }
    }
}

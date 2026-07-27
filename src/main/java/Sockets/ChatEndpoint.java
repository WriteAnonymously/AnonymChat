package Sockets;

import Classes.Chat;
import Classes.ChatBot;
import Classes.Constants;
import Classes.Message;
import Classes.User;
import DB.ConnectionPool;
import DB.MessageInfoDAO;
import Encode_Decode.MessageHandlerDecode;
import Encode_Decode.SocketInfoMessage;
import Encode_Decode.SocketInfoMessageEncoder;

import javax.websocket.CloseReason;
import javax.websocket.DecodeException;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(
        value = "/The_Chat",
        configurator = ChatroomServerConfigurator.class,
        encoders = {SocketInfoMessageEncoder.class})
public class ChatEndpoint {
    private static final Map<Long, Set<ChatEndpoint>> ENDPOINTS = new ConcurrentHashMap<>();
    private static final DateTimeFormatter MESSAGE_TIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Session session;

    @OnOpen
    public void onOpen(EndpointConfig endpointConfig, Session session)
            throws IOException, EncodeException, SQLException {
        Object chatIdValue = endpointConfig.getUserProperties().get(Constants.CHAT_ID);
        if (chatIdValue == null) {
            session.close(new CloseReason(
                    CloseReason.CloseCodes.VIOLATED_POLICY,
                    "Join a room before opening the chat connection."));
            return;
        }

        long chatId = Long.parseLong(chatIdValue.toString());
        User user = (User) endpointConfig.getUserProperties().get(Long.toString(chatId));
        Chat chat = (Chat) endpointConfig.getUserProperties().get(Constants.CHAT_INFO);
        boolean firstLogin = Boolean.TRUE.equals(
                endpointConfig.getUserProperties().get(Constants.FIRST_LOGIN));

        if (user == null || chat == null || user.getChatId() != chatId) {
            session.close(new CloseReason(
                    CloseReason.CloseCodes.VIOLATED_POLICY,
                    "The room session is invalid."));
            return;
        }

        session.getUserProperties().put(Constants.USER_ATR, user);
        session.getUserProperties().put(Constants.CHAT_ID, chatId);
        Set<ChatEndpoint> room =
                ENDPOINTS.computeIfAbsent(chatId, ignored -> new CopyOnWriteArraySet<>());
        synchronized (room) {
            boolean userAlreadyPresent = room.stream()
                    .map(endpoint -> endpoint.session)
                    .filter(activeSession -> activeSession != null && activeSession.isOpen())
                    .map(activeSession -> (User) activeSession.getUserProperties()
                            .get(Constants.USER_ATR))
                    .filter(activeUser -> activeUser != null)
                    .anyMatch(activeUser -> activeUser.getId() == user.getId());
            if (!userAlreadyPresent && getActiveUserCount(chatId) >= chat.getLimit()) {
                session.close(new CloseReason(
                        CloseReason.CloseCodes.TRY_AGAIN_LATER,
                        "The room is at its live capacity."));
                return;
            }
            this.session = session;
            room.add(this);
        }

        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            List<Message> messages = new MessageInfoDAO(connection).getLastNMessages(100, chatId);
            sendToSession(Constants.SOCKET_INFO_USER, user, session);
            sendToSession(Constants.SOCKET_INFO_CHAT, chat, session);
            sendToSession(Constants.SOCKET_INFO_OLD_MESSAGES, messages, session);
        }

        if (firstLogin) {
            broadcast(
                    Constants.SOCKET_INFO_BOT,
                    new ChatBot(null).announceNewUser(user.getUsername()),
                    chatId);
            Object httpSession = endpointConfig.getUserProperties()
                    .get(ChatroomServerConfigurator.HTTP_SESSION);
            if (httpSession instanceof HttpSession) {
                ((HttpSession) httpSession).setAttribute(Constants.FIRST_LOGIN, false);
            }
        }
    }

    @OnMessage
    public void onMessage(Session session, String payload)
            throws IOException, EncodeException, SQLException, DecodeException {
        User user = (User) session.getUserProperties().get(Constants.USER_ATR);
        Long chatId = (Long) session.getUserProperties().get(Constants.CHAT_ID);
        if (user == null || chatId == null) {
            session.close(new CloseReason(
                    CloseReason.CloseCodes.VIOLATED_POLICY,
                    "The room session expired."));
            return;
        }

        Message incoming = MessageHandlerDecode.decodeMessage(payload);
        String content = incoming == null ? null : incoming.getContent();
        if (content == null || content.isBlank()) {
            return;
        }
        content = content.strip();
        if (content.length() > Constants.MAX_MESSAGE_LENGTH) {
            sendToSession(
                    Constants.SOCKET_INFO_BOT,
                    "That message is too long. Keep it under "
                            + Constants.MAX_MESSAGE_LENGTH + " characters.",
                    session);
            return;
        }

        Message message = new Message(
                chatId,
                user.getId(),
                user.getUsername(),
                content,
                LocalDateTime.now().format(MESSAGE_TIME));

        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            new MessageInfoDAO(connection).addMessage(message);
            broadcast(Constants.SOCKET_INFO_MESSAGE, message, chatId);

            if (content.startsWith("BOT:")) {
                ChatBot bot = new ChatBot(connection);
                String answer = bot.answerMessage(content, user.getUsername(), chatId, connection);
                broadcast(Constants.SOCKET_INFO_BOT, answer, chatId);
                if (bot.getType(content) == 2) {
                    sendToSession(
                            Constants.SOCKET_INFO_BOT,
                            "Secret prompt: " + bot.answerWord(chatId),
                            session);
                }
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        Object chatIdValue = session.getUserProperties().get(Constants.CHAT_ID);
        if (chatIdValue == null) {
            return;
        }
        long chatId = (Long) chatIdValue;
        Set<ChatEndpoint> room = ENDPOINTS.get(chatId);
        if (room != null) {
            room.remove(this);
            if (room.isEmpty()) {
                ENDPOINTS.remove(chatId, room);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        if (session != null && session.isOpen()) {
            try {
                session.close(new CloseReason(
                        CloseReason.CloseCodes.UNEXPECTED_CONDITION,
                        "The connection encountered an error."));
            } catch (IOException ignored) {
                // The connection is already going away.
            }
        }
    }

    public static int getActiveUserCount(long chatId) {
        return getActiveUsers(chatId).size();
    }

    public static Set<String> getActiveUserNames(long chatId) {
        return new HashSet<>(getActiveUsers(chatId).values());
    }

    private static Map<Long, String> getActiveUsers(long chatId) {
        Set<ChatEndpoint> room = ENDPOINTS.get(chatId);
        if (room == null) {
            return Map.of();
        }
        Map<Long, String> users = new ConcurrentHashMap<>();
        room.stream()
                .map(endpoint -> endpoint.session)
                .filter(activeSession -> activeSession != null && activeSession.isOpen())
                .map(activeSession -> (User) activeSession.getUserProperties()
                        .get(Constants.USER_ATR))
                .filter(activeUser -> activeUser != null)
                .forEach(activeUser -> users.put(activeUser.getId(), activeUser.getUsername()));
        return users;
    }

    private void sendToSession(String type, Object message, Session target)
            throws IOException, EncodeException {
        synchronized (target) {
            if (target.isOpen()) {
                target.getBasicRemote().sendObject(new SocketInfoMessage(type, message));
            }
        }
    }

    private void broadcast(String type, Object message, long chatId) {
        Set<ChatEndpoint> room = ENDPOINTS.get(chatId);
        if (room == null) {
            return;
        }
        for (ChatEndpoint endpoint : room) {
            try {
                endpoint.sendToSession(type, message, endpoint.session);
            } catch (IOException | EncodeException exception) {
                try {
                    endpoint.session.close();
                } catch (IOException ignored) {
                    // Nothing more to do for a broken socket.
                }
            }
        }
    }
}

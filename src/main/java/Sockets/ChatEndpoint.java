package Sockets;


import Classes.Constants;
import Classes.Message;
import DB.ConnectionPool;
import DB.MessageInfoDAO;

import Encode_Decode.MessageDecoder;
import Encode_Decode.MessageEncoder;
import Encode_Decode.OldMessageEncoder;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@WebListener
@ServerEndpoint(value = "/The_Chat", configurator = ChatroomServerConfigurator.class, decoders = MessageDecoder.class, encoders = {MessageEncoder.class, OldMessageEncoder.class})
public class ChatEndpoint implements ServletContextListener {
    private static ServletContext servletContext;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContext = servletContextEvent.getServletContext();
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private static final Set<ChatEndpoint> endpoints = new CopyOnWriteArraySet<ChatEndpoint>();
    private static final Map<Integer, List<ChatEndpoint> > endpointMap = new ConcurrentHashMap<Integer, List<ChatEndpoint>>();
    private Session session;

    @OnOpen
    public void onOpen(EndpointConfig endpointConfig, Session session) throws IOException, EncodeException, SQLException, InterruptedException {
        this.session = session;
        endpoints.add(this);
        if (endpointConfig.getUserProperties().get(Constants.CHAT_ID) == null){
            System.out.println("Please refresh page");
            return;
        }
        long chatId = (Long) endpointConfig.getUserProperties().get(Constants.CHAT_ID);
        long userId = (Long) endpointConfig.getUserProperties().get(Constants.USER_ID);
        String username = (String) endpointConfig.getUserProperties().get(Constants.USERNAME);

        session.getUserProperties().put(Constants.USER_ID, userId);
        session.getUserProperties().put(Constants.CHAT_ID, chatId);
        session.getUserProperties().put(Constants.USERNAME, username);
        System.out.println(chatId + "-" + userId + "-" + username);

        MessageInfoDAO messageInfoDAO = null;
        ConnectionPool connectionPool = (ConnectionPool) servletContext.getAttribute(ConnectionPool.ATTRIBUTE);
        Connection con = connectionPool.getConnection();
        messageInfoDAO = new MessageInfoDAO(con);
        List<Message> list = messageInfoDAO.getLastNMessages(200, chatId);
        con.close();

        sendMessageUser(list, session);
        sendMessageUser(new Message(chatId, userId, "-", "n",   "Now"), session);
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException, SQLException, InterruptedException {
        sendMessage(message);
        MessageInfoDAO messageInfoDAO = null;
        ConnectionPool connectionPool = (ConnectionPool) servletContext.getAttribute(ConnectionPool.ATTRIBUTE);
        Connection con = connectionPool.getConnection();
        messageInfoDAO = new MessageInfoDAO(con);
        System.out.println(message.getContent() + message.getChatId());
        messageInfoDAO.addMessage(message);
        con.close();
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        endpoints.remove(this);
        System.out.println("Disconnected");
       // sendMessage("Discconected :(");
    }

    @OnError
    public void onError(Session session, Throwable t) {
        t.printStackTrace();
    }


    private void sendMessageUser(Object message, Session session) throws IOException, EncodeException {
        session.getBasicRemote().sendObject(message);
    }

    private void sendMessage(Object message) throws IOException, EncodeException {
        for (ChatEndpoint endpoint : endpoints) {
            endpoint.session.getBasicRemote().sendObject(message);
        }
    }
}

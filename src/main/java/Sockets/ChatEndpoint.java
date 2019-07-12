package Sockets;


import Classes.Message;
import DB.ConnectionPool;
import DB.MessageInfoDAO;
import Servlets.Encode_Decode.MessageDecoder;
import Servlets.Encode_Decode.MessageEncoder;
import Servlets.Encode_Decode.OldMessageEncoder;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.Cookie;
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
    private String username, chatId, userId;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContext = servletContextEvent.getServletContext();
     //   System.out.println("Socket part");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private static final Set<ChatEndpoint> endpoints = new CopyOnWriteArraySet<ChatEndpoint>();
    private static final Map<Integer, ChatEndpoint> endpointMap = new ConcurrentHashMap<Integer, ChatEndpoint>();
    private Session session;

    @OnOpen
    public void onOpen(EndpointConfig endpointConfig, Session session) throws IOException, EncodeException, SQLException, InterruptedException {
        this.session = session;
        endpoints.add(this);
        MessageInfoDAO messageInfoDAO = null;
        ConnectionPool connectionPool = (ConnectionPool) servletContext.getAttribute(ConnectionPool.ATTRIBUTE);
        Connection con = connectionPool.getConnection();
        messageInfoDAO = new MessageInfoDAO(con);
        System.out.println("New Connection:");
        List<Message> list = messageInfoDAO.getLastNMessages(100, 3);
        con.close();
        sendMessageUser(list, session);
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException, SQLException, InterruptedException {
        sendMessage(message);
        MessageInfoDAO messageInfoDAO = null;
        ConnectionPool connectionPool = (ConnectionPool) servletContext.getAttribute(ConnectionPool.ATTRIBUTE);
        Connection con = connectionPool.getConnection();
        messageInfoDAO = new MessageInfoDAO(con);
        message.setChatId(3);
        message.setUserId(1);
        message.setUserName("dachi");
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

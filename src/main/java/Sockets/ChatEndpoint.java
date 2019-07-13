package Sockets;


import Classes.Constants;
import Classes.Message;
import DB.ConnectionPool;
import DB.MessageInfoDAO;

import Encode_Decode.MessageEncoder;
import Encode_Decode.OldMessageEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;

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
@ServerEndpoint(value = "/The_Chat", configurator = ChatroomServerConfigurator.class, encoders = {MessageEncoder.class, OldMessageEncoder.class})
public class ChatEndpoint implements ServletContextListener {
    // private static final Set<ChatEndpoint> endpoints = new CopyOnWriteArraySet<ChatEndpoint>();
    private static final Map<Long, Set<ChatEndpoint> > endpointMap = new ConcurrentHashMap<Long, Set<ChatEndpoint>>();
    private static ServletContext servletContext;
    private Session session;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContext = servletContextEvent.getServletContext();
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    @OnOpen
    public void onOpen(EndpointConfig endpointConfig, Session session) throws IOException, EncodeException, SQLException, InterruptedException {
       this.session = session;
       //endpoints.add(this);
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

        if (!endpointMap.containsKey(chatId)){endpointMap.put(chatId, new CopyOnWriteArraySet<ChatEndpoint>());}
        endpointMap.get(chatId).add(this);

        MessageInfoDAO messageInfoDAO = null;
        ConnectionPool connectionPool = (ConnectionPool) servletContext.getAttribute(ConnectionPool.ATTRIBUTE);
        Connection con = connectionPool.getConnection();
        messageInfoDAO = new MessageInfoDAO(con);
        List<Message> list = messageInfoDAO.getLastNMessages(200, chatId);
        con.close();

        sendMessageUser(list, session);
        sendMessageUser(new Message(chatId, userId, username, "n",   "Now"), session);
    }

    @OnMessage
    public void onMessage(Session session, String msg) throws IOException, EncodeException, SQLException, InterruptedException, DecodeException {
        Message message =  decodeMessage(msg);
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
      //  endpoints.remove(this);
        try{
            if (session.getUserProperties().get(Constants.CHAT_ID) != null && endpointMap.containsKey(session.getUserProperties().get(Constants.CHAT_ID))) {
                endpointMap.get(session.getUserProperties().get(Constants.CHAT_ID)).remove(this);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Disconnected Session");
    }

    @OnError
    public void onError(Session session, Throwable t) {
        t.printStackTrace();
    }



    private void sendMessageUser(Object message, Session session) throws IOException, EncodeException {
        session.getBasicRemote().sendObject(message);
    }

    private void sendMessage(Object message) throws IOException, EncodeException {
        Long chatId = ((Message)message).getChatId();
        Set<ChatEndpoint> endpoints = endpointMap.get(chatId);
        for (ChatEndpoint endpoint : endpoints){
            endpoint.session.getBasicRemote().sendObject(message);
        }
    }

    /**
     * Parses the message and responses after the type of message
     */
    private void handleMessage(String message) throws DecodeException {
        Message mes = decodeMessage(message);
    }

    private Message decodeMessage(String s) throws DecodeException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = new Message(1, 1, "-", "Not received", "Infinity");

        try {
            message = mapper.readValue(s, Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }


}

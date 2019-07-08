package Sockets;


import Classes.Message;
import DB.MessageInfoDAO;
import Servlets.Encode_Decode.MessageDecoder;
import Servlets.Encode_Decode.MessageEncoder;
import Servlets.Encode_Decode.WebSocketMessage;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@WebListener
@ServerEndpoint(value = "/The_Chat", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatEndpoint implements ServletContextListener {
    private static ServletContext servletContext;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContext = servletContextEvent.getServletContext();
     //   System.out.println("Socket part");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private static final Set<ChatEndpoint> endpoints = new CopyOnWriteArraySet<ChatEndpoint>();
    private Session session;

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        this.session = session;
        endpoints.add(this);
        MessageInfoDAO messageInfoDAO = (MessageInfoDAO)servletContext.getAttribute(MessageInfoDAO.ATTRIBUTE);
      //  List<Message> list = messageInfoDAO.getLastNMessages();
    }

    @OnMessage
    public void onMessage(Session session, WebSocketMessage message) throws IOException, EncodeException, SQLException {
        sendMessage(message);
        MessageInfoDAO messageInfoDAO = (MessageInfoDAO)servletContext.getAttribute(MessageInfoDAO.ATTRIBUTE);
        messageInfoDAO.addMessage(1, 1, message.getContent());
    //    System.out.println("New message in Server" + message.getContent());
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



    private void sendMessage(WebSocketMessage message) throws IOException, EncodeException {
        for (ChatEndpoint endpoint : endpoints) {
            endpoint.session.getBasicRemote().sendObject(message);
        }
    }
}

package Servlets;


import Classes.Message;
import Servlets.Encode_Decode.MessageDecoder;
import Servlets.Encode_Decode.MessageEncoder;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/The_Chat") //decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatEndpoint {

    private static final Set<ChatEndpoint> endpoints = new CopyOnWriteArraySet<ChatEndpoint>();
    private Session session;

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        this.session = session;
        endpoints.add(this);
        sendMessageTemp(session,"Hello from server");
        System.out.println("new connection");
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException, EncodeException {
        sendMessageTemp(session, "The new message is "+message);
        System.out.println("New message in Server");
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        endpoints.remove(this);
        System.out.println("Disconnected");
        sendMessage("Discconected :(");
    }


    private void sendMessageTemp(Session session, String message) throws IOException, EncodeException {
      //  Message message1 = new Message();
      //  message1.setContent(message);
        session.getBasicRemote().sendObject(message);
    }

    private void sendMessage(String message) throws IOException, EncodeException {
        Message message1 = new Message();
        message1.setContent(message);
        for (ChatEndpoint endpoint : endpoints) {
            endpoint.session.getBasicRemote().sendObject(message);
        }
    }
}

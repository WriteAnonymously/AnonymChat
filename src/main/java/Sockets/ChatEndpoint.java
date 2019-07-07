package Sockets;


import Servlets.Encode_Decode.MessageDecoder;
import Servlets.Encode_Decode.MessageEncoder;
import Servlets.Encode_Decode.WebSocketMessage;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/The_Chat", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatEndpoint {

    private static final Set<ChatEndpoint> endpoints = new CopyOnWriteArraySet<ChatEndpoint>();
    private Session session;

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        this.session = session;
        endpoints.add(this);
        sendMessage(new WebSocketMessage("Hello from server", "server"));
      //  System.out.println("new connection");
    }

    @OnMessage
    public void onMessage(Session session, WebSocketMessage message) throws IOException, EncodeException {
        sendMessage(message);
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

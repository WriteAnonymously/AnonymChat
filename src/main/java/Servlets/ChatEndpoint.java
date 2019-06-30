package Servlets;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/hello")
public class ChatEndpoint {

    private Session session;

    @OnOpen
    public void onCreateSession(Session session){
        this.session = session;
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.println("Message = "+ message);

    }

}

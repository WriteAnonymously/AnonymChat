package Sockets;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class ChatroomServerConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession session = (HttpSession)request.getHttpSession();
        String username = null, chatId = null, userId = null;
        Object obj;
        try{
            obj = session.getAttribute("username");
            if (obj != null){
                username = (String)obj;
            }
            obj = session.getAttribute("chatId");
            if (obj != null){
                chatId = (String)obj;
            }
            obj = session.getAttribute("id");
            if (obj != null){
                userId = (String)obj;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        if (username != null && chatId != null && userId != null){
            sec.getUserProperties().put("username", username);
            sec.getUserProperties().put("chatId", chatId);
            sec.getUserProperties().put("userId", userId);
            System.out.println("Wohoo Configureeeed");
        }
    }
}

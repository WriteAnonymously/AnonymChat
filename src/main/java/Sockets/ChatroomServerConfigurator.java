package Sockets;

import Classes.Constants;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class ChatroomServerConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        System.out.println("Endconfig");
        HttpSession session = (HttpSession)request.getHttpSession();
        String username = null;
        long chatId = -1, userId = -1;
        if (session == null){
            System.out.println("NULL");
            return;
        } else {
            System.out.println("not null");
        }
        try{
            if (session.getAttribute(Constants.USERNAME) != null){
                username = (String)session.getAttribute(Constants.USERNAME) ;
                System.out.println(username);
            } else {
                System.out.println("Parameter not found");
            }
            if (session.getAttribute(Constants.CHAT_ID) != null){
                chatId = (Long) session.getAttribute(Constants.CHAT_ID);
                System.out.println(chatId);
            }
            if (session.getAttribute(Constants.USER_ID) != null){
                userId = (Long) session.getAttribute(Constants.USER_ID);
                System.out.println(userId);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        if (username != null && chatId != -1 && userId != -1){
            sec.getUserProperties().put("username", username);
            sec.getUserProperties().put("chatId", chatId);
            sec.getUserProperties().put("userId", userId);
            System.out.println("Wohoo Configureeeed");
        }
    }
}

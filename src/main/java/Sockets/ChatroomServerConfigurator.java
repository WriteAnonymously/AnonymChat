package Sockets;

import Classes.Chat;
import Classes.Constants;
import Classes.User;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class ChatroomServerConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession session = (HttpSession)request.getHttpSession();
        if (session == null){ return;}
        try{
            if (session.getAttribute(Constants.CHAT_ID) != null){
                String chatId = (String) session.getAttribute(Constants.CHAT_ID);
                if (session.getAttribute(chatId) != null){
                    sec.getUserProperties().put(Constants.FIRST_LOGIN, session.getAttribute(Constants.FIRST_LOGIN));
                    sec.getUserProperties().put(Constants.CHAT_ID, session.getAttribute(Constants.CHAT_ID));
                    sec.getUserProperties().put(chatId, session.getAttribute(chatId));
                    Chat chat = (Chat) session.getAttribute(Constants.CHAT_INFO);
                    sec.getUserProperties().put(Constants.CHAT_INFO, session.getAttribute(Constants.CHAT_INFO));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
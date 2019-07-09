package Sockets;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class ChatroomServerConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession session = (HttpSession)request.getHttpSession();
      /*  String username = (String) session.getAttribute("username");
        String chatId = (String) session.getAttribute("chatId");
        String userId = (String) session.getAttribute("id");
        sec.getUserProperties().put("username", username);
        sec.getUserProperties().put("chatId", chatId);
        sec.getUserProperties().put("userId", userId); */
        System.out.println("Wohoo Configureeeed");
    }
}

package Sockets;

import Classes.Chat;
import Classes.Constants;
import Classes.User;
import DB.DBInfo;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.net.URI;
import java.net.URISyntaxException;

public class ChatroomServerConfigurator extends ServerEndpointConfig.Configurator {
    public static final String HTTP_SESSION = "httpSession";

    @Override
    public boolean checkOrigin(String originHeaderValue) {
        if ("*".equals(DBInfo.ALLOWED_ORIGIN)) {
            return true;
        }
        if (originHeaderValue == null) {
            return false;
        }
        try {
            URI allowed = new URI(DBInfo.ALLOWED_ORIGIN);
            URI origin = new URI(originHeaderValue);
            return allowed.getScheme().equalsIgnoreCase(origin.getScheme())
                    && allowed.getAuthority().equalsIgnoreCase(origin.getAuthority());
        } catch (URISyntaxException | NullPointerException exception) {
            return false;
        }
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession session = (HttpSession)request.getHttpSession();
        if (session == null){ return;}
        Object chatIdValue = session.getAttribute(Constants.CHAT_ID);
        if (chatIdValue == null) {
            return;
        }
        String chatId = chatIdValue.toString();
        Object user = session.getAttribute(chatId);
        Object chat = session.getAttribute(Constants.CHAT_INFO);
        if (user instanceof User && chat instanceof Chat) {
            sec.getUserProperties().put(Constants.FIRST_LOGIN,
                    session.getAttribute(Constants.FIRST_LOGIN));
            sec.getUserProperties().put(Constants.CHAT_ID, chatId);
            sec.getUserProperties().put(chatId, user);
            sec.getUserProperties().put(Constants.CHAT_INFO, chat);
            sec.getUserProperties().put(HTTP_SESSION, session);
        }
    }
}

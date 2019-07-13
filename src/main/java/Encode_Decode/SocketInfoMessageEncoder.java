package Encode_Decode;

import Classes.Chat;
import Classes.Constants;
import Classes.Message;
import Classes.User;
import com.google.gson.Gson;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.util.List;

public class SocketInfoMessageEncoder implements Encoder.Text<SocketInfoMessage>{
    private static Gson gson = new Gson();

    public String encode(SocketInfoMessage socketInfoMessage) throws EncodeException {
        String result = "";
        String type = socketInfoMessage.getType();
        if (type.equals(Constants.SOCKET_INFO_MESSAGE)) {
            Message message = (Message)socketInfoMessage.getObject();
            result = "m"+ gson.toJson(message);
        } else if (type.equals(Constants.SOCKET_INFO_USER)) {
            User user = (User) socketInfoMessage.getObject();
            result = "u"+ gson.toJson(user);
        } else if (type.equals(Constants.SOCKET_INFO_CHAT)) {
            Chat chat = (Chat) socketInfoMessage.getObject();
            result = "c" + gson.toJson(chat);
        } else if (type.equals(Constants.SOCKET_INFO_OLD_MESSAGES)) {
            List<Message> messages = (List<Message>)socketInfoMessage.getObject();
            result = "l" + gson.toJson(messages);
        } else if (type.equals(Constants.SOCKET_INFO_BOT)) {
            String message = (String)socketInfoMessage.getObject();
            result = "b" + message;
        }
        System.out.println(result);
        return result;
    }

    public void init(EndpointConfig endpointConfig) {

    }

    public void destroy() {

    }
}

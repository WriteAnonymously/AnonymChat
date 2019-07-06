package Servlets.Encode_Decode;



import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<WebSocketMessage> {

    public String encode(WebSocketMessage message) throws EncodeException {
        return message.toString();
    }

    public void init(EndpointConfig endpointConfig) {

    }

    public void destroy() {

    }
}

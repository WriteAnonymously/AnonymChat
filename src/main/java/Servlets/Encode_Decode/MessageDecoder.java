package Servlets.Encode_Decode;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<WebSocketMessage> {


    public WebSocketMessage decode(String s) throws DecodeException {
        return new WebSocketMessage(s);
    }

    public boolean willDecode(String s) {
        return s != null;
    }

    public void init(EndpointConfig endpointConfig) {

    }

    public void destroy() {

    }
}

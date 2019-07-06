package Servlets.Encode_Decode;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<WebSocketMessage> {


    public WebSocketMessage decode(String s) throws DecodeException {
        JsonObject jsonObject = new JsonParser().parse("{\"name\": \"John\"}").getAsJsonObject();
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

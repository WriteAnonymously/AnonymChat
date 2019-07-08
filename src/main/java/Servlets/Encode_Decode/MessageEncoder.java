package Servlets.Encode_Decode;



import com.google.gson.Gson;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<WebSocketMessage> {
    private static Gson gson = new Gson();


    public String encode(WebSocketMessage message) throws EncodeException {
        String json = gson.toJson(message);
   //     System.out.println(json+"--- Encoded");
        return json;
    }

    public void init(EndpointConfig endpointConfig) {

    }

    public void destroy() {

    }
}

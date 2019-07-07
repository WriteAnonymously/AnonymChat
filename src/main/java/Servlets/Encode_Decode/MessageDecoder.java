package Servlets.Encode_Decode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

public class MessageDecoder implements Decoder.Text<WebSocketMessage> {
    private static Gson gson = new Gson();

    public WebSocketMessage decode(String s) throws DecodeException {
        ObjectMapper mapper = new ObjectMapper();
        WebSocketMessage message = new WebSocketMessage("Not received", "aa");

        try {
            message = mapper.readValue(s, WebSocketMessage.class);
          //  System.out.println(message.getContent()+"----Decoded");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return message;
    }

    public boolean willDecode(String s) {
        return s != null;
    }

    public void init(EndpointConfig endpointConfig) {

    }

    public void destroy() {

    }
}

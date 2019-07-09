package Servlets.Encode_Decode;

import Classes.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;
import java.sql.Time;

public class MessageDecoder implements Decoder.Text<Message> {

    public Message decode(String s) throws DecodeException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = new Message(1, 1, "Not received", null);

        try {
            message = mapper.readValue(s, Message.class);
      //       System.out.println(message.getContent()+"----Decoded");
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

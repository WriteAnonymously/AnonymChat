package Encode_Decode;

import Classes.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

public class MessageDecoder implements Decoder.Text<Message> {

    public Message decode(String s) throws DecodeException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = new Message(1, 1, "tamro", "Not received", "a");

        System.out.println("es aris S -- " + s);

        try {
            message = mapper.readValue(s, Message.class);
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

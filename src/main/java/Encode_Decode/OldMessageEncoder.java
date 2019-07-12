package Encode_Decode;


import Classes.Message;
import com.google.gson.Gson;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.util.List;

public class OldMessageEncoder implements Encoder.Text<List<Message>> {
    private static Gson gson = new Gson();


    public String encode(List<Message> messages) throws EncodeException {
        String json = "l" + gson.toJson(messages);
    //    System.out.println(json+"--- Encoded");
        return json;
    }


    public void init(EndpointConfig endpointConfig) {

    }

    public void destroy() {

    }
}

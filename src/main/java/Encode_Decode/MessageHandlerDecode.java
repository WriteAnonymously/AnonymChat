package Encode_Decode;

import Classes.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.DecodeException;
import java.io.IOException;

public class MessageHandlerDecode {
    /**
     * Translates JSon Representation of Message to the
     * message object
     */
    public static Message decodeMessage(String s) throws DecodeException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = new Message(1, 1, "-", "Not received", "Infinity");

        try {
            message = mapper.readValue(s, Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * Parses the message and responses after the type of message
     */
    public static void handleMessage(String message) throws DecodeException {
        Message mes = MessageHandlerDecode.decodeMessage(message);
    }

}

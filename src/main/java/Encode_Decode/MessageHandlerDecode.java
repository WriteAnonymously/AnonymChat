package Encode_Decode;

import Classes.Message;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import javax.websocket.DecodeException;
public class MessageHandlerDecode {
    private static final Gson GSON = new Gson();
    /**
     * Translates JSon Representation of Message to the
     * message object
     */
    public static Message decodeMessage(String s) throws DecodeException {
        try {
            return GSON.fromJson(s, Message.class);
        } catch (JsonParseException e) {
            throw new DecodeException(s, "Invalid message payload", e);
        }
    }

    /**
     * Parses the message and responses after the type of message
     */
    public static void handleMessage(String message) throws DecodeException {
        Message mes = MessageHandlerDecode.decodeMessage(message);
    }

}

package Encode_Decode;

import Classes.Message;
import org.junit.Test;

import javax.websocket.DecodeException;

import static org.junit.Assert.assertEquals;

public class MessageHandlerDecodeTest {
    @Test
    public void decodesMessageContent() throws Exception {
        Message message = MessageHandlerDecode.decodeMessage("{\"content\":\"hello\"}");
        assertEquals("hello", message.getContent());
    }

    @Test(expected = DecodeException.class)
    public void rejectsMalformedJson() throws Exception {
        MessageHandlerDecode.decodeMessage("{not-json}");
    }
}

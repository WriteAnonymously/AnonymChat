package DB;

import Classes.Chat;
import Classes.PrivateChat;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ChatInfoDAOAddTest {
    @Test
    public void testChatInfoDAOAdd(){
        Connection con = null;
        try {
            con = PrepareDB.getInstance();
            MessageInfoDAO messageInfoDAO = new MessageInfoDAO(con);
            UserInfoDAO userInfoDAO = new UserInfoDAO(con);
            ChatInfoDAO chatInfoDAO = new ChatInfoDAO(con);
            long chatID = chatInfoDAO.addChat("chat", "public", "kai chat",  1);
            long userID = userInfoDAO.addUser(1, "wvera");
            List<Chat> lst = chatInfoDAO.getTopNChats(1);
            Chat ch = lst.get(0);
            Assert.assertEquals(1, chatID);
            Assert.assertEquals(1, ch.getID());
            chatID = chatInfoDAO.addChat("kai xalxis chati","public","dasveneba minda",2);
            userID = userInfoDAO.addUser(chatID,"damrtymeli-purismchameli");
            Assert.assertEquals(2, chatID);
            Assert.assertEquals(1,userID);
            userID = userInfoDAO.addUser(chatID,"geralt-rivieli");
            Assert.assertEquals(2,userID);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void chatInfoGetInfoTest() throws SQLException, ClassNotFoundException {
        Connection con = PrepareDB.getInstance();
        ChatInfoDAO chatInfoDAO = new ChatInfoDAO(con);
        long chatId = chatInfoDAO.addChat("amxela chati", ChatInfoDAO.PRIVATE, "adeqi dajeqi", 10);
        Chat chat = chatInfoDAO.getChatInfo(chatId);
        Assert.assertTrue(chat instanceof PrivateChat);
        Chat chat1 = new Chat(chatId, "amxela chati", "adeqi dajeqi", 10, null);
        Assert.assertEquals(chat1, chat);
    }
}

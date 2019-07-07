package DB;

import Classes.Chat;
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
            long charID = chatInfoDAO.addChat("chat", "public", "kai chat",  1);
            long userID = userInfoDAO.addUser(1, "wvera");
            List<Chat> lst = chatInfoDAO.getTopNChats(1);
            Chat ch = lst.get(0);
            Assert.assertEquals(1, charID);
            Assert.assertEquals(1, ch.getID());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}

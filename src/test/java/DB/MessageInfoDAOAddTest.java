package DB;

import Classes.Chat;
import Classes.Message;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MessageInfoDAOAddTest {

    @Test
    public void MessageInfoDAOAddTest(){
        Connection con = null;
        try {
            con = PrepareDB.getInstance();
            MessageInfoDAO messageInfoDAO = new MessageInfoDAO(con);
            UserInfoDAO userInfoDAO = new UserInfoDAO(con);
            ChatInfoDAO chatInfoDAO = new ChatInfoDAO(con);
            long chatID = chatInfoDAO.addChat("lamzira", ChatInfoDAO.PRIVATE, "top chat", 1);
            long userID = userInfoDAO.addUser(chatID, "nata");
            messageInfoDAO.addMessage(userID, chatID, "vaso");
            List<Message> lst = messageInfoDAO.getLastNMessages(1, chatID);
            Message m = lst.get(0);
            Assert.assertEquals("vaso", m.getContent());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMessageCorrectTest(){
        try {
            Connection con = PrepareDB.getInstance();
            ChatInfoDAO chatDao = new ChatInfoDAO(con);
            UserInfoDAO userDao = new UserInfoDAO(con);
            MessageInfoDAO messageDao = new MessageInfoDAO(con);
            long chatId = chatDao.addChat("amreza", "saqartvelo", "chven vart saqartvelo",10);
            long userId = userDao.addUser(chatId, "lamara");
            messageDao.addMessage(userId,chatId, "me miyvars balaxi da chemi gogo");
            List<Message> list = messageDao.getLastNMessages(1, chatId);
            Message expectedMessage = new Message(chatId, userId, "lamara", "me miyvars balaxi da chemi gogo", "200");
            Assert.assertEquals("lamara", list.get(0).getUserName());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

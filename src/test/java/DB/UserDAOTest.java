package DB;

import Classes.User;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class UserDAOTest {
    @Test
    public void addUserTest(){
        try {
            Connection con = PrepareDB.getInstance();
            UserInfoDAO userInfoDAO = new UserInfoDAO(con);
            ChatInfoDAO chatInfoDAO = new ChatInfoDAO(con);
            long chatId = chatInfoDAO.addChat("saxalxo", ChatInfoDAO.PRIVATE, "magaria", 10);
            long userId = userInfoDAO.addUser(chatId, "vipi");
            Assert.assertEquals(new User(userId, "vipi", chatId), userInfoDAO.getLastUser());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

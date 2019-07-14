package DB;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RandomIdentificatorsDAOTest {

    @Test
    public void RandomIdentificatorsDAOAddTest(){
        try {
            Connection con = PrepareDB.getInstance();
            RandomIdentificatorsDAO dao = new RandomIdentificatorsDAO(con);
            ChatInfoDAO chatInfoDAO = new ChatInfoDAO(con);
            long chatId = chatInfoDAO.addChat("armazi", ChatInfoDAO.PUBLIC, "mefe qartlisa", 10);
            String s = UUID.randomUUID().toString();
            Set<String> set = new HashSet<String>();
            set.add(s.toString());
            dao.addNotUsedRandomIdentificator(s, chatId);
            Set<String> gotSet = dao.getRandomIdentificatorsSet(chatId);
            Assert.assertEquals(set, gotSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

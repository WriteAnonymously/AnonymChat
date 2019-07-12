package DB;

import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class UsernameDAOTest {
    @Test
    public void addTest() throws SQLException, ClassNotFoundException {
        UsernameDAO dao = new UsernameDAO(PrepareDB.getInstance());
        String actual = "saba";
        dao.addUsername(actual);
        String last = dao.getLastUsername();
        Assert.assertEquals(last, actual);
    }
}

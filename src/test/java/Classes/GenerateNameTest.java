package Classes;

import DB.ChatInfoDAO;
import DB.PrepareDB;
import DB.UserInfoDAO;
import DB.UsernameDAO;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class GenerateNameTest {
    @Test
    public void test() throws SQLException, ClassNotFoundException{
        Connection con = null;
        try{
            con = PrepareDB.getInstance();
            UsernameDAO namesDao = new UsernameDAO(con);
            UserInfoDAO usrDao = new UserInfoDAO(con);
            ChatInfoDAO chatDao = new ChatInfoDAO(con);
            Long id = chatDao.addChat("saxeli", ChatInfoDAO.PUBLIC, "agwera", 100);
            List<String> names = new ArrayList<String>(Arrays.asList("saba", "vaxo", "nata", "dachvi", "churgula", "wvera", "gurama"));
            for(int i = 0; i < names.size(); i++){
                namesDao.addUsername(names.get(i));
            }
            for(int i = 0; i < 5; i++){
                usrDao.addUser(id, names.get(i));
            }
            NameGenerator ng = new NameGenerator(chatDao, namesDao);
            String newUsrName = ng.generateName(id);
            boolean isCorrect = false;
            if(newUsrName.equals("wvera") || newUsrName.equals("gurama")) isCorrect = true;
            Assert.assertTrue(isCorrect);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}

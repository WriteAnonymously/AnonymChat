package DB;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class GetRandomUsernamesTest {

    @Test
    public void addTest(){
        Connection con = null;
        try{
            con = PrepareDB.getInstance();
            UsernameDAO dao = new UsernameDAO(con);
            List<String> names = new ArrayList<String>(Arrays.asList("saba", "vaxo", "nata", "dachvi", "churgula"));
            for(int i = 0; i < names.size(); i++){
                dao.addUsername(names.get(i));
            }
            boolean isCorrect = true;
            Set<String> randomNames = dao.getNUsernames(names.size());
            for(int i = 0; i < names.size(); i++){
                if(!randomNames.contains(names.get(i))) {
                    isCorrect = false;
                }
            }
            Assert.assertTrue(isCorrect);
            Assert.assertTrue(names.size() == randomNames.size());

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void randomTest(){
        Connection con = null;
        try{
            con = PrepareDB.getInstance();
            UsernameDAO dao = new UsernameDAO(con);
            Set<String> names = new HashSet<String>(Arrays.asList("saba", "vaxo", "nata", "dachvi", "churgula"));
            for(String name : names){
                dao.addUsername(name);
            }
            int numNames = 3;
            boolean isCorrect = true;
            Set<String> randomNames = dao.getNUsernames(numNames);
            for(String randomName : randomNames){
                if(!names.contains(randomName)){
                    isCorrect = false;
                }
            }
            Assert.assertTrue(isCorrect);
            Assert.assertTrue(numNames == randomNames.size());

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

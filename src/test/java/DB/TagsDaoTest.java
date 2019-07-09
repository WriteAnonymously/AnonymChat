package DB;

import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TagsDaoTest {
    @Test
    public void testAddAndSearch() throws SQLException, ClassNotFoundException {
        TagsDAO dao = new TagsDAO(PrepareDB.getInstance());
        Set<String> tags = new HashSet<String>();
        List<String> ex = new ArrayList<String>();
        ex.add("saba");
        ex.add("dachvi");
        ex.add("bobola");
        List<String> ex2 = new ArrayList<String>();
        ex2.add("saba");
        ex2.add("givi");
        ex2.add("bobola");
        ex2.add("vaxo");
        List<String> ex3 = new ArrayList<String>();
        ex3.add("bobola");
        ex3.add("dachvi");
        ex3.add("vaxo");

        dao.addTags(ex, 1);
        dao.addTags(ex2, 2);
        dao.addTags(ex3, 3);
        tags.add("saba");
        tags.add("givi");
        tags.add("vaxo");
        List<Long> chats = dao.searchChats(tags);
        Long actual = 2L;
        Assert.assertEquals(chats.size(), 3);
        Assert.assertEquals(chats.get(0), actual);
    }
}

package Classes;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class TextParserTest {

    @Test
    public void normalCaseForAdd(){
        // normal case
        String name = "Chat Name";
        String description = "Chat Description";
        String tags = "tag1, tag2, tag3";
        ArrayList<String> actual = new ArrayList<String>(Arrays.asList("tag1", "tag2", "tag3", "Chat", "Name", "Chat", "Description"));
        ArrayList<String> result = TextParser.parseForAddition(tags, name, description);
        Assert.assertTrue(result.equals(actual));
    }

    @Test
    public void badCaseForAdd(){
        // has no description
        String name = "Chat Name";
        String description = "";
        String tags = "tag1, tag2, tag3";
        ArrayList<String> actual = new ArrayList<String>(Arrays.asList("tag1", "tag2", "tag3", "Chat", "Name"));
        ArrayList<String> result = TextParser.parseForAddition(tags, name, description);
        Assert.assertTrue(result.equals(actual));
    }

    @Test
    public void worseCaseForAdd(){
        // has no description and tags
        String name = "Chat Name";
        String description = "";
        String tags = "";
        ArrayList<String> actual = new ArrayList<String>(Arrays.asList("Chat", "Name"));
        ArrayList<String> result = TextParser.parseForAddition(tags, name, description);
        Assert.assertTrue(result.equals(actual));
    }

    @Test
    public void searchTest(){
        String line = "kargi gogoebi sadac iqnebian";
        ArrayList<String> actual = new ArrayList<String>(Arrays.asList("kargi", "gogoebi", "sadac", "iqnebian"));
        ArrayList<String> result = TextParser.parseForSearch(line);
        Assert.assertTrue(result.equals(actual));
    }

}

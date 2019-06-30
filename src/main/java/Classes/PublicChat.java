package Classes;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;

public class PublicChat extends Chat {
    private ArrayList<String> tags;

    PublicChat(int ID, String name, String description, int limit, int numMembers, Date creationDate) {
        super(ID, name, description, limit, numMembers, creationDate);
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    /**
     * This method takes care of returning tags according to Chat.
     * It should be called after addTag()
     */
    public ArrayList<String> getTags() {
        return tags;
    }
}

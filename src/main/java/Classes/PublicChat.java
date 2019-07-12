package Classes;

import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class PublicChat extends Chat {
    private ArrayList<String> tags;

    public PublicChat(long ID, String name, String description, int limit, String creationDate) {
        super(ID, name, description, limit, creationDate);
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

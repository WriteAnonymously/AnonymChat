package Classes;
import java.sql.Time;
import java.util.Date;
import java.util.HashSet;

public class Chat {
    private long ID;
    private String name;
    private String description;
    private int limit;
    private int numMembers;
    private String creationDate;
    private HashSet<String> usedUsernames;
    private static final int DEFAULT_LIMIT = 1000;

    public Chat(long ID, String name, String description, int limit, String creationDate) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
    }


    /*
     * Gets ID from the Chat
     */
    public long getID() {
        return ID;
    }

    /*
     * Gets name from the Chat
     */
    public String getName() {
        return name;
    }

    /*
     * Gets description from the Chat
     */
    public String getDescription() {
        return description;
    }

    /*
     * Gets numbembers from the Chat
     */
    public int getNumMembers() {
        return numMembers;
    }

    /*
     * Gets creation date from the Chat
     */
    public String getCreationTime() {
        return creationDate;
    }

    /**
     * This method takes care of returning used usernames.
     * It should be called after addUsername()
     */
    public HashSet<String> getUsedUsernames() {
        return usedUsernames;
    }

    /*
     * adds new username
     */
    public void addUsername(String newUsername) {
        usedUsernames.add(newUsername);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Chat)){
            return false;
        }
        Chat chat = (Chat)obj;
        return ID == chat.ID && name.equals(chat.name) && description.equals(chat.description)
                && limit == chat.limit;
    }
}

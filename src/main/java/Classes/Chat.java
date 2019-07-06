package Classes;
import java.util.Date;
import java.util.HashSet;

public class Chat {
    private long ID;
    private String name;
    private String description;
    private int limit;
    private int numMembers;
    private Date creationDate;
    private HashSet<String> usedUsernames;
    private static final int DEFAULT_LIMIT = 1000;

    public Chat(long ID, String name, String description, int limit, Date creationDate) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.numMembers = numMembers;
        this.creationDate = creationDate;
    }


    public long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }

    public int getNumMembers() {
        return numMembers;
    }

    public Date getCreationTime() {
        return creationDate;
    }

    /**
     * This method takes care of returning used usernames.
     * It should be called after addUsername()
     */
    public HashSet<String> getUsedUsernames() {
        return usedUsernames;
    }

    public void addUsername(String newUsername) {
        usedUsernames.add(newUsername);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

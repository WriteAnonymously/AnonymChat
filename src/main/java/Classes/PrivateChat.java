package Classes;
import java.util.ArrayList;
import java.util.Date;

public class PrivateChat extends Chat {
    private ArrayList<String> members;

    public PrivateChat(long ID, String name, String description, int numMembers, Date creationDate) {
        super(ID, name, description, numMembers, creationDate);
    }

    /**
     * This method takes care of returning members of chat.
     * It should be called after calling addMember()
     */
    public ArrayList<String> getMembers() {
        return members;
    }

    public void addMember(String newMember) {
        members.add(newMember);
    }
}

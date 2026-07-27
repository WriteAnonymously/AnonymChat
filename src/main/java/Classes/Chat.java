package Classes;

public class Chat {
    private long ID;
    private String name;
    private String description;
    private int limit;
    private String creationDate;

    public Chat(long ID, String name, String description, int limit, String creationDate) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.limit = limit;
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

    public int getLimit() {
        return limit;
    }

    /*
     * Gets creation date from the Chat
     */
    public String getCreationTime() {
        return creationDate;
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

package Classes;

/*
    მომხმარებლის კლასი.
    ინახავს მომხმარებლის აიდის, ნიქნეიმსა და ჩათის აიდის.
 */
public class User {
    private long id, chatId;
    private String username;

    /** Constructor, obviously */
    public User(long id, String username, long chatId){
        this.id = id;
        this.username = username;
        this.chatId = chatId;
    }

    /**
     *  Gets Id of the user
     */
    public long getId(){
        return this.id;
    }

    /**
     *  Gets name of the user
     */
    public String getUsername(){
        return this.username;
    }

    /**
     *  Gets id of the chat where user is posting
     */
    public long getChatId(){
        return this.chatId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)){
            return false;
        }
        User u = (User) obj;
        return id == u.id && username.equals(u.username) && chatId == u.chatId;
    }
}

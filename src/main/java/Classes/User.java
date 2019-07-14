package Classes;

/*
    მომხმარებლის კლასი.
    ინახავს მომხმარებლის აიდის, ნიქნეიმსა და ჩათის აიდის.
 */
public class User {
    private long id, chatId;
    private String username;

    public User(long id, String username, long chatId){
        this.id = id;
        this.username = username;
        this.chatId = chatId;
    }

    public long getId(){
        return this.id;
    }

    public String getUsername(){
        return this.username;
    }

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

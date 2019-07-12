package Classes;

/*
    მომხმარებლის კლასი.
    ინახავს მომხმარებლის აიდის, ნიქნეიმსა და ჩათის აიდის.
 */
public class User {

    private String id, username, chatId;

    public User(String id, String username, String chatId){
        this.id = id;
        this.username = username;
        this.chatId = chatId;
    }

    public String getId(){
        return this.id;
    }

    public String getUsername(){
        return this.username;
    }

    public String getChatId(){
        return this.chatId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)){
            return false;
        }
        User u = (User) obj;
        return id.equals(u.id) && username.equals(u.username) && chatId.equals(u.chatId);
    }
}

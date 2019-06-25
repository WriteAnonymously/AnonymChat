package back;

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

}

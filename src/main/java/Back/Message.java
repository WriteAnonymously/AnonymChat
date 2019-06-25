package back;

import java.util.Date;

/*
    მესიჯის კლასი.
    ინახავს მესიჯის აიდის, ჩათის აიდის, ავტორის აიდის, ტექსტსა და შექმნის დროს.
 */
public class Message {
    private String id, chatId, userId, content;
    private Date creationDate;

    public Message(String id, String chatId, String userId, String content, Date creationDate){
        this.id = id;
        this.chatId = chatId;
        this.userId = userId;
        this.content = content;
        this.creationDate = creationDate;
    }

    public String getId(){
        return this.id;
    }

    public String getChatId(){
        return this.chatId;
    }

    public String getUserId(){
        return this.userId;
    }

    public String getContent(){
        return this.content;
    }

    public Date getCreationDate(){
        return this.creationDate;
    }
}

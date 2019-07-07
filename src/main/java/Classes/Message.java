package Classes;

import java.util.Date;

/*
    მესიჯის კლასი.
    ინახავს მესიჯის აიდის, ჩათის აიდის, ავტორის აიდის, ტექსტსა და შექმნის დროს.
 */
public class Message {
      private long chatId, userId;
      private String content;
      private Date creationDate;


    public Message(long chatId, long userId, String content, Date creationDate){
        this.chatId = chatId;
        this.userId = userId;
        this.content = content;
        this.creationDate = creationDate;
    }
    public void setContent(String content){
        this.content = content;
    }

    public long getChatId(){
        return chatId;
    }

    public long getUserId(){
        return userId;
    }

    public String getContent(){
        return content;
    }

    public Date getCreationDate(){
        return creationDate;
    }
}

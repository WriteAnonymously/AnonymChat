package Classes;

import java.sql.Time;
import java.util.Date;

/*
    მესიჯის კლასი.
    ინახავს მესიჯის აიდის, ჩათის აიდის, ავტორის აიდის, ტექსტსა და შექმნის დროს.
 */
public class Message {
      private long chatId, userId;
      private String content;
      private String creationDate;

    public Message(){}

    public Message(long chatId, long userId, String content, String creationDate){
        this.chatId = chatId;
        this.userId = userId;
        this.content = content;
        this.creationDate = creationDate;
    }
    public void setContent(String content){
        this.content = content;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public String getCreationDate(){
        return creationDate;
    }
}

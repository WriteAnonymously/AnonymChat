package Classes;

import java.util.Date;

/*
    მესიჯის კლასი.
    ინახავს მესიჯის აიდის, ჩათის აიდის, ავტორის აიდის, ტექსტსა და შექმნის დროს.
 */
public class Message {
      private String chatId, userId, content;
      private String creationDate;


    public Message(String chatId, String userId, String content, String creationDate){
        this.chatId = chatId;
        this.userId = userId;
        this.content = content;
        this.creationDate = creationDate;
    }
    public void setContent(String content){
        this.content = content;
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

    public String getCreationDate(){
        return this.creationDate;
    }
}

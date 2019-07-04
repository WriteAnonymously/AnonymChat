package Classes;

import java.util.Date;

/*
    მესიჯის კლასი.
    ინახავს მესიჯის აიდის, ჩათის აიდის, ავტორის აიდის, ტექსტსა და შექმნის დროს.
 */
public class Message {
    private String content;
  //  private String id, chatId, userId, content;
  //  private String creationDate;


   /* //TODO Stringad gadavakete Date, mere gadmovaketeb isev
    public Message(String id, String chatId, String userId, String content, String creationDate){
        this.id = id;
        this.chatId = chatId;
        this.userId = userId;
        this.content = content;
        this.creationDate = creationDate;
    }
 */
    public void setContent(String content){
        this.content = content;
    }

    /*
    public String getId(){
        return this.id;
    }

    public String getChatId(){
        return this.chatId;
    }

    public String getUserId(){
        return this.userId;
    }

       */

    public String getContent(){
        return this.content;
    }

    /*

    public String getCreationDate(){
        return this.creationDate;
    }

     */

}

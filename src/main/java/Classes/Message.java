package Classes;

import java.sql.Time;
import java.util.Date;

/*
    მესიჯის კლასი.
    ინახავს მესიჯის აიდის, ჩათის აიდის, ავტორის აიდის, ტექსტსა და შექმნის დროს.
 */
public class Message {
    private long chatId, userID;
    private String userName;
    private String content;
    private String creationDate;

    public Message(long chatId, long userID, String userName, String content, String creationDate){
        this.chatId = chatId;
        this.userID = userID;
        this.userName = userName;
        this.content = content;
        this.creationDate = creationDate;
    }

    public long getChatId(){
        return chatId;
    }

    public void setChatId(long chatId){
        this.chatId = chatId;
    }

    public void setUserId(long userId){
        this.userID = userId;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public long getUserId(){
        return userID;
    }

    public String getUserName(){
        return userName;
    }

    public String getContent(){
        return content;
    }

    public String getCreationDate(){
        return creationDate;
    }

    /*
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Message)){
            return false;
        }
        Message m = (Message) obj;
        return content.equals(m.content) && userName.equals(m.userName) && userID == m.userID
                    && chatId == m.chatId;
    } */
}

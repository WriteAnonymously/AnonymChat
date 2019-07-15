package Classes;

import java.sql.Time;
import java.util.Date;
import java.util.List;

/*
    მესიჯის კლასი.
    ინახავს მესიჯის აიდის, ჩათის აიდის, ავტორის აიდის, ტექსტსა და შექმნის დროს.
 */
public class Message {
    private long chatId, userId;
    private String userName, content, creationDate;

    public Message(){}
    public Message(long chatId, long userId, String userName, String content, String creationDate){
        this.chatId = chatId;
        this.userId = userId;
        this.userName = userName;
        this.content = content;
        this.creationDate = creationDate;
    }

    /*
     * Gets id of particular chat where message was written
     */
    public long getChatId() {
        return chatId;
    }

    /*
     * Gets username of sender of the message
     */
    public String getUserName() {
        return userName;
    }

    /*
     * Gets id of sender of the message
     */
    public long getUserId() {
        return userId;
    }

    /*
     * Gets content of the message
     */
    public String getContent() {
        return content;
    }

    /*
     * Gets date when message was written
     */
    public String getCreationDate() {
        return creationDate;
    }

    /*
     * sets id of sender of the message
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /*
     * sets id of sender of the message
     */
    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    /*
     * Sets date when message was written
     */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    /*
     * Sets username of sender of the message
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /*
     * Sets content of the message
     */
    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Message)){
            return false;
        }
        Message m = (Message) obj;
        return content.equals(m.content) && userName.equals(m.userName) && userId == m.userId
                    && chatId == m.chatId;
    }
}

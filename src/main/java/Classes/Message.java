package Classes;

import java.sql.Time;
import java.util.Date;

/*
    მესიჯის კლასი.
    ინახავს მესიჯის აიდის, ჩათის აიდის, ავტორის აიდის, ტექსტსა და შექმნის დროს.
 */
public class Message {
    private long chatId, userId;
    private String userName, content, creationDate;

    public Message (){}
    public Message(long chatId, long userId, String userName, String content, String creationDate){
        this.chatId = chatId;
        this.userId = userId;
        this.userName = userName;
        this.content = content;
        this.creationDate = creationDate;
    }

    public long getChatId() {
        return chatId;
    }

    public String getUserName() {
        return userName;
    }

    public long getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setContent(String content) {
        this.content = content;
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

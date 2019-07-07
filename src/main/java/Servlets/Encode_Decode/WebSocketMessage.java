package Servlets.Encode_Decode;

public class WebSocketMessage {
    private String content, user;

    public WebSocketMessage(){}
    public WebSocketMessage(String content, String user){
        this.content = content;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setContent(){
        this.content = content;
    }

    public String getContent(){
        return this.content;
    }

    @Override
    public String toString() {
        return "{content='" +this.content+'\'' +"}";
    }
}

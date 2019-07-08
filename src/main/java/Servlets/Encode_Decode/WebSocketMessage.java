package Servlets.Encode_Decode;

public class WebSocketMessage {
    private String content;
    private int user;

    public WebSocketMessage(){}
    public WebSocketMessage(String content, int user){
        this.content = content;
        this.user = user;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
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

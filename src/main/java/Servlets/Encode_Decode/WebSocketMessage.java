package Servlets.Encode_Decode;

public class WebSocketMessage {
    private String content;

    public WebSocketMessage(){}
    public WebSocketMessage(String content){
        this.content = content;
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

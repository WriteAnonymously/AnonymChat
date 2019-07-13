package Encode_Decode;

public class SocketInfoMessage {
    String type;
    Object object;
    public SocketInfoMessage(){}

    public SocketInfoMessage(String type, Object object){
        this.type = type;
        this.object = object;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public String getType() {
        return type;
    }
}

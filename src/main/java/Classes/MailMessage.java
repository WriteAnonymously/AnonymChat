package Classes;

public class MailMessage {

    private String mail, content, subject;

    public MailMessage(String mail, String content, String subject){
        this.content = content;
        this.mail = mail;
        this.subject = subject;
    }

    /*
     * Gets mail of particular message
     */
    public String getMail(){
        return mail;
    }

    /*
     * Gets content of particular message
     */
    public String getContent(){
        return content;
    }

    /*
     * Gets subject of particular message
     */
    public String getSubject(){
        return subject;
    }

    @Override
    public String toString() {
        return "{\"to\":" + mail + ", \"subject\":" + subject + ", \"content\":" + content + "}";
    }
}
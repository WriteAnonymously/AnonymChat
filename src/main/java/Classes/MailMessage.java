package Classes;

public class MailMessage {

    private String mail, content, subject;

    public MailMessage(String mail, String content, String subject){
        this.content = content;
        this.mail = mail;
        this.subject = subject;
    }

    public String getMail(){
        return mail;
    }

    public String getContent(){
        return content;
    }

    public String getSubject(){
        return subject;
    }

    @Override
    public String toString() {
        return "{\"to\":" + mail + ", \"subject\":" + subject + ", \"content\":" + content + "}";
    }
}
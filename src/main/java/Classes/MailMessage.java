package Classes;

public class MailMessage {

    private String mail, content, subject;
    private boolean html;

    public MailMessage(String mail, String content, String subject){
        this(mail, content, subject, false);
    }

    public MailMessage(String mail, String content, String subject, boolean html){
        this.content = content;
        this.mail = mail;
        this.subject = subject;
        this.html = html;
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

    public boolean isHtml() {
        return html;
    }
}

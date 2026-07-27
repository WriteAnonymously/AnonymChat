package Classes;

import DB.DBInfo;

import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MessageSender {
    private List<MailMessage> toAccounts;

    public MessageSender(List <MailMessage> to){
        this.toAccounts = new ArrayList<MailMessage>();
        int size = to.size();
        for (int i=0; i<size; ++i){
            toAccounts.add(to.get(i));
        }
    }

    /**
     * sends all the mails to accounts provided
     * */
    public void send() throws MessagingException {
        int size = toAccounts.size();
        for (int i=0; i<size; ++i){
            sendMail(toAccounts.get(i));
        }
    }

    /**
     * sends a mail to the account provided
     * */
    private void sendMail(MailMessage to) throws MessagingException {
        Properties properties = new Properties();
        boolean useAuthentication = !DBInfo.SMTP_USERNAME.isBlank();
        properties.put("mail.smtp.auth", Boolean.toString(useAuthentication));
        properties.put("mail.smtp.starttls.enable", Boolean.toString(DBInfo.SMTP_STARTTLS));
        properties.put("mail.smtp.host", DBInfo.SMTP_HOST);
        properties.put("mail.smtp.port", Integer.toString(DBInfo.SMTP_PORT));

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return useAuthentication
                        ? new PasswordAuthentication(DBInfo.SMTP_USERNAME, DBInfo.SMTP_PASSWORD)
                        : null;
            }
        });

        Message message = prepareMessage(session, to);
        if (useAuthentication) {
            Transport.send(message, DBInfo.SMTP_USERNAME, DBInfo.SMTP_PASSWORD);
        } else {
            Transport.send(message);
        }
    }

    /**
     * prepares message for sending
     * */

    private javax.mail.Message prepareMessage(Session session, MailMessage to) throws MessagingException {
        javax.mail.Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(DBInfo.SMTP_FROM));
        message.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to.getMail()));
        message.setSubject(to.getSubject());
        if (to.isHtml()) {
            message.setContent(to.getContent(), "text/html; charset=UTF-8");
        } else {
            message.setText(to.getContent());
        }
        return message;
    }
}

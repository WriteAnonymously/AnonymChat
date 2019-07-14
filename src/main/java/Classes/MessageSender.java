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
        System.out.println("Started sending to -- " + to);
        Properties properties = new Properties();
        properties.put("mail.stmp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(DBInfo.myAccountEmail, DBInfo.myAccountPassword);
            }
        });

        Message message = prepareMessage(session, to);

        Transport.send(message, DBInfo.myAccountEmail, DBInfo.myAccountPassword);
        System.out.println("message sent to " + to);
    }

    /**
     * prepares message for sending
     * */
    private javax.mail.Message prepareMessage(Session session, MailMessage to) throws MessagingException {
        javax.mail.Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(DBInfo.myAccountEmail));
        message.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to.getMail()));
        message.setSubject(to.getSubject());
        ((MimeMessage) message).setText(to.getContent(), "UTF-8");
        return message;
    }
}
package Classes;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MailSenderWorker extends Thread {
    private Set<String> mails;
    private List<String> randomStrings;
    private String link;

    public MailSenderWorker(Set<String> mails, List<String> randomStrings, String link){
        this.mails = mails;
        this.randomStrings = randomStrings;
        this.link = link;
    }

    @Override
    public void run() {
        System.out.println("daiwyo gagzavna");
        System.out.println("Set size is --- " + mails.size());
        List<MailMessage> messages = new ArrayList<MailMessage>();
        int pos = 0;
        for (String s:mails){
            MailMessage curMessage = new MailMessage(s, link + randomStrings.get(pos), "Invite in group");
            messages.add(curMessage);
        }
        MessageSender sender = new MessageSender(messages);
        try {
            sender.send();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        System.out.println("daamtavra gagzavna");
    }
}

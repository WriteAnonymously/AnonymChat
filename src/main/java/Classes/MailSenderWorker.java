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
        List<MailMessage> messages = new ArrayList<MailMessage>();
        int pos = 0;
        for (String s:mails){
            String curLink = link;
            int ind = curLink.indexOf("$");
            curLink = curLink.substring(0, ind) + randomStrings.get(pos) + curLink.substring(ind + 1);
            System.out.println(curLink);
            MailMessage curMessage = new MailMessage(s, curLink, "Invite in group");
            messages.add(curMessage);
        }
        MessageSender sender = new MessageSender(messages);
        try {
            sender.send();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

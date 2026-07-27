package Classes;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MailSenderWorker extends Thread {
    private static final Logger LOGGER =
            Logger.getLogger(MailSenderWorker.class.getName());
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
            MailMessage curMessage = new MailMessage(
                    s,
                    "<p>You were invited to a private room on AnonymChat.</p>"
                            + "<p><a href=\"" + curLink + "\">Enter the room</a></p>"
                            + "<p>This link is personal and can only be claimed once.</p>",
                    "Your private AnonymChat invitation",
                    true);
            messages.add(curMessage);
            pos++;
        }
        MessageSender sender = new MessageSender(messages);
        try {
            sender.send();
        } catch (MessagingException exception) {
            LOGGER.log(Level.SEVERE, "Could not send private-room invitations", exception);
        }
    }
}

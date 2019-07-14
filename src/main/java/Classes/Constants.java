package Classes;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final String CHAT_ID = "chatId";
    public static final String USERNAME = "username";
    public static final String USER_ID = "userId";
    public static final String USER_ATR = "user";
    public static final String CHAT_INFO = "chatInfo";
    public static final String SOCKET_INFO_MESSAGE = "message";
    public static final String SOCKET_INFO_OLD_MESSAGES = "oldMessages";
    public static final String SOCKET_INFO_CHAT = "newChatInfo";
    public static final String SOCKET_INFO_USER = "newUserInfo";
    public static final String SOCKET_INFO_BOT = "botMessage";
    public static final String FIRST_LOGIN = "firstLogin";

    public static void main(String[] args){
        List<MailMessage> list = new ArrayList<MailMessage>();
        list.add(new MailMessage("vkoto17@freeuni.edu.ge", "გამარჯობა სალამი", "try try try it"));
        list.add(new MailMessage("sstur17@freeuni.edu.ge", "გამარჯობა სალამი", "try try try it"));
        list.add(new MailMessage("dkurt17@freeuni.edu.ge", "გამარჯობა სალამი", "try try try it"));
        list.add(new MailMessage("nchur17@freeuni.edu.ge", "გამარჯობა სალამი", "try try try it"));
        list.add(new MailMessage("nkhur17@freeuni.edu.ge", "გამარჯობა სალამი", "try try try it"));
        MessageSender sender = new MessageSender(list);
        try {
            sender.send();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

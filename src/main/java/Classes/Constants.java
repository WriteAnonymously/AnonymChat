package Classes;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final String CHAT_ID = "chatId";
    public static final String USERNAME = "username";
    public static final String USER_ID = "userId";
    public static final String USER_ATR = "user";

    public static void main(String[] args){
        List<MailMessage> list = new ArrayList<MailMessage>();
//        list.add("v.kotoreishvili@gmail.com");
//        list.add("vkoto17@freeuni.edu.ge");
//        list.add("sstur17@freeuni.edu.ge");
//        list.add("dkurt17@freeuni.edu.ge");
//        list.add("nkhur17@freeuni.edu.ge");
//        list.add("nchur17@freeuni.edu.ge");
        MessageSender sender = new MessageSender(list);
        try {
            sender.send();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
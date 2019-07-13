package Classes;

import DB.ChatInfoDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class ChatBot {
    private Connection con;
    private long chatId;
    Random rand = new Random(System.currentTimeMillis());

    public ChatBot(long chatId, Connection con) {
        this.chatId = chatId;
        this.con = con;
    }

    public String answerMessage(String query) throws SQLException {
        return randomUser();
    }

    public String randomUser() throws SQLException {
        String result = "";
        ChatInfoDAO chatInfoDAO = new ChatInfoDAO(con);
        Set<String> usernames = chatInfoDAO.getUserNames(chatId);
        int index = rand.nextInt(usernames.size());
        Iterator<String> iter = usernames.iterator();
        for (int i = 0; i < index; i++) {
            iter.next();
        }
        result = iter.next();
        return  result;
    }
}
package Classes;

import DB.ChatInfoDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class ChatBot {
    Map<Long, String> wordsMap;
    Random rand = new Random(System.currentTimeMillis());

    public ChatBot() {
        wordsMap = new HashMap<Long, String>();
    }

    public String announceNewUser(String username){
        return "Say Hello to "+username;
    }

    public String answerMessage(String query, String username, long chatId, Connection con) throws SQLException {
        int type = getType(query);
        if (type == 0) {
            return "the random user is" +randomUser(chatId, con);
        } else if (type == 1){
            return randomNumber(10);
        } else if (type == 2){
            addNewWord(chatId, randomUser(chatId, con));
            return "guess who is he/she " + username + " explains";
        } else if (type == 3){
            if (wordsMap.containsKey(chatId)){
                String word = wordsMap.get(chatId);
                if (query.contains(word)){
                    wordsMap.remove(chatId);
                    return username+ " gets it!";
                } else {
                    return "nope";
                }
            } else {
                return "no word to guess";
            }
        }
        return "Beep, Beep. I don't know the command";
    }

    public int getType(String query){
        if (query.startsWith("BOT:RANDOM USER")) {
            return 0;
        } else if (query.startsWith("BOT:RANDOM NUMBER")){
            return 1;
        } else if (query.startsWith("BOT:GUESS")){
            return 2;
        } else if (query.startsWith("BOT:ANSWER")){
            return 3;
        }
        return -1;
    }

    public String answerWord(Long chatId){
        if (wordsMap.containsKey(chatId)) {
            return wordsMap.get(chatId);
        } else {
            return "NO WORD TO GUESS!! sorry ;)";
        }
    }

    public String randomUser(long chatId, Connection con) throws SQLException {
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

    public String randomNumber(int max){
        return Integer.toString(rand.nextInt(max));
    }

    public void addNewWord(long chatId, String word){
        wordsMap.put(chatId, word);
    }
}

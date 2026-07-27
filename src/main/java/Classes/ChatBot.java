package Classes;

import DB.GuessDAO;
import Sockets.ChatEndpoint;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class ChatBot {
    private Connection con;
    Random rand = new Random(System.currentTimeMillis());

    public ChatBot(Connection con) {
        this.con = con;
    }

    /**
     * Announces when new user has entered the server
     */
    public String announceNewUser(String username){
        return "Say Hello to "+username;
    }

    /**
     * Answers the query of user
     */
    public String answerMessage(String query, String username, long chatId, Connection con) throws SQLException {
        int type = getType(query);
        if (type == 0) {
            return "the random user is " +randomUser(chatId);
        } else if (type == 1){
            return randomNumber(10);
        } else if (type == 2){
            String theWord = randomUser(chatId);
            addNewWord(chatId, theWord);
            return "guess who is he/she " + username + " explains";
        } else if (type == 3){
            String ans = answerWord(chatId);
            if (ans.length() > 0){
                if (query.contains(ans)){
                    return username + " gets it!";
                } else {
                    return "nope";
                }
            }
            return "no word found";
        }
        return "Beep, Beep. I don't know the command";
    }

    /**
     * returns type of query
     */
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


    /**
     * Checks what is the word to guess
     */
    public String answerWord(Long chatId) throws SQLException {
        GuessDAO guessDAO = new GuessDAO(con);
        Guess ans = guessDAO.getGuessForChat(chatId);
        if (ans != null){
            return ans.getWord();
        }
        return "";
    }

    /**
     * Returns random user from the chat
     */
    public String randomUser(long chatId) throws SQLException {
        Set<String> usernames = ChatEndpoint.getActiveUserNames(chatId);
        if (usernames.isEmpty()) {
            return "nobody yet";
        }
        int index = rand.nextInt(usernames.size());
        Iterator<String> iter = usernames.iterator();
        for (int i = 0; i < index; i++) {
            iter.next();
        }
        return iter.next();
    }

    /* Returns random number*/
    public String randomNumber(int max){
        return Integer.toString(rand.nextInt(max));
    }

    public void addNewWord(long chatId, String word) throws SQLException {
        GuessDAO guessDAO = new GuessDAO(con);
        Guess have = guessDAO.getGuessForChat(chatId);
        if (have == null){
            guessDAO.addGuess(chatId, word);
        } else {
            guessDAO.updateGuess(chatId, word);
        }
    }
}

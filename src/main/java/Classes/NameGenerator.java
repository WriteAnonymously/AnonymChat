package Classes;

import DB.ChatInfoDAO;
import DB.UsernameDAO;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class NameGenerator {
    private ChatInfoDAO chatdao;
    private UsernameDAO userdao;


    public NameGenerator(ChatInfoDAO chatdao, UsernameDAO userdao){
        this.chatdao = chatdao;
        this.userdao = userdao;
    }

    /**
     * generates random username for user
     *
     * @param chatID id of chat
     * */
    public String generateName(long chatID) throws SQLException {
        Set<String> userNames = chatdao.getUserNames(chatID);
        int size = userNames.size();
        Set<String> randomNames = userdao.getNUsernames(size + 1);
        String randomName = "";

        randomName = generateRandomName(randomNames, userNames, true);
        return randomName;
    }

    private String generateRandomName(Set<String> randomNames, Set<String> names,  boolean canRepeat) {
        String result = "";
        if (canRepeat){
            Random rand = new Random();
            int index = rand.nextInt(randomNames.size());
            Iterator<String> iter = randomNames.iterator();
            for (int i = 0; i < index; i++) {
                iter.next();
            }
            result = iter.next();
        } else {
            for(String name : randomNames){
                if(!names.contains(name)){
                    result = name;
                    break;
                }
            }
        }
        return result;
    }
}

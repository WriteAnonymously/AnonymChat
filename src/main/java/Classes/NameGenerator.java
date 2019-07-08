package Classes;

import DB.ChatInfoDAO;
import DB.UsernameDAO;

import java.sql.SQLException;
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
        Set<String> usernames = chatdao.getUserNames(chatID);
        int size = usernames.size();
        Set<String> randomNames = userdao.getNUsernames(size + 1);
        String randomName = "";
        for(String name : randomNames){
            if(!usernames.contains(name)){
                randomName = name;
                break;
            }
        }
        return randomName;
    }
}

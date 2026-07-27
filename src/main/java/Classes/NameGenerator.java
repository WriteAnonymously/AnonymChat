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
        Set<String> userNames = chatdao.getUserNames(chatID);
        int size = userNames.size();
        Set<String> randomNames = userdao.getNUsernames(size + 1);
        String randomName = generateRandomName(randomNames, userNames);
        if (!randomName.isEmpty()) {
            return randomName;
        }
        int suffix = size + 1;
        while (userNames.contains("Guest-" + suffix)) {
            suffix++;
        }
        return "Guest-" + suffix;
    }

    private String generateRandomName(Set<String> randomNames, Set<String> names) {
        for (String name : randomNames) {
            if (!names.contains(name)) {
                return name;
            }
        }
        return "";
    }
}

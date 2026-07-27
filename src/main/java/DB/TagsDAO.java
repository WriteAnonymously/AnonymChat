package DB;

import Classes.Chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TagsDAO {
    private Connection con;
    public static final String ATTRIBUTE = "tagsInfo";


    public TagsDAO(Connection con){
        this.con = con;
    }

    /**
     * adds new tag in database
     *
     * @param tags list of tags
     * @param chatID id of chat
     * @throws SQLException throws exception if occur any error
     * */
    public void addTags(List<String> tags, long chatID) throws SQLException {
        try (PreparedStatement statement = con.prepareStatement(
                "insert into " + DBInfo.TAG_TABLE + " (name, chatid) value (?, ?)")) {
            for (String tag : tags) {
                statement.setString(1, tag);
                statement.setLong(2, chatID);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    /**
     * finds most suitable chats for given tags
     *
     * @param tags list of tags
     * @throws SQLException throws exception if occur any error
     * */
    public ArrayList<Chat> searchChats(ArrayList<String> tags) throws SQLException {
        int maxNumberOfChats = 10;
        Map<Long, Integer> ids = new HashMap<Long, Integer>();
        for (String tag : tags) {
            try (PreparedStatement statement = con.prepareStatement(
                    "select t.chatid from " + DBInfo.TAG_TABLE + " t "
                            + "join " + DBInfo.CHAT_TABLE + " c on c.id = t.chatid "
                            + "where locate(lower(?), lower(t.name)) > 0 "
                            + "and lower(c.status) = 'public'")) {
                statement.setString(1, tag);
                try (ResultSet set = statement.executeQuery()) {
                    while (set.next()) {
                        long id = set.getLong(1);
                        ids.merge(id, 1, Integer::sum);
                    }
                }
            }
        }
        List<Map.Entry<Long, Integer>> sorted = new ArrayList<>(ids.entrySet());
        sorted.sort(Map.Entry.<Long, Integer>comparingByValue().reversed());

        ChatInfoDAO chatdao = new ChatInfoDAO(con);
        ArrayList<Chat> result = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry :
                sorted.subList(0, Math.min(sorted.size(), maxNumberOfChats))) {
            Chat chat = chatdao.getChatInfo(entry.getKey());
            if (chat != null) {
                result.add(chat);
            }
        }
        return result;
    }

    /**
     * returns all the tags of a given chat
     *
     * @param chatId id of chat
     * @throws SQLException throws exception if occur any error
     * */
    public ArrayList<String> getTags(Long chatId) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        try (PreparedStatement statement = con.prepareStatement(
                "select name from " + DBInfo.TAG_TABLE + " t where t.chatid = ?")) {
            statement.setLong(1, chatId);
            try (ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    result.add(set.getString("name"));
                }
            }
        }
        return result;
    }


}

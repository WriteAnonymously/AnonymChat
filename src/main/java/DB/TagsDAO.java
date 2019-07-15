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
        for(int i = 0; i < tags.size(); i++) {
            PreparedStatement statement = con.prepareStatement("insert into " + DBInfo.TAG_TABLE
                    + " (name, chatid) value "
                    + "(?, ?);");
            statement.setString(1, tags.get(i));
            statement.setLong(2, chatID);
            statement.executeUpdate();
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
        for(String tag : tags) {
            PreparedStatement statement = con.prepareStatement("select chatid from " + DBInfo.TAG_TABLE
                    + " t where t.name = ?;");
            statement.setString(1, tag);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Long id = set.getLong(1);
                if(ids.containsKey(id)){
                    int freq = ids.get(id);
                    ids.put(id, freq + 1);
                } else {
                    ids.put(id, 1);
                }
            }
        }
        // sorting by their popularity
        Object[] sorted = ids.entrySet().toArray();
        Arrays.sort(sorted, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<Long, Integer>) o2).getValue().compareTo(
                        ((Map.Entry<Long, Integer>) o1).getValue());
            }
        });
        ArrayList<Long> chats = new ArrayList<Long>();

        int len = ids.size() > maxNumberOfChats ? maxNumberOfChats : ids.size();
        for(int i = 0; i < len; i++){
            long chatID = ((Map.Entry<Long, Integer>)sorted[i]).getKey();
            chats.add(chatID);
        }
        // getting chats from table
        ChatInfoDAO chatdao = new ChatInfoDAO(con);
        ArrayList<Chat> result = new ArrayList<Chat>();
        for(int i = 0; i < chats.size(); i++){
            Chat chat = chatdao.getChatInfo(chats.get(i));
            result.add(chat);
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
        PreparedStatement statement = con.prepareStatement("select name from " + DBInfo.TAG_TABLE
                + " t where t.chatid = ?;");
        statement.setLong(1, chatId);
        ResultSet set = statement.executeQuery();
        while(set.next()){
            String tag = set.getString("name");
            result.add(tag);
        }
        statement.close();
        return result;
    }


}
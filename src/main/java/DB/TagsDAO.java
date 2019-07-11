package DB;

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
        System.out.println(chatID + "id");
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
    public List<Long> searchChats(Set<String> tags) throws SQLException {
        int maxNumberOfChats = 20;
        Map<Long, Integer> results = new HashMap<Long, Integer>();
        for(String tag : tags) {
            PreparedStatement statement = con.prepareStatement("select chatid from " + DBInfo.TAG_TABLE
                    + " t where t.name = ?;");
            statement.setString(1, tag);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Long id = set.getLong(1);
                if(results.containsKey(id)){
                    int freq = results.get(id);
                    results.put(id, freq + 1);
                } else {
                    results.put(id, 1);
                }
            }
        }
        Object[] sorted = results.entrySet().toArray();
        Arrays.sort(sorted, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<Long, Integer>) o2).getValue().compareTo(
                        ((Map.Entry<Long, Integer>) o1).getValue());
            }
        });
        List<Long> chats = new ArrayList<Long>();

        int len = results.size() > maxNumberOfChats ? maxNumberOfChats : results.size();
        for(int i = 0; i < len; i++){
            long chatID = ((Map.Entry<Long, Integer>)sorted[i]).getKey();
            chats.add(chatID);
        }
        return chats;
    }
}
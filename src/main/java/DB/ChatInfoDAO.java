package DB;

import Classes.Chat;
import Classes.PublicChat;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatInfoDAO {
    private Connection connection;
    public static final String ATTRIBUTE = "chatInfo";
    public static String PUBLIC = "Public";
    public static String PRIVATE = "Private";
    public ChatInfoDAO(Connection connection){
        this.connection = connection;
    }

    /**
     * adds new chat into database with info provided
     *
     * @param chatName name of chat
     * @param status status of chat public/private
     * @param maxUsersAllowed maximum number of user -1 should be here if no restrictions
     * */
    public long addChat(String chatName, String status, String description, int maxUsersAllowed) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("insert into " + DBInfo.CHAT_TABLE
                + " (name, status, description, max_users_number, creation_date) value "
                + "(?, ?, ?, ?, sysdate());");
        statement.setString(1, chatName);
        statement.setString(2, status);
        statement.setString(3, description);
        statement.setInt(4, maxUsersAllowed);
        synchronized (connection) {
            statement.executeUpdate();
            return getLastInsertedID();
        }
    }


    /**
     * finds the last inserted id
     * */
    private long getLastInsertedID() {
        try {
            Statement st = connection.createStatement();
            String q = "select LAST_INSERT_ID();";
            ResultSet set = st.executeQuery(q);
            set.last();
            return Long.parseLong(set.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * gets top n public chat by number of members
     *
     * @param n number of top chats
     * */
    public List<Chat> getTopNChats(int n) throws SQLException {
        List<Chat> topChatsSet = new ArrayList<Chat>();
        PreparedStatement statement = connection.prepareStatement("select * from " + DBInfo.CHAT_TABLE
                                + " t where t.status = \"public\" order by -1 * (select count(*) from " + DBInfo.USERS_TABLE
                                + " cus where cus.chatid = t.id) limit ?;");
        statement.setInt(1, n);
        System.out.println(statement.toString());
        ResultSet set = statement.executeQuery();
        while (set.next()){
            String name = set.getString("name");
            long id = Long.parseLong(set.getString("id"));
            String description = set.getString("description");
            int maxNumber = Integer.parseInt(set.getString("max_users_number"));
            String status = set.getString("status");
            Date date = Date.valueOf(set.getString("creation_date"));
            PublicChat chat = new PublicChat(id, name, description, maxNumber, date);
            topChatsSet.add(chat);
        }
        return topChatsSet;
    }

    /**
     * returns set of usernames used in chat
     *
     * @param chatID id of chat for which set should be found
     * @return Set<String> set of the usernames in chat
     * */
    public Set<String> getUserNames(long chatID) throws SQLException {
        Set<String> userNames = new HashSet<String>();
        PreparedStatement statement = connection.prepareStatement("select name from " + DBInfo.USERS_TABLE
                                + "where chatid = ?;");
        statement.setLong(1, chatID);
        ResultSet set = statement.executeQuery();
        while (set.next()){
            userNames.add(set.getString(1));
        }
        return userNames;
    }

}

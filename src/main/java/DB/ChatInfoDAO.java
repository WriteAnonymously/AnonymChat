package DB;

import Classes.Chat;
import Classes.PublicChat;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ChatInfoDAO {
    private Connection connection;

    public ChatInfoDAO(Connection connection){
        this.connection = connection;
    }

    /**
     * adds new chat into database with info provided
     *
     * @param chatID id of chat
     * @param chatName name of chat
     * @param status status of chat public/private
     * @param maxUsersAllowed maximum number of user -1 should be here if no restrictions
     * */
    public String addChat(String chatID, String chatName, String status, int maxUsersAllowed) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("insert into " + DBconnector.CHAT_TABLE
                + " (id, name, status, max_users, creation_date) value "
                + "(?, ?, ?, ?, sysdate());");
        statement.setString(1, chatID);
        statement.setString(2, chatName);
        statement.setString(3, status);
        statement.setInt(4, maxUsersAllowed);
        synchronized (connection) {
            statement.executeUpdate();
            return getLastInsertedID();
        }
    }


    /**
     * finds the last inserted id
     * */
    private String getLastInsertedID() {
        try {
            Statement st = connection.createStatement();
            String q = "select LAST_INSERT_ID();";
            ResultSet set = st.executeQuery(q);
            set.last();
            return set.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * gets top n public chat by number of members
     *
     * @param n number of top chats
     * */
    public Set<Chat> getTopNChats(int n) throws SQLException {
        Set <Chat> topChatsSet = new HashSet<Chat>();
        PreparedStatement statement = connection.prepareStatement("select * from " + DBconnector.CHAT_TABLE
                                + " t where t.status = \"public\" order by (select count(*) from " + DBconnector.USERS_TABLE
                                + " cus where cus.chat_id = t.id) limit ?");
        statement.setInt(1, n);
        ResultSet set = statement.executeQuery();
        while (set.next()){
            String name = set.getString("name");
            long id = Long.parseLong(set.getString("id"));
            String description = set.getString("description");
            int maxNumber = Integer.parseInt(set.getString("max_user_number"));
            String status = set.getString("status");
            Date date = Date.valueOf(set.getString("creation_date"));
            PublicChat chat = new PublicChat(id, name, description, maxNumber, date);
            topChatsSet.add(chat);
        }
        return topChatsSet;
    }
}

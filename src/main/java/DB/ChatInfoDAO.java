package DB;

import Classes.Chat;
import Classes.PrivateChat;
import Classes.PublicChat;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatInfoDAO {
    private Connection connection;
    public static final String ATTRIBUTE = "chatInfo";
    public static final String PUBLIC = "Public";
    public static final String PRIVATE = "Private";
    public static final Integer DEFAULT_LIMIT = 100;
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
        try (PreparedStatement statement = connection.prepareStatement("insert into " + DBInfo.CHAT_TABLE
                + " (name, status, description, max_users_number, creation_date) value "
                + "(?, ?, ?, ?, now(4))", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, chatName);
            statement.setString(2, status);
            statement.setString(3, description);
            statement.setInt(4, maxUsersAllowed);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException("Creating the room did not return an id");
                }
                return keys.getLong(1);
            }
        }
    }

    /**
     * gets top n public chat by number of members
     *
     * @param n number of top chats
     * */
    public List<Chat> getTopNChats(int n) throws SQLException {
        List<Chat> topChatsSet = new ArrayList<Chat>();
        PreparedStatement statement = connection.prepareStatement("select * from " + DBInfo.CHAT_TABLE
                                + " t where lower(t.status) = \"public\" "
                                + "order by coalesce((select max(m.creation_date) from "
                                + DBInfo.MESSAGE_TABLE + " m where m.chatid = t.id), "
                                + "t.creation_date) desc limit ?;");
        statement.setInt(1, n);
        ResultSet set = statement.executeQuery();
        while (set.next()){
            String name = set.getString("name");
            long id = Long.parseLong(set.getString("id"));
            String description = set.getString("description");
            int maxNumber = Integer.parseInt(set.getString("max_users_number"));
            String status = set.getString("status");
            String date = set.getString("creation_date");
            PublicChat chat = new PublicChat(id, name, description, maxNumber, date);
            topChatsSet.add(chat);
        }
        statement.close();
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
        PreparedStatement statement = connection.prepareStatement("select username from " + DBInfo.USERS_TABLE
                                + " where chatid = ?;");
        statement.setLong(1, chatID);

        ResultSet set = statement.executeQuery();
        while (set.next()){
            userNames.add(set.getString(1));
        }
        statement.close();
        return userNames;
    }

    /**
     * returns chat information by id
     *
     * @param chatId id of chat
     * @return Chat info of chat
     * */
    public Chat getChatInfo(long chatId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select * from " + DBInfo.CHAT_TABLE + " where id = ? ");
        statement.setLong(1, chatId);
        Chat chat = null;
        ResultSet set = statement.executeQuery();
        if (!set.next()) {
            statement.close();
            return null;
        }
        String status = set.getString("status");
        if (status.equalsIgnoreCase(ChatInfoDAO.PRIVATE)){
            chat = new PrivateChat(chatId, set.getString("name"), set.getString("description"),
                        set.getInt("max_users_number"), set.getString("creation_date"));
        } else {
            chat = new PublicChat(chatId, set.getString("name"), set.getString("description"),
                    set.getInt("max_users_number"), set.getString("creation_date"));
        }
        statement.close();
        return chat;
    }

}

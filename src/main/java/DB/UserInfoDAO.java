package DB;

import Classes.User;

import java.sql.*;

public class UserInfoDAO {
    private Connection con;
    public static final String ATTRIBUTE = "userInfo";

    public UserInfoDAO(Connection con){
        this.con = con;
    }

    /**
     * adds new user in database with name and id provided
     *
     * @param chatID id of chat where user to be added
     * @param name name of user to add
     * @return id of added user
     * @throws SQLException throws exception if occur any error
     * */
    public long addUser(long chatID, String name) throws SQLException {
        try (PreparedStatement statement = con.prepareStatement("insert into " + DBInfo.USERS_TABLE
                + " (chatid, username, creation_date) value "
                + "(?, ?, now(4))", Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, chatID);
            statement.setString(2, name);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException("Creating the guest did not return an id");
                }
                return keys.getLong(1);
            }
        }
    }

    /**
     * gets back last inserted user
     *
     * @return last inserted user
     * */
    public User getLastUser() throws SQLException {
        try (PreparedStatement statement = con.prepareStatement(
                "select id, chatid, username from " + DBInfo.USERS_TABLE
                        + " order by creation_date desc limit 1");
             ResultSet result = statement.executeQuery()) {
            if (!result.next()) {
                return null;
            }
            return new User(
                    result.getLong("id"),
                    result.getString("username"),
                    result.getLong("chatid"));
        }
    }

    /**
     * gets user by it's id
     * @param userId id of user
     * @return user
     * */
    public User getUser(long userId) throws SQLException {
        try (PreparedStatement statement = con.prepareStatement(
                "select id, chatid, username from " + DBInfo.USERS_TABLE + " where id = ?")) {
            statement.setLong(1, userId);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return null;
                }
                return new User(
                        result.getLong("id"),
                        result.getString("username"),
                        result.getLong("chatid"));
            }
        }
    }
}

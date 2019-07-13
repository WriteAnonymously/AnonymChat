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
        PreparedStatement statement = con.prepareStatement("insert into " + DBInfo.USERS_TABLE
                        + " (chatid, username, creation_date) value "
                        + "(?, ?, now(4));");
        statement.setLong(1, chatID);
        statement.setString(2, name);
        System.out.println(statement.toString());
        statement.executeUpdate();
        Statement st = con.createStatement();
        String q = "select LAST_INSERT_ID();";
        ResultSet set = st.executeQuery(q);
        set.last();
        return Long.parseLong(set.getString(1));
    }

    /**
     * gets back last inserted user
     *
     * @return last inserted user
     * */
    public User getLastUser() throws SQLException {
        User user = null;
        PreparedStatement statement = con.prepareStatement("select * from " + DBInfo.USERS_TABLE + " order by creation_date desc limit 1;");
        ResultSet ansSet = statement.executeQuery();
        ansSet.next();
        user = new User(Long.parseLong(ansSet.getString("id")), ansSet.getString("username"), Long.parseLong(ansSet.getString("chatid")));
        return user;
    }
}

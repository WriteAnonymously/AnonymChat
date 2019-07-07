package DB;

import java.sql.*;

public class UserInfoDAO {
    private Connection con;

    public UserInfoDAO(Connection con){
        this.con = con;
    }

    /**
     * adds new user in database with name and id provided
     *
     * @param chatID id of chat where user to be added
     * @param name name of user to add
     * @throws SQLException throws exception if occur any error
     * */
    public long addUser(long chatID, String name) throws SQLException {
        PreparedStatement statement = con.prepareStatement("insert into " + DBInfo.USERS_TABLE
                        + " (chatid, username, creation_date) value "
                        + "(?, ?, sysdate());");
        statement.setLong(1, chatID);
        statement.setString(2, name);
        System.out.println(statement.toString());
        statement.executeUpdate();
        try {
            Statement st = con.createStatement();
            String q = "select LAST_INSERT_ID();";
            ResultSet set = st.executeQuery(q);
            set.last();
            return Long.parseLong(set.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}

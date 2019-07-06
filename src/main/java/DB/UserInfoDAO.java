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
     * @param userID id of user to add
     * @param name name of user to add
     * @throws SQLException throws exception if occur any error
     * */
<<<<<<< HEAD
    public long addUser(String userID, String name) throws SQLException {
        PreparedStatement statement = con.prepareStatement("insert into " + DBconnector.USERS_TABLE
=======
    public void addUser(String userID, String name) throws SQLException {
        PreparedStatement statement = con.prepareStatement("insert into " + DBInfo.USERS_TABLE
>>>>>>> 15189bd7c69c990fa9a197c538c524335753311f
                        + " (id, name, creation_date) value "
                        + "(?, ?, sysdate());");
        statement.setString(1, userID);
        statement.setString(2, name);
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

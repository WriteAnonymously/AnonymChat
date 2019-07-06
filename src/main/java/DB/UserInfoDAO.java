package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    public void addUser(String userID, String name) throws SQLException {
        PreparedStatement statement = con.prepareStatement("insert into " + DBInfo.USERS_TABLE
                        + " (id, name, creation_date) value "
                        + "(?, ?, sysdate());");
        statement.setString(1, userID);
        statement.setString(2, name);
        statement.executeUpdate();
    }
}

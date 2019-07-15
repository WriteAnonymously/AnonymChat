package DB;

import Classes.User;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UsernameDAO {
    private Connection con;
    public static final String ATTRIBUTE = "usernameInfoDAO";

    public UsernameDAO(Connection con){
        this.con = con;
    }

    /**
     * adds new username in database
     *
     * @param name username to add
     * @throws SQLException throws exception if occur any error
     * */
    public void addUsername(String name) throws SQLException {
        PreparedStatement statement = con.prepareStatement("insert into " + DBInfo.USERNAMES_TABLE
                + " (username) value "
                + "(?);");
        statement.setString(1, name);
        statement.executeUpdate();
        statement.close();
    }
    /**
     * gets last username from table
     *
     * @throws SQLException throws exception if occur any error
     * @return last username
     * */
    public String getLastUsername() throws SQLException {
        String result = "";
        PreparedStatement statement = con.prepareStatement("select username from " + DBInfo.USERNAMES_TABLE
                + " order by id limit 1;");
        ResultSet set = statement.executeQuery();
        if(set.next()) {
            result = set.getString("username");
        }
        statement.close();
        return result;
    }

    /**
     * gets n random usernames
     *
     * @param n number of usernames
     * @return Set of usernames
     * */
    public Set<String> getNUsernames(int n) throws SQLException {
        Set<String> usernames = new HashSet<String>();
        PreparedStatement statement = con.prepareStatement("select username from " + DBInfo.USERNAMES_TABLE
                + " order by RAND() limit ?;");
        statement.setInt(1, n);
        ResultSet set = statement.executeQuery();
        while (set.next()){
            String name = set.getString("username");
            usernames.add(name);
        }
        statement.close();
        return usernames;
    }
}

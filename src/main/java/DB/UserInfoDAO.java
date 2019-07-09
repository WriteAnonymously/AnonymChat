package DB;

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
                        + "(?, ?, sysdate());");
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
     * finds the number of users in chat and maximal number of users allowed
     *
     * @return Pair of the values where
     * */
    public Pair getCurrAndMax(long chatID) throws  SQLException{
        Statement getAllowed = con.createStatement();
        String allowed = "Select max_users_number from " + DBInfo.CHAT_TABLE + " where id = " + chatID;
        ResultSet st = getAllowed.executeQuery(allowed);
        int maxNumber = st.getInt("max_users_number");
        Statement cntUsers = con.createStatement();
        String countUsers = "Select COUNT(id) as cnt from " + DBInfo.USERS_TABLE + " where chatid = " + chatID;
        ResultSet stcnt = cntUsers.executeQuery(countUsers);
        int currNumber = st.getInt("cnt");
        Pair pair = new Pair(currNumber, maxNumber);
        return pair;
    }


    /* the class for storing maximal and current number of chats */
    public class Pair {
        private int current, maximal;
        public Pair(int current, int maximal){
            this.current = current;
            this.maximal = maximal;
        }
        public int  getCurrent(){
            return current;
        }
        public int getMaximal(){
            return maximal;
        }
    }
}

package DB;

import java.sql.*;

public class UserInfoDAO {
    private Connection con;
    public static final String ATTRIBUTE = "userInfo";
    public UserInfoDAO(Connection con){
        this.con = con;
    }

    private class Pair{
        private int key,value;
        public Pair(int key, int value){
            this.key = key;
            this.value = value;
        }
        public int  getKey(){
            return key;
        }
        public int getValue(){
            return value;
        }
        public void setKey(int nKey){
            key = nKey;
        }
        public void setValue(int nValue ){
            value = nValue;
        }
    }

    /**
     * adds new user in database with name and id provided
     *
     * @param chatID id of chat where user to be added
     * @param name name of user to add
     * @throws SQLException throws exception if occur any error
     * */

    //აქ იმას ჩავამატებ, რომ ჩატის წევრების მაქსიმალურ რაოდენობას არ გადასცდეს - ნიკოლოზ ჭურღულია
    // error -1 - რაღაც ვერ ჩაემატა საკაიფოდ
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

    /**
     * generates a random username
     *
     * @param chatID id of chat where user to be added
     * @throws SQLException throws exception if occur any error
     * */
    public String generateName(long chatID) throws SQLException{
        /*
            gvchirdeba username-ebis table.
            randomad airchevs usernames am tabledan da mere amowmebs mocemul chatshi aris tu ara es username dakavebuli
            tu ar aris, daabrunebs. tu arada axlidan cdis.
         */
        return "wvera";
    }

    public Pair getCurrAndMax(long chatID) throws  SQLException{
        Statement getAllowed = con.createStatement();
        String allowed = "Select max_users_number from chats where id = " + chatID;
        ResultSet st = getAllowed.executeQuery(allowed);
        int maxNumber = st.getInt("max_users_number");
        Statement cntUsers = con.createStatement();
        String countUsers = "Select COUNT(id) as cnt from users where chatid = " + chatID;
        ResultSet stcnt = cntUsers.executeQuery(countUsers);
        int currNumber = st.getInt("cnt");
        Pair pair = new Pair(currNumber,maxNumber);
        return pair;
    }

}

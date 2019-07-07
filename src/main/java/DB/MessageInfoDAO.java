package DB;

import Classes.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageInfoDAO {
    Connection con;
    public MessageInfoDAO(Connection con){
        this.con = con;
    }


    public void addMessage(long userID, long chatID, String content) throws SQLException{
        PreparedStatement statement = con.prepareStatement("insert into " + DBInfo.CHAT_TABLE
                + " (chatid, userid, content, creation_date) value "
                + "(?, ?, ? , sysdate());");

            statement.setLong(1, chatID);

            statement.setLong(2, userID);

            statement.setString(3, content);

            statement.executeUpdate();



    }

    public List<Message> getLastNMessages(int n, long chatID) throws SQLException {
        List<Message> msgs = new ArrayList<Message>();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT TOP " + n + " userid, content, creation_date FROM "
                                + DBInfo.CHAT_TABLE + " WHERE id = " + chatID + " ORDER BY creation_date DESC");
        while (rs.next()) {
            long userID = Long.parseLong(rs.getString("userid"));
            String content = rs.getString("content");
            Date date = Date.valueOf(rs.getString("creation_date"));
            Message curr = new Message(chatID, userID, content, date);
            msgs.add(curr);

        }
        return msgs;
    }

}

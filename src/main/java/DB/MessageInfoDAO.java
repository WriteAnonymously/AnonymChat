package DB;

import Classes.Message;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageInfoDAO {
    Connection con;
    public MessageInfoDAO(Connection con){
        this.con = con;
    }

    public void addMessage(int chatID, String content, int userID) throws SQLException{
        PreparedStatement statement = con.prepareStatement("insert into " + DBconnector.CHAT_TABLE
                + " (chatID, userID, content ,date) value "
                + "(?, ?, ? , sysdate());");

            statement.setInt(1, chatID);

            statement.setInt(2, userID);

            statement.setString(3, content);

            statement.executeUpdate();



    }

    public List<Message> getLastNMessages(int n, long chatID) throws SQLException {
        List<Message> msgs = new ArrayList<Message>();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT TOP " + n + " ID,USER_ID,CONTENT,DT FROM CHATS WHERE ID = " + chatID + " ORDER BY DT DESC");
        while (rs.next()) {
            Message curr = new Message(rs.getString(1), Long.toString(chatID), rs.getString(2), rs.getString(3), rs.getString(4));
            msgs.add(curr);

        }
        return msgs;
    }

}

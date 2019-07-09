package DB;

import Classes.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageInfoDAO {
    private Connection con;
    public static final String ATTRIBUTE = "messageInfo";

    public MessageInfoDAO(Connection con){
        this.con = con;
    }

    /**
     * adds new message in database with message Object
     *
     * @param message object
     * */
    public void addMessage(Message message) throws SQLException {
        addMessage(message.getUserId(), message.getChatId(), message.getContent());
    }

    /**
     * adds new message in database with info provided
     *
     * @param userID id of user message is of
     * @param chatID chat id the message is written in
     * @param content content of message
     * */
    public void addMessage(long userID, long chatID, String content) throws SQLException{
        PreparedStatement statement = con.prepareStatement("insert into " + DBInfo.MESSAGE_TABLE
                + " (chatid, userid, content, creation_date) value "
                + "(?, ?, ? , now(4));");

            statement.setLong(1, chatID);
            statement.setLong(2, userID);
            statement.setString(3, content);
            System.out.println(userID + "  " + chatID + "  " + statement);
            statement.executeUpdate();
            statement.close();
    }

    /**
     * returns the list of last n inserted messages
     *
     * @param n the number of messages is wanted
     * @param chatID id of chat where messages is to be searched in
     * @return List<Message> list of the messages
     * */
    public List<Message> getLastNMessages(int n, long chatID) throws SQLException {
        List<Message> msgs = new ArrayList<Message>();
        Statement st = con.createStatement();
        String s = "SELECT userid, content, creation_date FROM "
                + DBInfo.MESSAGE_TABLE + " WHERE chatid = " + chatID + " ORDER BY creation_date LIMIT " + n + ";";
        System.out.println(s);
        ResultSet rs = st.executeQuery(s);
        while (rs.next()) {
            long userID = Long.parseLong(rs.getString("userid"));
            String content = rs.getString("content");
            System.out.println(rs.getString("creation_date"));
            String date = rs.getString("creation_date");
            Message curr = new Message(chatID, userID, content, date);
            msgs.add(curr);
        }
        st.close();
        return msgs;
    }

}

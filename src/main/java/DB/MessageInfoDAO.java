package DB;

import Classes.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
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
        List<Message> messages = new ArrayList<Message>();
        Statement st = con.createStatement();
        String s = "select m.userid as userid, m.content as content, m.creation_date as creation_date, u.username as username " +
                "from " + DBInfo.MESSAGE_TABLE + " m " + "left join " + DBInfo.USERS_TABLE + " u " + "on u.id = m.userid " +
                "where m.chatid = " + chatID + " order by m.creation_date desc limit " + n + ";";
        ResultSet rs = st.executeQuery(s);
        while (rs.next()) {
            long userID = rs.getLong("userid");
            String userName = rs.getString("username");
            String content = rs.getString("content");
            String date = rs.getString("creation_date");
            date = clearDate(date);
            Message curr = new Message(chatID, userID, userName, content, date);
            messages.add(curr);
        }
        Collections.reverse(messages);
        st.close();
        return messages;
    }

    /**
     * clears the date with microsecond elements
     *
     * @param date date to be cleared
     * @return cleared date
     * */
    private String clearDate(String date) {
        int pos = date.length() - 1;
        while (date.charAt(pos) != '.'){
            pos--;
        }
        return date.substring(0, pos);
    }

}

package DB;

import Classes.Guess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GuessDAO {

    private Connection connection;

    public GuessDAO(Connection connection){
        this.connection = connection;
    }

    /**
     * adds guess for chat provided
     *
     * @param chatId id of chat to add guess to
     * @param word itself
     * */
    public void addGuess(long chatId, String word) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("insert into " + DBInfo.GUESS_TABLE + " (chatid, word) value (?, ?, ?);");
        statement.setLong(1, chatId);
        statement.setString(2, word);
        statement.executeUpdate();
        statement.close();
    }

    public void updateGuess(long chatId, String word) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("update " + DBInfo.GUESS_TABLE + " set word = ? " +
                                " where chatid = ?;");
        statement.setLong(1, chatId);
        statement.setString(2, word);
        statement.executeUpdate();
        statement.close();
    }

    public Guess getGuessForChat(long chatId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select chatid, word from " + DBInfo.GUESS_TABLE + " where chatid = ?;");
        statement.setLong(1, chatId);
        ResultSet set = statement.executeQuery();
        if (!set.next()) {
            Guess ans = new Guess(set.getString("word"), set.getLong("chatid"));
            statement.close();
            return ans;
        }
        return null;
    }
}

package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class RandomIdentificatorsDAO {
    private Connection connection;

    public RandomIdentificatorsDAO(Connection con){
        connection = con;
    }

    /**
     * adds random id for chatId in DB not used table
     *
     * @param randomString string
     * @param chatId id of chat
     * */
    public void addNotUsedRandomIdentificator(String randomString, long chatId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("insert into " + DBInfo.NOT_USED_RANDOM_IDENTIFICATORS  +
                                " (id, chatid) values (?, ?);");
        statement.setString(1, randomString);
        statement.setLong(2, chatId);
        statement.executeUpdate();
        statement.close();
    }

    /**
     * removes random id from DB not used table
     *
     * @param randomString string
     * */
    public void removeNotUsedRandomIdentificator(String randomString, long chatId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("delete from ? where id = ?;");
        statement.setString(1, DBInfo.NOT_USED_RANDOM_IDENTIFICATORS);
        statement.setString(2, randomString);
        statement.executeUpdate();
        statement.close();
    }

    /**
     * adds random id for chatId in DB used table
     *
     * @param randomString string
     * @param chatId id of chat
     * */
    public void addUsedRandomIdentificator(String randomString, long chatId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("insert into " + DBInfo.USED_RANDOM_IDENTIFICATORS  +
                                    " (id, chatid) values (?, ?);");
        statement.setString(1, randomString);
        statement.setLong(2, chatId);
        statement.executeUpdate();
        statement.close();
    }

    /**
     * gets chat id from DB for not used table
     *
     * @param randomString string
     * @return id of chat where the random string is
     * */
    public long getChatIdForNotUsedIdentificator(String randomString) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select chatid from " + DBInfo.NOT_USED_RANDOM_IDENTIFICATORS + " where id = ?;");
        statement.setString(1, randomString);
        ResultSet set = statement.executeQuery();
        statement.close();
        set.last();
        long chatId = set.getLong("chatid");
        statement.close();
        return chatId;
    }

    /**
     * gets chat id from DB for used table
     *
     * @param randomString string
     * @return id of chat where the random string is
     * */
    public long getChatIdForUsedIdentificator(String randomString) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select chatid from " + DBInfo.USED_RANDOM_IDENTIFICATORS + " where id = ?;");
        statement.setString(1, randomString);
        ResultSet set = statement.executeQuery();
        statement.close();
        set.last();
        long chatId = set.getLong("chatid");
        statement.close();
        return chatId;
    }

    /**
     * gets back set of used random strings
     *
     * @param chatId id of chat the set wanted for
     * @return Set of used random strings
     * */
    public Set <String> getRandomIdentificatorsSet(long chatId) throws SQLException {
        Set<String> ansSet = new HashSet<String>();
        PreparedStatement statement = connection.prepareStatement("select id from " + DBInfo.USED_RANDOM_IDENTIFICATORS + " where chatid = ? union " +
                            "select id from " + DBInfo.NOT_USED_RANDOM_IDENTIFICATORS + " where chatid = ?;");
        statement.setLong(1, chatId);
        statement.setLong(2, chatId);
        ResultSet set = statement.executeQuery();
        while (set.next()){
            ansSet.add(set.getString("id"));
        }
        statement.close();
        return ansSet;
    }
}

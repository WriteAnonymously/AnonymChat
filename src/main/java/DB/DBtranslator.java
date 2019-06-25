package db;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBtranslator {
    private static final String MESSAGE_TABLE = "messages";
    private static final String CHAT_TABLE = "chats";
    private static final String USER_TABLE = "users";
    private static final String TAG_TABLE = "tags";

    protected Connection con = null;

    /**
     * Constructor, con is initialized
     */
    public DBtranslator(){
        con = getConnect();
    }

    /**
     * Connects to DataBase and returns Connection object
     */
    private static Connection getConnect(){
        Connection con = null;
        try{
            Class.forName(DBconnector.DRIVER);
            con = DriverManager.getConnection("jdbc:mysql://"+DBconnector.MYSQL_DATABASE_SERVER, DBconnector.MYSQL_USERNAME, DBconnector.MYSQL_PASSWORD);
        } catch (Exception e){System.out.println(e);}
        return con;
    }


    public Connection getConnection(){
        return con;
    }

    /**
     * Closes connection
     */
    public void closeConnection(){
        try{
            con.close();
        } catch (Exception e){}
    }

    /**
     * Method for updating database.
     * String update Usage: "Insert into table values ()..."
     */
    private  int updateDB(String update) throws SQLException {
        Statement statement = con.createStatement();
        int result = statement.executeUpdate(update);
        return result;
    }

    /**
     * Method for getting information from database
     * Returns ResultSet object;
     * String query usage: "select from table..."
     */
    private ResultSet getFromDB(String query) throws SQLException{
        Statement statement = con.createStatement();
        ResultSet result = statement.executeQuery(query);
        return result;
    }

    /**
     * Method that returns String for inserting a new user
     */
    private String createUserQuery(String userID, String  chatID, String username){
        String query = "INSERT INTO " + USER_TABLE +
                "(id, chatID, username) " +
                "VALUES (" + userID + ", " +
                chatID + ", " +
                withQuotations(username);

        query += ")";
        return query;
    }

    /**
     * General method for creating getter query,
     *  I don't believe it will work.
     */
    private String createGetQuery(String indicator, String myIndicator, String table){
        String query = "SELECT * FROM "+table + " "+
                "WHERE "+ indicator + " = "+ myIndicator;
        return query;
    }


    /**
     * Method that returns String for inserting a new message
     */
    private String createMessageQuery(String chatID, String userID,
                                      String text, Date creationDate) {
        String query = "INSERT INTO " + MESSAGE_TABLE +
                "(chatID, userID, content, date) " +
                "VALUES (" + chatID + ", " +
                userID + ", " +
                withQuotations(text) + ", " +
                withQuotations(dateToString(creationDate));

        query += ")";
        return query;
    }


    /**
     * Method that returns String for inserting a new chat
     */
    private String createChatQuery(String chatID, String chatName, String description,
                                   int visible, int numMembers, Date creationDate){
        String query = "INSERT INTO " + CHAT_TABLE +
                "(id, name, description, visibility) " +
                "VALUES (" + chatID + ", " +
                withQuotations(chatName) + ", " +
                withQuotations(description) + ", " +
                visible + ", " +
                numMembers + ", " +
                withQuotations(dateToString(creationDate));

        query += ")";
        return query;
    }



    /**
     * Method that returns String for inserting a new tag
     */
    private String createTagQuery(String id, String word, String chatID){
        String query = "INSERT INTO " + TAG_TABLE +
                "(id, word, chatID) " +
                "VALUES (" + id + ", " + withQuotations(word) + ", " +
                chatID;

        query += ")";
        return query;
    }

    private String dateToString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("MM/DD/YYYY");
        return formatter.format(date);
    }

    /**
     * Decoretes string with quotations
     */
    private String withQuotations(String word){
        return "\'" + word + "\'";
    }

    /*
    * Inserts into sql database a new user
    */
    public void insertUserDB(String userID, String  chatID, String username) throws  SQLException{
        String query = createUserQuery(userID, chatID, username);
        updateDB(query);
    }

    /*
    * Insert into sql database a new message
    */
    public void insertMessageDB(String chatID, String userID,
                                String text, Date creationDate)throws  SQLException{
        String query = createMessageQuery(chatID, userID, text, creationDate);
        updateDB(query);
    }

    /*
     * Inserts into sql database a new chat
     */
    public void insertChatDB(String chatID, String chatName, String description,
                             int visible, int numMembers, Date creationDate) throws SQLException{
        String query = createChatQuery(chatID, chatName, description, visible, numMembers, creationDate);
        updateDB(query);
    }

    /*
     * Insert into sql database a new tag
     */
    public void insertTagDB(String id, String word, String chatID) throws SQLException{
        String query = createTagQuery(id, word, chatID);
        updateDB(query);
    }


}

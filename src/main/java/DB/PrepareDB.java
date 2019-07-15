package DB;

import Classes.Chat;
import Classes.Message;
import org.apache.commons.dbcp.BasicDataSource;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrepareDB {
    private static Connection connection = null;

    /**
     * გასატესტად თუ მონაცემები სწორია ბაზის გამოგადგებათ
     * */
    public static void main(String[] args){
        try {
            Connection con = PrepareDB.getInstance();
            Statement statement = connection.createStatement();
            statement.executeUpdate("use anonym_chat_schema;");
            addUserNames(con);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void addUserNames(Connection con) throws SQLException {
        UsernameDAO usernameDAO = new UsernameDAO(con);
        List<String> names = new ArrayList<String>(Arrays.asList("saba", "vaxo", "vasila", "dachvi", "churgula"));
        for (int i = 0; i < names.size(); i++){
            usernameDAO.addUsername(names.get(i));
        }
    }


    /**
     * gets creates Connection instance and returns
     *
     * @return Connection to database
     * */
    public static Connection getConnect() throws ClassNotFoundException, SQLException {
        if (connection == null)
            Class.forName(DB.DBInfo.DRIVER);
        connection = DriverManager.getConnection("jdbc:mysql://"+ DB.DBInfo.MYSQL_DATABASE_SERVER, DB.DBInfo.MYSQL_USERNAME, DB.DBInfo.MYSQL_PASSWORD);
//        Statement statement = connection.createStatement();
//        statement.executeUpdate("use anonym_chat_schema;");
//        prepareStructure();
//        BasicDataSource pool = new BasicDataSource();
        return connection;
    }


    /**
     * gets instance of Connection
     *
     * @throws SQLException if database parameters not found or some other database error
     * @throws ClassNotFoundException throws if driver class not found
     * */
    public static Connection getInstance() throws SQLException, ClassNotFoundException {
        if (connection == null){
            synchronized (PrepareDB.class){
                if (connection == null){
                    getConnect();
                }
            }
        }
        return connection;
    }
}
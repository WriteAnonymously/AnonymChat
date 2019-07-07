package DB;

import Classes.Chat;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PrepareDB {
    private static Connection connection = null;

    /**
     * გასატესტად თუ მონაცემები სწორია ბაზის გამოგადგებათ
     * */
    public static void main(String[] args){
        try {
            Connection con = PrepareDB.getInstance();
            MessageInfoDAO messageInfoDAO = new MessageInfoDAO(con);
            UserInfoDAO userInfoDAO = new UserInfoDAO(con);
            ChatInfoDAO chatInfoDAO = new ChatInfoDAO(con);
            long charID = chatInfoDAO.addChat("chat", "public", "kai chat",  1);
            long userID = userInfoDAO.addUser(1, "wvera");
            List<Chat> lst = chatInfoDAO.getTopNChats(1);
            Chat ch = lst.get(0);
            System.out.println("Chat ID: " + ch.getID());
            System.out.println("User ID: " + userID);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * gets creates Connection instance and returns
     *
     * @return Connection to database
     * */
    private static Connection getConnect() throws ClassNotFoundException, SQLException {
        Class.forName(DB.DBInfo.DRIVER);
        connection = DriverManager.getConnection("jdbc:mysql://"+ DB.DBInfo.MYSQL_DATABASE_SERVER, DB.DBInfo.MYSQL_USERNAME, DB.DBInfo.MYSQL_PASSWORD);
        Statement statement = connection.createStatement();
        statement.executeUpdate("use anonym_chat_schema;");
//        prepareStructure();
        return connection;
    }

    /**
     * prepare database for use including drop old tables and create new ones
     * */
    private static void prepareStructure() {
        BufferedReader reader;
        try {
            System.out.println(System.getProperty("user.dir"));
            reader = new BufferedReader(new FileReader("src/main/java/DB/db.sql"));
            String q = "";
            while (true){
                String line = reader.readLine();
                q += " " + (line != null ?line:"");
                if (line != null && !line.equals("") && line.indexOf(";") != -1){
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(q);
                    q = "";
                }
                if (line == null){
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

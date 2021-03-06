package DB;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * the class is for storing an Connections to Database and getting and putting it
 * */

public class ConnectionPool extends MysqlDataSource {
    public static final String ATTRIBUTE = "connectionPool";
    private static final int CONNECTION_NUMBER = 20;
    private ArrayList <Connection> freeConnections, busyConnections;
    private Semaphore connectionLock, lock;

    private static ConnectionPool connectionPool = new ConnectionPool();


    public static void main(String[] args){
        try {
            Connection con = connectionPool.getConnection();
            Statement statement = con.createStatement();
            ResultSet set = statement.executeQuery("select * from " + DBInfo.CHAT_TABLE);
            while (set.next()){
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ConnectionPool getInstance(){
        return connectionPool;
    }

    private ConnectionPool(){
        setUser(DBInfo.MYSQL_USERNAME);
        setPassword(DBInfo.MYSQL_PASSWORD);
        setURL(DBInfo.MYSQL_DATABASE_URL);
    }
}

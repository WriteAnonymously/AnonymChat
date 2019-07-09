package DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * the class is for storing an Connections to Database and getting and putting it
 * */

public class ConnectionPool {
    public static final String ATTRIBUTE = "connectionPool";
    private static final int CONNECTION_NUMBER = 20;
    private ArrayList <Connection> freeConnections, busyConnections;
    private Semaphore connectionLock, lock;

    private static ConnectionPool connectionPool = new ConnectionPool();


    public static void main(String[] args){
        try {
            Connection con = null;
            con.close();
            for (int i=0; i<CONNECTION_NUMBER + 1; ++i)
                con = connectionPool.getConnection();
            Statement statement = con.createStatement();
            ResultSet set = statement.executeQuery("select * from " + DBInfo.CHAT_TABLE);
            while (set.next()){
                System.out.println(set.getString(1));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ConnectionPool getInstance(){
        return connectionPool;
    }

    private ConnectionPool(){
//        connectionPool.setUser(DBInfo.MYSQL_USERNAME);
//        connectionPool.setPassword(DBInfo.MYSQL_PASSWORD);
//        connectionPool.setURL(DBInfo.MYSQL_DATABASE_URL);
        freeConnections = new ArrayList<Connection>();
        busyConnections = new ArrayList<Connection>();
        connectionLock = new Semaphore(CONNECTION_NUMBER);
        lock = new Semaphore(1);
        for (int i=0; i<=CONNECTION_NUMBER; ++i){
            try {
                freeConnections.add(PrepareDB.getConnect());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() throws InterruptedException {
        connectionLock.acquire();
        lock.acquire();
        Connection con = freeConnections.get(freeConnections.size() - 1);
        freeConnections.remove(con);
        busyConnections.add(con);
        lock.release();
        return con;
    }

    public void putConnection(Connection con) throws InterruptedException {
        lock.acquire();
        if (!busyConnections.contains(con) || freeConnections.contains(con)){
            new RuntimeException("Connection put with not been taken");
        }
        busyConnections.remove(con);
        freeConnections.add(con);
        lock.release();
        connectionLock.release();
    }

    public void close() throws InterruptedException, SQLException {
        lock.acquire();
        for (int i=0; i<freeConnections.size(); ++i){
            freeConnections.get(i).close();
        }
        for (int i=0; i<busyConnections.size(); ++i){
            busyConnections.get(i).close();
        }
        lock.release();
    }
}

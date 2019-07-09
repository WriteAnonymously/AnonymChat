package DB;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class ConnectionPool extends MysqlDataSource {
    public static final String ATTRIBUTE = "connectionPool";

    private static ConnectionPool connectionPool = new ConnectionPool();

    public static ConnectionPool getInstance(){
        return connectionPool;
    }

    private ConnectionPool(){
        connectionPool.setUser(DBInfo.MYSQL_USERNAME);
        connectionPool.setPassword(DBInfo.MYSQL_PASSWORD);
        connectionPool.setURL(DBInfo.MYSQL_DATABASE_URL);
    }
}

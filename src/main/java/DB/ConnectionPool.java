package DB;

import org.apache.commons.dbcp2.BasicDataSource;

import java.time.Duration;

/**
 * Shared, bounded JDBC connection pool.
 */
public class ConnectionPool extends BasicDataSource {
    public static final String ATTRIBUTE = "connectionPool";
    private static final int CONNECTION_NUMBER = 20;
    private static final ConnectionPool INSTANCE = new ConnectionPool();

    public static ConnectionPool getInstance(){
        return INSTANCE;
    }

    protected ConnectionPool(){
        setDriverClassName(DBInfo.DRIVER);
        setUsername(DBInfo.MYSQL_USERNAME);
        setPassword(DBInfo.MYSQL_PASSWORD);
        setUrl(DBInfo.MYSQL_DATABASE_URL);
        setInitialSize(0);
        setMinIdle(1);
        setMaxIdle(8);
        setMaxTotal(CONNECTION_NUMBER);
        setMaxWait(Duration.ofSeconds(5));
        setValidationQuery("SELECT 1");
        setTestOnBorrow(true);
    }
}

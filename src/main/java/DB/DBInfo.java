package DB;

// This Should be used after establishing database server
// Now I use it for local connections, taken directly from OOP assignment
public class DBInfo {
    public static final String USERS_TABLE = "users";
    public static final String CHAT_TABLE = "chats";
    public static final String MESSAGE_TABLE = "messages";
    public static final String MEMBER_TABLE = "messages";

    public static final String MYSQL_USERNAME = "";
    public static final String MYSQL_PASSWORD = "";
    public static final String MYSQL_DATABASE_SERVER = "localhost:3306/";
    public static final String MYSQL_DATABASE_NAME = "";
    public static final String DRIVER = "com.mysql.jdbc.Driver";
}

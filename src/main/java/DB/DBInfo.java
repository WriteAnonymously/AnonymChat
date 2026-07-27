package DB;

/**
 * Runtime configuration. Secrets are read from the environment and are never
 * stored in the repository.
 */
public final class DBInfo {
    public static final String USERS_TABLE = "users";
    public static final String CHAT_TABLE = "chats";
    public static final String USERNAMES_TABLE = "usernames";
    public static final String MESSAGE_TABLE = "messages";
    public static final String TAG_TABLE = "tags";
    public static final String USED_RANDOM_IDENTIFICATORS = "used_random_identificators";
    public static final String NOT_USED_RANDOM_IDENTIFICATORS = "not_used_random_identificators";
    public static final String GUESS_TABLE = "guesses";

    public static final String MYSQL_DATABASE_URL = env(
            "DB_URL",
            "jdbc:mysql://localhost:3306/anonym_chat_schema"
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
    public static final String MYSQL_USERNAME = env("DB_USER", "anonymchat");
    public static final String MYSQL_PASSWORD = env("DB_PASSWORD", "anonymchat");
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static final String APP_BASE_URL = env("APP_BASE_URL", "http://localhost:8080");
    public static final String ALLOWED_ORIGIN = env("ALLOWED_ORIGIN", APP_BASE_URL);

    public static final String SMTP_HOST = env("SMTP_HOST", "localhost");
    public static final int SMTP_PORT = envInt("SMTP_PORT", 1025);
    public static final String SMTP_USERNAME = env("SMTP_USERNAME", "");
    public static final String SMTP_PASSWORD = env("SMTP_PASSWORD", "");
    public static final String SMTP_FROM = env("SMTP_FROM", "hello@anonymchat.local");
    public static final boolean SMTP_STARTTLS = envBoolean("SMTP_STARTTLS", false);
    public static final String CONTACT_EMAIL = env("CONTACT_EMAIL", "hello@anonymchat.local");

    private DBInfo() {
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value.trim();
    }

    private static int envInt(String name, int fallback) {
        try {
            return Integer.parseInt(env(name, Integer.toString(fallback)));
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    private static boolean envBoolean(String name, boolean fallback) {
        String value = System.getenv(name);
        return value == null ? fallback : Boolean.parseBoolean(value);
    }
}

package Classes;

import java.util.List;

/**
 * Public room data exposed to the lobby. Database identifiers are used only as
 * navigation keys; the only displayed count is the current live socket count.
 */
public class RoomSummary {
    private final long id;
    private final String name;
    private final String description;
    private final String creationDate;
    private final int activeUsers;
    private final int capacity;
    private final List<String> tags;

    public RoomSummary(
            Chat chat,
            int activeUsers,
            List<String> tags) {
        this.id = chat.getID();
        this.name = chat.getName();
        this.description = chat.getDescription();
        this.creationDate = chat.getCreationTime();
        this.activeUsers = activeUsers;
        this.capacity = chat.getLimit();
        this.tags = tags;
    }
}

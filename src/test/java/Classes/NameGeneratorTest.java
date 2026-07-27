package Classes;

import DB.ChatInfoDAO;
import DB.UsernameDAO;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NameGeneratorTest {
    @Test
    public void choosesAnUnusedTemporaryName() throws Exception {
        ChatInfoDAO chats = mock(ChatInfoDAO.class);
        UsernameDAO names = mock(UsernameDAO.class);
        when(chats.getUserNames(41L)).thenReturn(setOf("Juniper", "Orbit"));
        when(names.getNUsernames(3)).thenReturn(setOf("Juniper", "Orbit", "Foxglove"));

        assertEquals("Foxglove", new NameGenerator(chats, names).generateName(41L));
    }

    @Test
    public void fallsBackToAUniqueGuestName() throws Exception {
        ChatInfoDAO chats = mock(ChatInfoDAO.class);
        UsernameDAO names = mock(UsernameDAO.class);
        when(chats.getUserNames(42L)).thenReturn(setOf("Juniper", "Guest-3"));
        when(names.getNUsernames(3)).thenReturn(setOf("Juniper"));

        assertEquals("Guest-4", new NameGenerator(chats, names).generateName(42L));
    }

    private Set<String> setOf(String... values) {
        Set<String> result = new LinkedHashSet<>();
        for (String value : values) {
            result.add(value);
        }
        return result;
    }
}

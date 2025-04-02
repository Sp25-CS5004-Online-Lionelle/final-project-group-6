import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.Database;
import model.Records.Park;
import org.junit.jupiter.api.BeforeEach;
import java.util.Set;
import java.util.List;

/**
 * Unit tests for the {@link Database} class.
 */
public class DatabaseTest {

    private Database database;
    private Park park1;
    private Park park2;

    @BeforeEach
    public void setUp() {
        database = new Database();
        park1 = new Park("Yellowstone", "Wyoming", "Yellowstone national park",
                List.of("Hiking", "Camping"), List.of("url1", "url2"), "123", "YS");
        park2 = new Park("Yosemite", "California", "Yosemite national park",
                List.of("Hiking", "Camping"), List.of("url3", "url4"), "456", "YM");
    }

    @Test
    public void testContainsPark() {
        database.updateDB(Set.of(park1));
        assertTrue(database.contains(park1));
        assertFalse(database.contains(park2));
    }

    @Test
    public void testGetParkByName() {
        database.updateDB(Set.of(park1, park2));
        assertEquals(park1, database.getParkByName("YELLOWstone"));
        assertEquals(park2, database.getParkByName("yosemite"));
        assertNull(database.getParkByName("Grand Canyon"));
    }

    @Test
    public void testSavePark() {
        database.savePark(park1);
        assertTrue(database.contains(park1));
    }

    @Test
    public void testRemovePark() {
        database.updateDB(Set.of(park1, park2));
        database.removePark(park1);
        assertFalse(database.contains(park1));
        assertTrue(database.contains(park2));
    }

    @Test
    public void testUpdateDB() {
        database.updateDB(List.of(park1));
        assertTrue(database.contains(park1));
        assertFalse(database.contains(park2));
    }

    @Test
    public void testClear() {
        database.updateDB(Set.of(park1));
        database.clear();
        assertFalse(database.contains(park1));
    }
}

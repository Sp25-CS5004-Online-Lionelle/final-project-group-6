import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.Database;
import model.Records.*;
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
        park1 = new Park("Yellowstone", "WY", "America's first national park", 
            List.of(new Activity("84902834092", "Hiking"), new Activity("93280409", "Fishing")),
            List.of(new Address("91209", "Yellowstone City", "WY", "1234 yellow street")), 
            List.of(new ParkImage("buffalo in field", "www.urltoimage.com", "Bob Guy")), 
            "YELL");
        park2 = new Park("Grand Canyon", "AZ", "One of the seven natural wonders of the world", 
            List.of(new Activity("123456789", "Rafting"), new Activity("9083409", "Hiking"), new Activity("987654321", "Camping")),
            List.of(new Address("86023", "Grand Canyon Village", "AZ", "567 Canyon Road")), 
            List.of(new ParkImage("Sunset over canyon", "www.grandcanyonimage.com", "Jane Doe")), 
            "GRCA");
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
        assertEquals(park2, database.getParkByName("Grand Canyon"));
        assertNull(database.getParkByName("yosemite"));
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

    @Test
    public void testGetParksByActivityName() {
        database.updateDB(Set.of(park1, park2));

        Set<Park> hikingParks = database.getParksByActivityName("Hiking");
        assertEquals(2, hikingParks.size());
        assertTrue(hikingParks.contains(park1));
        assertTrue(hikingParks.contains(park2));

        Set<Park> fishingParks = database.getParksByActivityName("Fishing");
        assertEquals(1, fishingParks.size());
        assertTrue(fishingParks.contains(park1));

        Set<Park> campingParks = database.getParksByActivityName("Camping");
        assertEquals(1, campingParks.size());
        assertTrue(campingParks.contains(park2));
    }

    @Test
    public void testGetParkByParkCode() {
        database.updateDB(Set.of(park1, park2));

        assertEquals(park1, database.getParkByParkCode("yell"));
        assertEquals(park1, database.getParkByParkCode("YELL"));
        assertEquals(park2, database.getParkByParkCode("gRcA"));
    }
}

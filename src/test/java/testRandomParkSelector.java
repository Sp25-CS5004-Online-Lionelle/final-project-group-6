import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.RandomParkSelector;
import java.util.List;
import java.util.Random;

public class testRandomParkSelector {
    private static final String PARK_CODES_FILE = "data/ParkCodes.txt";
    private Random mockRandom;
    private RandomParkSelector selector;

    @BeforeEach
    public void setUp() {
        // Create a mock Random that returns 0
        mockRandom = new Random() {
            @Override
            public int nextInt(int bound) {
                return 0;
            }
        };

        selector = new RandomParkSelector(mockRandom, PARK_CODES_FILE);
    }

    @Test
    public void testLoadParkCodes() {
        List<String> codes = selector.getParkCodes();
        assertNotNull(codes);
        assertTrue(codes.size() > 0);
        assertTrue(codes.contains("YELL")); // Yellowstone
        assertTrue(codes.contains("GRCA")); // Grand Canyon
        assertTrue(codes.contains("YOSE")); // Yosemite
        assertTrue(codes.contains("ZION")); // Zion
    }

    @Test
    public void testGetRandomParkCode() {
        String code = selector.getRandomParkCode();
        assertNotNull(code);
        assertEquals(4, code.length());
    }

    @Test
    public void testGetRandomParkCodeWithInvalidPath() {
        RandomParkSelector invalidSelector = new RandomParkSelector(mockRandom, "invalid/path.txt");
        String code = invalidSelector.getRandomParkCode();
        assertNull(code);
    }
} 
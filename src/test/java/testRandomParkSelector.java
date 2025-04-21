import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.RandomParkSelector;
import model.DisplayParks;
import java.util.List;
import java.util.Random;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class testRandomParkSelector {
    private static final String PARK_CODES_FILE = "data/ParkCodes.txt";
    private Random mockRandom;
    private RandomParkSelector selector;
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

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
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void tearDown() {
        System.setErr(originalErr);
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
        String invalidPath = "invalid/path.txt";
        RandomParkSelector invalidSelector = new RandomParkSelector(mockRandom, invalidPath);
        String code = invalidSelector.getRandomParkCode();
        assertNull(code);
        String expectedOutput = String.format("Failed to load park codes from file: %s%n%s%n", 
            invalidPath, DisplayParks.formatBasicParkInfo(null));
        assertEquals(expectedOutput, errContent.toString());
    }

    @Test
    public void testGetRandomParkCodeWithInvalidCode() {
        // Create a new selector with an empty park codes list
        RandomParkSelector emptySelector = new RandomParkSelector(mockRandom, "") {
            @Override
            public List<String> getParkCodes() {
                return new ArrayList<>();
            }
        };
        
        errContent.reset(); // Clear any previous error messages
        String code = emptySelector.getRandomParkCode();
        assertNull(code);
        assertEquals(DisplayParks.formatBasicParkInfo(null) + System.lineSeparator(), errContent.toString());
    }
} 
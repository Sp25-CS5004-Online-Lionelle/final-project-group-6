import model.FileManager;
import model.Records.Park;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    private static final String USER_SEARCH_RESULTS = "src/main/resources/userSearchResults.json";
    private static final String USER_SAVED_LIST = "src/main/resources/userSavedParks.json";

    private List<Park> testParks;

    @BeforeEach
    void setUp() throws IOException {
        testParks = new ArrayList<>();
        testParks.add(new Park("Yellowstone National Park", "WY", "First National Park", null, null, null, "YELL"));
        testParks.add(new Park("Zion National Park", "UT", "Beautiful canyons", null, null, null, "ZION"));

        // Ensure test files are clean before each test
        Files.deleteIfExists(Paths.get(USER_SEARCH_RESULTS));
        Files.deleteIfExists(Paths.get(USER_SAVED_LIST));
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up test files after each test
        Files.deleteIfExists(Paths.get(USER_SEARCH_RESULTS));
        Files.deleteIfExists(Paths.get(USER_SAVED_LIST));
    }

    @Test
    void testSaveSearchToFile_NullOrEmpty() {
        assertFalse(FileManager.saveSearchToFile(null));
        assertFalse(FileManager.saveSearchToFile(new ArrayList<>()));
    }

    @Test
    void testSaveSearchToFile_Success() throws IOException {
        assertTrue(FileManager.saveSearchToFile(testParks));

        // Verify the file content
        String fileContent = Files.readString(Paths.get(USER_SEARCH_RESULTS));
        assertNotNull(fileContent);
        assertTrue(fileContent.contains("Yellowstone National Park"));
        assertTrue(fileContent.contains("Zion National Park"));
    }

    @Test
    void testLoadSavedParks_EmptyFile() {
        List<Park> parks = FileManager.loadSavedParks();
        assertNotNull(parks);
        assertTrue(parks.isEmpty());
    }

    @Test
    void testLoadSavedParks_Success() throws IOException {
        // Write test data to the file
        FileManager.updateSavedList(testParks);

        // Load the data and verify
        List<Park> loadedParks = FileManager.loadSavedParks();
        assertNotNull(loadedParks);
        assertEquals(2, loadedParks.size());
        assertEquals("Yellowstone National Park", loadedParks.get(0).name());
        assertEquals("Zion National Park", loadedParks.get(1).name());
    }

    @Test
    void testUpdateSavedList_Success() throws IOException {
        FileManager.updateSavedList(testParks);

        // Verify the file content
        String fileContent = Files.readString(Paths.get(USER_SAVED_LIST));
        assertNotNull(fileContent);
        assertTrue(fileContent.contains("Yellowstone National Park"));
        assertTrue(fileContent.contains("Zion National Park"));
    }
}
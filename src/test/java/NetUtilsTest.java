import model.NetUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NetUtilsTest {
    
    @Test
    void testStateAndZipReturnSameData() {
        // Using Washington state (WA) and a Seattle zip code
        String stateResponse = NetUtils.getParksByState("WA");
        String zipResponse = NetUtils.getParksByZip("98101");
        
        // Both responses should be equal since they're querying the same state
        assertEquals(stateResponse, zipResponse);
    }

    @Test
    void testInvalidStateCode() {
        String response = NetUtils.getParksByState("XX")
            .replaceAll("\\s+", "");
        
        String expectedEmptyResponse = "{\"total\":\"0\",\"limit\":\"20\",\"start\":\"0\",\"data\":[]}"
            .replaceAll("\\s+", "");
        
        assertEquals(expectedEmptyResponse, response);
    }

    @Test
    void testGetParksByState() {
        String response = NetUtils.getParksByState("WA");
        assertNotNull(response);
        
        // Test for specific park data in Washington state
        assertTrue(response.contains("Ebey's Landing"));
        assertTrue(response.contains("This stunning landscape on the Salish Sea"));
        
        // Test response structure
        assertTrue(response.contains("\"total\""));
        assertTrue(response.contains("\"data\""));
        assertTrue(response.contains("\"limit\""));
    }

    @Test
    void testGetParksByZip() {
        String response = NetUtils.getParksByZip("98239"); // Coupeville, WA zip code
        assertNotNull(response);
        
        // Test for specific park data near this zip code
        assertTrue(response.contains("Ebey's Landing"));
        
        // Test response structure
        assertTrue(response.contains("\"total\""));
        assertTrue(response.contains("\"data\""));
    }

    @Test
    void testGetParkByParkCode() {
        String response = NetUtils.getParkByParkCode("EBLA");
        assertNotNull(response);
        
        // Test for specific park data
        assertTrue(response.contains("Ebey's Landing"));
        assertTrue(response.contains("This stunning landscape on the Salish Sea"));
        
        // Test response structure
        assertTrue(response.contains("\"total\""));
        assertTrue(response.contains("\"data\""));
        assertTrue(response.contains("\"parkCode\":\"ebla\""));
    }

    @Test
    void testInvalidParkCode() {
        String response = NetUtils.getParkByParkCode("XXXX")
            .replaceAll("\\s+", "");
        
        String expectedEmptyResponse = "{\"total\":\"0\",\"limit\":\"50\",\"start\":\"0\",\"data\":[]}"
            .replaceAll("\\s+", "");
        
        assertEquals(expectedEmptyResponse, response);
    }

    @Test
    void testGetListOfActivities() {
        var activities = NetUtils.getListOfActivities();
        assertNotNull(activities);
        assertFalse(activities.isEmpty());
        
        // Test for common activities that should be present
        assertTrue(activities.contains("Hiking"));
        assertTrue(activities.contains("Camping"));
        assertTrue(activities.contains("Wildlife Watching"));
        
        // Activities list should be reasonable size (currently around 40)
        assertTrue(activities.size() >= 30 && activities.size() <= 50);
    }
}

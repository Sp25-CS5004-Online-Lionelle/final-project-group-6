import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import model.DisplayParks;
import model.Records.*;
import model.ParksModel;
import model.NetUtils;
import java.util.List;

public class DisplayParksTest {
    
    @Test
    void testFormatParksDisplay_EbeysLanding() {
        // Get actual API response for Washington state
        String apiResponse = NetUtils.getParksByState("WA");
        List<Park> parks = null;
        try {
            parks = ParksModel.deserializeResponse(apiResponse);
        } catch (Exception e) {
            System.out.println("There was an error trying to desesrialize api response");
        }
        
        // Get first park (Ebey's Landing)
        Park ebeysLanding = parks.get(0);
        
        // Format single park
        String result = DisplayParks.formatParkListWithSeparators(List.of(ebeysLanding));

        // Split the result into lines and verify each line separately
        String[] lines = result.split("\n");
        
        // Verify each line after trimming
        assertEquals("Park: Ebey's Landing", lines[0].trim());
        assertEquals("Code: ebla", lines[1].trim());
        assertEquals("State: WA", lines[2].trim());
        assertEquals("Description: This stunning landscape on the Salish Sea, with its rich farmland and promising seaport, lured the earliest American pioneers north of the Columbia Ri...", lines[3].trim());
    }

    @Test
    void testFormatParkListWithSeparators_EmptyList() {
        String result = DisplayParks.formatParkListWithSeparators(List.of());
        assertEquals("No parks found.", result);
    }

    @Test
    void testFormatParkListWithSeparators_NullList() {
        String result = DisplayParks.formatParkListWithSeparators(null);
        assertEquals("No parks found.", result);
    }
    
    @Test
    void testFormatBasicParkInfo() {
        // Get actual API response for Washington state
        String apiResponse = NetUtils.getParksByState("WA");
        List<Park> parks = null;
        try {
            parks = ParksModel.deserializeResponse(apiResponse);
        } catch (Exception e) {
            System.out.println("There was an error trying to desesrialize api response");
        }
        
        // Get first park (Ebey's Landing)
        Park ebeysLanding = parks.get(0);
        
        // Format single park
        String result = DisplayParks.formatBasicParkInfo(ebeysLanding);
        
        // Verify the result contains expected info
        assertTrue(result.contains("Park: Ebey's Landing"));
        assertTrue(result.contains("Code: ebla"));
        assertTrue(result.contains("State: WA"));
    }
    
    @Test
    void testFormatParkDetails() {
        // Get actual API response for Washington state
        String apiResponse = NetUtils.getParksByState("WA");
        List<Park> parks = null;
        try {
            parks = ParksModel.deserializeResponse(apiResponse);
        } catch (Exception e) {
            System.out.println("There was an error trying to deserialize api response");
        }
        
        // Get first park (Ebey's Landing)
        Park ebeysLanding = parks.get(0);
        
        // Format park details
        String result = DisplayParks.formatParkDetails(ebeysLanding);
        
        // Verify the result contains all expected sections
        assertTrue(result.contains("Park: Ebey's Landing"));
        assertTrue(result.contains("Code: ebla"));
        assertTrue(result.contains("State: WA"));
        assertTrue(result.contains("Description: This stunning landscape"));
        assertTrue(result.contains("Address: 162 Cemetery Road Coupeville WA, 98239"));
        
        // Verify activities section is present and properly formatted
        assertTrue(result.contains("Activities:"));
        assertTrue(result.contains("- Hiking"));  // Ebey's Landing offers hiking
        
        // Counting the number of activities to ensure proper formatting
        int activityCount = 0;
        for (String line : result.split("\n")) {
            if (line.trim().startsWith("- ")) {
                activityCount++;
            }
        }
        assertTrue(activityCount > 0, "Should have at least one activity");
    }
    
    @Test
    void testFormatParkDetails_NullPark() {
        String result = DisplayParks.formatParkDetails(null);
        assertEquals("No parks found.", result);
    }
    
    @Test
    void testGetParkSeparator() {
        String separator = DisplayParks.getParkSeparator();
        
        // Verify it's the correct separator (five newlines)
        assertEquals("\n\n\n\n\n", separator);
        
        // Verify the separator splits content correctly
        String testContent = "Test1" + separator + "Test2";
        String[] parts = testContent.split(separator);
        
        assertEquals(2, parts.length);
        assertEquals("Test1", parts[0]);
        assertEquals("Test2", parts[1]);
    }

    @Test
    void testFormatSavedParkListItem_ValidPark() {
        // Get actual API response for Washington state
        String apiResponse = NetUtils.getParksByState("WA");
        List<Park> parks = null;
        try {
            parks = ParksModel.deserializeResponse(apiResponse);
        } catch (Exception e) {
            System.out.println("There was an error trying to deserialize api response");
        }
        
        // Get first park (Ebey's Landing)
        Park ebeysLanding = parks.get(0);
        
        // Format park for saved list
        String result = DisplayParks.formatSavedParkListItem(ebeysLanding);
        
        // Verify format is "Park Name, STATE"
        assertEquals("Ebey's Landing, WA", result);
    }

    @Test
    void testFormatSavedParkListItem_NullPark() {
        String result = DisplayParks.formatSavedParkListItem(null);
        assertEquals("Invalid park", result);
    }
}

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
        List<Park> parks = ParksModel.deserializeResponse(apiResponse);
        
        // Get first park (Ebey's Landing)
        Park ebeysLanding = parks.get(0);
        
        // Format single park
        String result = DisplayParks.formatParksForDisplay(List.of(ebeysLanding));

        // Split the result into lines and verify each line separately
        String[] lines = result.split("\n");
        
        // Verify each line after trimming
        assertEquals("Park: Ebey's Landing", lines[0].trim());
        assertEquals("Code: ebla", lines[1].trim());
        assertEquals("State: WA", lines[2].trim());
        assertEquals("Address: 162 Cemetery Road Coupeville WA, 98239", lines[3].trim());
        assertTrue(lines[4].trim().startsWith("Description: This stunning landscape"));
    }

    @Test
    void testFormatParksForDisplay_EmptyList() {
        String result = DisplayParks.formatParksForDisplay(List.of());
        assertEquals("No parks found.", result);
    }

    @Test
    void testFormatParksForDisplay_NullList() {
        String result = DisplayParks.formatParksForDisplay(null);
        assertEquals("No parks found.", result);
    }
}

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import model.Records.*;
import model.ParksModel;
import model.NetUtils;
import java.util.List;
import controller.ParkController;

class ParksControllerTest {
    
    @Test
    void testParkRecordCreationFromWashingtonApiCall() {
        // Make actual API call for Washington state
        String apiResponse = NetUtils.getParksByState("WA");
        
        // Parse JSON into Park records
        List<Park> parks = ParksModel.deserializeResponse(apiResponse);
        
        // Get first park (Ebey's Landing)
        Park park = parks.get(0);
        
        // Print park details for debugging
        System.out.println("Park Name: " + park.name());
        System.out.println("State: " + park.states());
        System.out.println("Park Code: " + park.parkCode());
        
        // Verify exact fields for Ebey's Landing
        assertEquals("Ebey's Landing", park.name(), "Park name should match");
        assertEquals("WA", park.states(), "State should be WA");
        assertEquals("ebla", park.parkCode(), "Park code should match");
        assertTrue(park.description().contains("This stunning landscape on the Salish Sea"),
                "Description should match the start of actual description");
        
        // Verify address
        assertFalse(park.addresses().isEmpty(), "Should have addresses");
        Address address = park.addresses().get(0);
        assertEquals("98239", address.postalCode(), "Postal code should match");
        assertEquals("Coupeville", address.city(), "City should match");
        assertEquals("WA", address.stateCode(), "State code should match");
        assertEquals("162 Cemetery Road", address.line1(), "Street address should match");
        
        // Verify activities (checking first few)
        assertFalse(park.activities().isEmpty(), "Should have activities");
        assertTrue(park.activities().stream()
                .anyMatch(activity -> activity.name().equals("Astronomy")),
                "Should have Astronomy activity");
        assertTrue(park.activities().stream()
                .anyMatch(activity -> activity.name().equals("Biking")),
                "Should have Biking activity");
        
        // Verify images
        assertFalse(park.images().isEmpty(), "Should have images");
        ParkImage firstImage = park.images().get(0);
        assertEquals("View from the Bluff Overlook", firstImage.title(), "First image title should match");
        assertEquals("NPS Photo / H. Richards", firstImage.credit(), "Image credit should match");
        assertTrue(firstImage.url().contains("structured_data"),
                "Image URL should contain expected path");
    }

}

import model.NetUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.Records;
import model.Records.Park;
import javax.swing.ImageIcon;
import java.util.List;
import java.util.ArrayList;

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
        try {
            var activities = NetUtils.getListOfActivities();
            assertNotNull(activities);
            assertFalse(activities.isEmpty());
            
            // Test for common activities that should be present
            assertTrue(activities.contains("Hiking"));
            assertTrue(activities.contains("Camping"));
            assertTrue(activities.contains("Wildlife Watching"));

        } catch (Exception e) {
            assertTrue(false); // failed the test
            e.printStackTrace();
        }
    
    }

    @Test
    void testDownloadImagesSuccess() {
        // Create a mock park with actual Ebey's Landing image URLs
        List<Records.ParkImage> images = new ArrayList<>();
        images.add(new Records.ParkImage("View from the Bluff Overlook", 
                             "https://www.nps.gov/common/uploads/structured_data/3C84BC00-1DD8-B71B-0BD2CA9CA44675E9.jpg", 
                             "NPS Photo / H. Richards"));
        images.add(new Records.ParkImage("Sunrise over Admiralty Bay", 
                             "https://www.nps.gov/common/uploads/structured_data/3C84BDD7-1DD8-B71B-0B3517BBE2AFC768.jpg", 
                             "NPS Photo / H. Richards"));
        images.add(new Records.ParkImage("Mt Baker and the historic Smith Barn", 
                             "https://www.nps.gov/common/uploads/structured_data/3C84BF6A-1DD8-B71B-0B478117375417E0.jpg", 
                             "NPS Photo / H. Richards"));
        
        Park mockPark = createMockPark("EBLA", "Ebey's Landing", images);
        
        // Test downloading 2 images
        List<ImageIcon> icons = NetUtils.downloadImages(mockPark, 2);
        
        // Should return exactly 2 images
        assertEquals(2, icons.size());
        
        // Images should have height and width (valid images)
        assertTrue(icons.get(0).getIconHeight() > 0);
        assertTrue(icons.get(0).getIconWidth() > 0);
        assertTrue(icons.get(1).getIconHeight() > 0);
        assertTrue(icons.get(1).getIconWidth() > 0);
    }
    
    @Test
    void testDownloadImagesMoreThanAvailable() {
        // Create a mock park with just 1 image
        List<Records.ParkImage> images = new ArrayList<>();
        images.add(new Records.ParkImage("View from the Bluff Overlook", 
                             "https://www.nps.gov/common/uploads/structured_data/3C84BC00-1DD8-B71B-0BD2CA9CA44675E9.jpg", 
                             "NPS Photo / H. Richards"));
        
        Park mockPark = createMockPark("EBLA", "Ebey's Landing", images);
        
        // Try to download 3 images when only 1 is available
        List<ImageIcon> icons = NetUtils.downloadImages(mockPark, 3);
        
        // Should return only 1 image
        assertEquals(1, icons.size());
        assertTrue(icons.get(0).getIconHeight() > 0);
    }
    
    @Test
    void testDownloadImagesEmptyList() {
        // Create a mock park with no images
        Park mockPark = createMockPark("EBLA", "Ebey's Landing", new ArrayList<>());
        
        // Download images from a park with no images
        List<ImageIcon> icons = NetUtils.downloadImages(mockPark, 2);
        
        // Should return an empty list
        assertTrue(icons.isEmpty());
    }
    
    @Test
    void testDownloadImagesInvalidUrl() {
        // Create a mock park with one invalid URL
        List<Records.ParkImage> images = new ArrayList<>();
        images.add(new Records.ParkImage("Invalid Image", 
                             "https://invalid-url-that-does-not-exist.jpg", 
                             "Invalid"));
        
        Park mockPark = createMockPark("EBLA", "Ebey's Landing", images);
        
        // Try to download the invalid image
        List<ImageIcon> icons = NetUtils.downloadImages(mockPark, 1);
        
        // ImageIcon will still create an object for invalid URLs, but the image dimensions will be -1
        // indicating the image failed to load
        assertFalse(icons.isEmpty());
        assertEquals(-1, icons.get(0).getIconWidth());
        assertEquals(-1, icons.get(0).getIconHeight());
    }
    
    @Test
    void testDownloadImagesNullPark() {
        // Download images with a null park
        List<ImageIcon> icons = NetUtils.downloadImages(null, 2);
        
        // Should return an empty list
        assertTrue(icons.isEmpty());
    }
    
    @Test
    void testDownloadZeroImages() {
        // Create a mock park with images
        List<Records.ParkImage> images = new ArrayList<>();
        images.add(new Records.ParkImage("View from the Bluff Overlook", 
                             "https://www.nps.gov/common/uploads/structured_data/3C84BC00-1DD8-B71B-0BD2CA9CA44675E9.jpg", 
                             "NPS Photo / H. Richards"));
        
        Park mockPark = createMockPark("EBLA", "Ebey's Landing", images);
        
        // Request 0 images
        List<ImageIcon> icons = NetUtils.downloadImages(mockPark, 0);
        
        // Should return an empty list
        assertTrue(icons.isEmpty());
    }
    
    /**
     * Helper method to create a mock Park object for testing.
     */
    private Park createMockPark(String parkCode, String name, List<Records.ParkImage> images) {
        // Create a mock Park with just the fields needed for testing
        return new Records.Park(
            name,
            "WA", // states
            "Test description",
            null, // activities
            null, // addresses
            images,
            parkCode
        );
    }
}

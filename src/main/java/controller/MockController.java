package controller;

import model.Records.Park;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.event.ActionListener;

public class MockController implements IController {
    private List<Park> parks;
    private Random random;

    public MockController() {
        parks = new ArrayList<>();
        random = new Random();
        // Add some mock data for rendering

        initializeMockData();
    }

    public List<ActionListener> initActionListeners() {
        throw new UnsupportedOperationException("This method is not implemented in MockController");
    }

    public void runApp() {
        throw new UnsupportedOperationException("This method is not implemented in MockController");
    }

    /**
     * This is made up mock data, demonstrates how to create a valid park object
     */
    private void initializeMockData() {
        parks.add(new Park(
            "Yellowstone", "WY", "America's first national park", 
            List.of("Hiking", "Fishing", "Jogging"), List.of("https://example.com/yellowstone.jpg", "https://example.com/yellowstone2.jpg"),
            "123 Park Ave, Yellowstone, WY", "YELL"));
        parks.add(new Park(
            "Grand Canyon", "AZ", "One of the world's most spectacular natural wonders", 
            List.of("Hiking", "Fishing", "Jogging"), List.of("https://example.com/grandcanyon.jpg"),
            "311 Park Lane, Grand Canyon, AZ", "GCAN"));
        parks.add(new Park("Yosemite","CA", "Famous for cliffs", 
            List.of("Hiking", "Fishing", "Jogging"), List.of("www.example.com", "www.example2.com"), 
            "123 Yosemite BLVD, Yosemite, CA", "YOSM"));
    }

    public List<Park> searchParks(String query) {
        List<Park> results = new ArrayList<>();
        for (Park park : parks) {
            if (park.stateCode().toLowerCase().contains(query.toLowerCase()) ||
                park.Name().toLowerCase().contains(query.toLowerCase())) {
                results.add(park);
            }
        }
        return results;
    }

    public List<Park> getAllParks() {
        return new ArrayList<>(parks);
    }

    public Park getRandomPark() {
        if (parks.isEmpty()) return null;
        return parks.get(random.nextInt(parks.size()));
    }
} 
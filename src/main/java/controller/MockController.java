package controller;

import model.Park;
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

    public List<ActionListener> setActionListeners() {
        throw new UnsupportedOperationException("This method is not implemented in MockController");
    }

    public void runApp() {
        throw new UnsupportedOperationException("This method is not implemented in MockController");
    }

    private void initializeMockData() {
        parks.add(new Park("Yellowstone", "WY", "America's first national park", "https://example.com/yellowstone.jpg"));
        parks.add(new Park("Grand Canyon", "AZ", "One of the world's most spectacular natural wonders", "https://example.com/grandcanyon.jpg"));
        parks.add(new Park("Yosemite", "CA", "Famous for its waterfalls and granite cliffs", "https://example.com/yosemite.jpg"));
    }

    public List<Park> searchParks(String query) {
        List<Park> results = new ArrayList<>();
        for (Park park : parks) {
            if (park.getState().toLowerCase().contains(query.toLowerCase()) ||
                park.getName().toLowerCase().contains(query.toLowerCase())) {
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
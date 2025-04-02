package controller;

import model.Records.*;
import model.Records.Address;
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
        parks.add(new Park("Yellowstone", "WY", "America's first national park", 
            List.of(new Activity("84902834092", "Hiking"), new Activity("93280409", "Fishing")),
            List.of(new Address("91209", "Yellowstone City", "WY", "1234 yellow street")), 
            List.of(new ParkImage("buffalo in field", "www.urltoimage.com", "Bob Guy")), 
            "YELL"));
        parks.add(new Park("Grand Canyon", "AZ", "One of the seven natural wonders of the world", 
            List.of(new Activity("123456789", "Rafting"), new Activity("987654321", "Camping")),
            List.of(new Address("86023", "Grand Canyon Village", "AZ", "567 Canyon Road")), 
            List.of(new ParkImage("Sunset over canyon", "www.grandcanyonimage.com", "Jane Doe")), 
            "GRCA"));

        parks.add(new Park("Yosemite", "CA", "Famous for its waterfalls and giant sequoia trees", 
            List.of(new Activity("1122334455", "Rock Climbing"), new Activity("5544332211", "Wildlife Viewing")),
            List.of(new Address("95389", "Yosemite Valley", "CA", "890 Park Lane")), 
            List.of(new ParkImage("El Capitan view", "www.yosemiteimage.com", "John Smith")), 
            "YOSE"));
    }

    public List<Park> searchParks(String query) {
        List<Park> results = new ArrayList<>();
        for (Park park : parks) {
            if (park.states().toLowerCase().contains(query.toLowerCase()) ||
                park.name().toLowerCase().contains(query.toLowerCase())) {
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
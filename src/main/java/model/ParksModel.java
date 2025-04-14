package model;

import model.Records.Activity;
import model.Records.Park;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Records.ParkWrapper;
import javax.swing.ImageIcon;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParksModel implements IModel{
    /** Path to the file where user saved parks are stored */
    private static final String FILE_PATH = "src/main/resources/userSavedParks.json";

    /** List of parks retrieved from the API */
    private List<Park> parkList;
    /** List of activities in String form */
    private List<String> activityList;
    /** List of parks saved by the user */
    private List<Park> userSavedParks;

    /**
     * Public constructor for Parks model.
     * Initializes the park list and fetches the list of activities from the API.
     */
    public ParksModel() {
        this.parkList = new ArrayList<>();
        this.userSavedParks = new ArrayList<>();
        try {
            this.activityList = NetUtils.getListOfActivities();
        } catch (Exception e) {
            System.out.println("Unable to fetch activity list from API " + e);
        }
    }

    /**
     * Updates the database with a data to be downloaded from the API
     * 
     * @param query
     * @return true if the database was updated successfully, false otherwise
     */
    public boolean updateDB(String query) {
        query = query.trim().toUpperCase();
        String response;

        // Get response from API based on input type
        if (query.matches("\\d{5}")) {
            response = NetUtils.getParksByZip(query);
        } else {
            response = NetUtils.getParksByState(query);
        }

        // Parse JSON response into Park records
        try {
            this.parkList = IModel.deserializeResponse(response);
        } catch (Exception e) {
            System.err.println("error parsing JSON: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Returns the list of parks currently being stored in the model.
     * 
     * @return a list of parks, or an empty list if none are stored
     */
    public List<Park> getParkList() {
        if (this.parkList == null) {
            return new ArrayList<>();
        }
        return this.parkList;
    }

    /**
     * Get the park by its name.
     * 
     * @return the park with the specified name
     */
    public Park getParkByName(String parkName) {
        for (Park park : this.parkList) {
            if (park.name().equalsIgnoreCase(parkName)) {
                return park;
            }
        }
        return null;
    }

    /**
     * Get all parks that offer a specific activity.
     * 
     * @param activityName the name of the activity
     * @return a list of parks that offer the specified activity
     */
    public List<Park> getParksByActivityName(String activityName) {
        List<Park> result = new ArrayList<>();
        for (Park park : this.parkList) {
            for (Activity activity : park.activities()) {
                if (activity.name().equalsIgnoreCase(activityName)) {
                    result.add(park);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Get filtered parks based on a list of selected activities.
     * 
     * @param selectedActivities
     * @return filtered list of parks that match the selected activities
     */
    public List<Park> getFilteredParks(List<String> selectedActivities) {
        List<Park> filteredParks = new ArrayList<>();
        for (String activity : selectedActivities) {
            filteredParks.addAll(getParksByActivityName(activity));
        }
        return filteredParks;
    }

    /**
     * Get the park by its park code.
     * 
     * @return the park with the specified park code
     */
    public Park getParkByParkCode(String parkCode) {
        for (Park park : this.parkList) {
            if (park.parkCode().equalsIgnoreCase(parkCode)) {
                return park;
            }
        }
        return null;
    }

    /**
     * Returns the list of activities available in the parks.
     * 
     * @return a list of activity names, or an empty list if no activities are
     *         available
     */
    public List<String> getActivityList() {
        if (this.activityList == null) {
            return new ArrayList<>();
        }
        return this.activityList;
    }

    /**
     * Sets the park list in the model. This is typically used for testing purposes
     * to set a predefined list of parks.
     * 
     * @param parkList the list of parks to set in the model
     */
    public void setParkList(List<Park> parkList) {
        if (parkList == null) {
            this.parkList = new ArrayList<>();
        } else {
            this.parkList = parkList;
        }
    }

    /**
     * Download images from the url field of a park object.
     * Uses URL object to download the images.
     * @param park
     * @param numImages
     * @return
     */
    public List<ImageIcon> downloadImages(Park park, int numImages) {

        if (park.images() == null || park.images().isEmpty()) {
            System.err.println("No images available for park");
            return new ArrayList<>();
        }

        List<String> urls = park.images()
                .stream()
                .map(img -> img.url())
                .toList();

        List<ImageIcon> icons = new ArrayList<>();

        for (int i = 0; i < numImages; i++) {
            if (i >= urls.size()) {
                break;
            }
            try {
                ImageIcon icon = new ImageIcon(new java.net.URL(urls.get(i)));
                icons.add(icon);
            } catch (Exception e) {
                System.err.println("Failed to load image: " + e.getMessage());
            }
        }

        return icons;
    }

    /**
     * Loads the user saved park list from a file.
     * 
     * @return a list of parks saved by the user
     */
    public List<Park> loadUserSavedParksFromFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Read the JSON file and deserialize it into a list of Park objects
            ParkWrapper wrapper = objectMapper.readValue(new File(FILE_PATH), ParkWrapper.class);
            return wrapper.data();
        } catch (IOException e) {
            System.err.println("Failed to load user saved park list from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

}

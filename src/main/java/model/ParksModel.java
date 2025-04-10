package model;

import model.Records.Activity;
import model.Records.Park;
import model.Records.ParkImage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Records.ParkWrapper;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;

public class ParksModel {
    /** List of parks stored */
    private List<Park> parkList;
    /** List of activities in String form */
    private List<String> activityList;

    /** Public constructor for Parks model.
     * Initializes the park list and fetches the list of activities from the API.
     */
    public ParksModel() {
        this.parkList = new ArrayList<>();
        try {
            this.activityList = NetUtils.getListOfActivities();
        } catch (Exception e) {
            System.out.println("Unable to fetch activity list from API " + e);
        }
    }

    /**
     * Updates the database with a data to be downloaded from the API
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
            this.parkList = ParksModel.deserializeResponse(response);
        } catch (Exception e) {
            System.err.println("error parsing JSON: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Returns the list of parks currently being stored in the model.
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
     * @return a list of activity names, or an empty list if no activities are available
     */
    public List<String> getActivityList() {
        if (this.activityList == null) {
            return new ArrayList<>();
        }
        return this.activityList;
    }

    /**
     * Sets the park list in the model. This is typically used for testing purposes to set a predefined list of parks.
     * @param parkList the list of parks to set in the model
     */
    public void setParkList(List<Park> parkList) {
        if (parkList == null) {
            this.parkList = new ArrayList<>();
        } else {
            this.parkList = parkList;
        }
    }

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
     * Deserialize the response from the API into a Park object
     * @param Json the JSON received from the API
     * @return a park object
     */
    public static List<Park> deserializeResponse(String json) throws JsonProcessingException, JsonMappingException {
        ObjectMapper om = new ObjectMapper();
        ParkWrapper wrapper = om.readValue(json, ParkWrapper.class);
        return wrapper.data();
    }

    /** 
     * Takes list of parks and converts it into a json string with all the data
     * NOTE: I chose to write the value as a string for testing purposes, we could alternatively changes this to 'writeValueAsBytes()'
     * We also could refactor to have this method write directly to the file {@link https://github.com/FasterXML/jackson-databind}
    */
    public static String serializeList(List<Park> parks) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        ParkWrapper wrapper = new ParkWrapper(parks);
        return om.writeValueAsString(wrapper);
    }
}
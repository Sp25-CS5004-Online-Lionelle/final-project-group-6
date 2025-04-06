package model;

import model.Records.Activity;
import model.Records.Park;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Records.ParkWrapper;
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
}
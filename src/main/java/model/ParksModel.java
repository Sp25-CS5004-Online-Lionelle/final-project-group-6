package model;

import model.Records.Activity;
import model.Records.Park;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Records.ParkWrapper;
import javax.swing.ImageIcon;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParksModel implements IModel {
    /** Path to the file where user search results are stored */
    private static final String USER_SEARCH_RESULTS = "src/main/resources/userSearchResults.json";

    /** Path to file where user saved parks are stored */
    private static final String USER_SAVED_LIST = "src/main/resources/userSavedParks.json";

    /** List of parks retrieved from the API */
    private List<Park> parkList;

    /** List of activities in String form */
    private List<String> activityList;

    /** Random park selector */
    private final RandomParkSelector randomSelector;

    /**
     * Public constructor for Parks model.
     * Initializes the park list and fetches the list of activities from the API.
     */
    public ParksModel() {
        this.parkList = new ArrayList<>();
        this.randomSelector = new RandomParkSelector();
        try {
            this.activityList = NetUtils.getListOfActivities();
        } catch (Exception e) {
            System.out.println("Unable to fetch activity list from API " + e);
        }
    }

    /**
     * Gets a random park from all national parks using park codes.
     * 
     * @return true if a random park was successfully loaded, false otherwise
     */
    public boolean getRandomPark() {
        String randomParkCode = randomSelector.getRandomParkCode();
        if (randomParkCode == null) {
            return false;
        }

        // Get the park data from the API
        String response = NetUtils.getParkByParkCode(randomParkCode);
        if (response == null) {
            return false;
        }

        // Parse the response and update the park list
        try {
            this.parkList = IModel.deserializeResponse(response);
            return !this.parkList.isEmpty();
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the database with a data to be downloaded from the API
     * 
     * @param query the query to search for parks (e.g., park name, state code, or
     *              zip code)
     * @return true if the database was updated successfully, false otherwise
     */
    public boolean updateDB(String query) {
        query = query.trim().toUpperCase();
        String response;
        boolean filterByName = false;

        // Get response from API based on input type
        if (query.matches("\\d{5}")) {
            response = NetUtils.getParksByZip(query);
        } else if (query.matches("^[A-Z]{2}$")) { // Matches a 2-letter state code (e.g., MA, WA)
            response = NetUtils.getParksByState(query);
        } else { // Default to searching by park name
            response = NetUtils.getParksByName(query);
            filterByName = true;
        }

        // Parse JSON response into Park records
        try {
            List<Park> allParks = IModel.deserializeResponse(response);

            // Filter parks to include only those whose names contain the query
            if (filterByName) {
                final String queryParkName = query;
                allParks = allParks.stream()
                        .filter(park -> park.name().toUpperCase().contains(queryParkName))
                        .toList();
            }

            this.parkList = allParks;
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
     * Saves the current list of parks to a file.
     *
     * @return true if the pars were saved, else false
     * @param filePath Path to save the file
     */
    @Override
    public boolean saveSearchToFile() {

        List<Park> currentResults = this.getParkList();
        if (currentResults == null || currentResults.isEmpty()) {
            return false;
        }

        List<Park> existingParks = this.loadSavedParks();
        if (existingParks == null) {
            existingParks = new ArrayList<>();
        }

        // Add new parks to the existing list if they are not already present
        for (Park park : currentResults) {
            if (!existingParks.contains(park)) {
                existingParks.add(park);
            }
        }

        try { // serialize and write to file
            FileWriter writer = new FileWriter(USER_SEARCH_RESULTS);
            String updatedJson = IModel.serializeList(existingParks);
            writer.write(updatedJson);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Loads the user saved park list from a file.
     * 
     * @return a list of parks saved by the user
     */
    @Override
    public List<Park> loadSavedParks() {
        return loadFile(USER_SAVED_LIST);
    }

    /**
     * Loads the user saved search results from a file.
     * 
     * @return a list of parks (search) saved by the user
     */
    @Override
    public List<Park> loadSavedSearch() {
        return loadFile(USER_SEARCH_RESULTS);
    }

    /**
     * Syncs the saved list stored as json with the saved list displayed in the UI.
     * 
     * @param parks
     */
    @Override
    public void updateSavedList(List<Park> parks) {
        try {
            String JSON = IModel.serializeList(parks);
            FileWriter writer = new FileWriter(USER_SAVED_LIST); // will overwrite here intentionally
            writer.write(JSON);
            writer.close();
        } catch (Exception e) {
            System.err.println("Failed to update saved list: " + e.getMessage());
        }
    }

    /**
     * Loads the user saved park list from a file.
     * 
     * @return a list of parks saved by the user
     */
    private List<Park> loadFile(String file) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Read the JSON file and deserialize it into a list of Park objects
            ParkWrapper wrapper = objectMapper.readValue(new File(file), ParkWrapper.class);
            return wrapper.data();
        } catch (IOException e) {
            System.err.println("Failed to load user saved park list from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

}

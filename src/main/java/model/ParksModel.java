package model;

import model.Records.Activity;
import model.Records.Park;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParksModel implements IModel {
    /** List of parks retrieved from the API. */
    private List<Park> parkList;

    /** List of activities in String form. */
    private List<String> activityList;

    /** Random park selector. */
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
            System.err.println("Unable to fetch activity list from API " + e);
            //set activity list to empty list in case of api failure
            this.activityList = new ArrayList<>();
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
     * Updates the database with a data to be downloaded from the API.
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
            if (!strValid("data/ValidZips.txt", 
                query.substring(0, 5))) { // Returning false if invalid state code is passed
                return false;
            }
            response = NetUtils.getParksByZip(query);
        } else if (query.matches("^[A-Z]{2}$")) { // Matches a 2-letter state code (e.g., MA, WA)
            if (!strValid("data/StateCodes.txt", 
                query.substring(0, 2))) { // Returning false if invalid state code is passed
                return false;
            }
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
     * Update the database with a List rather than search query.
     * Important for keeping model and view in sync.
     * 
     * @param pl the list of parks to update the model with
     * @return true if the db was updated, else false
     */
    public boolean updateDB(List<Park> pl) {
        if (pl != null && !pl.isEmpty()) {
            this.parkList = pl;
            return true;
        }
        return false;
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
     * @param parkName
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
     * @param parkCode
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
     * Helper method to validate that a state code or zip is valid.
     * @param filePath
     * @param str
     * @return true if code/zip is valid, else false
     */
    private boolean strValid(String filePath, String str) {
        Set<String> set = new HashSet<>();

        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            while ((line = reader.readLine()) != null) {
                set.add(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return set.contains(str);
    }
}

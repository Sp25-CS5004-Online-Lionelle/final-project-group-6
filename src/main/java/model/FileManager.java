package model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.Records.Park;
import model.Records.ParkWrapper;

public final class FileManager {

    /** Path to the file where user search results are stored. */
    private static final String USER_SEARCH_RESULTS = "src/main/resources/userSearchResults.json";

    /** Path to file where user saved parks are stored. */
    private static final String USER_SAVED_LIST = "src/main/resources/userSavedParks.json";

    /** Private Constructor to prevent instantiation. */
    private FileManager() {
    }

    /**
     * Saves the current list of parks to a file.
     *
     * @return true if the pars were saved, else false
     * @param currentResults list of parks to save the file
     */
    public static boolean saveSearchToFile(List<Park> currentResults) {

        if (currentResults == null || currentResults.isEmpty()) {
            return false;
        }

        List<Park> existingParks = loadSavedParks();
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
    
    public static List<Park> loadSavedParks() {
        return loadFile(USER_SAVED_LIST);
    }

    /**
     * Loads the user saved search results from a file.
     * 
     * @return a list of parks (search) saved by the user
     */
    
    public static List<Park> loadSavedSearch() {
        return loadFile(USER_SEARCH_RESULTS);
    }

    /**
     * Syncs the saved list stored as json with the saved list displayed in the UI.
     * 
     * @param parks
     */
    public static void updateSavedList(List<Park> parks) {
        try {
            String json = IModel.serializeList(parks);
            FileWriter writer = new FileWriter(USER_SAVED_LIST); // will overwrite here intentionally
            writer.write(json);
            writer.close();
        } catch (Exception e) {
            System.err.println("Failed to update saved list: " + e.getMessage());
        }
    }

    /**
     * Loads the user saved park list from a file.
     * 
     * @param file
     * @return a list of parks saved by the user
     */
    private static List<Park> loadFile(String file) {
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

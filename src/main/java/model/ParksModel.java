package model;
import model.Records.Park;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Records.ParkWrapper;
import java.util.List;

public class ParksModel {

    /**
     * Updates the database with a data to be downloaded from the API
     * @param query
     * @return true if the database was updated successfully, false otherwise
     */
    public boolean updateDB(String query) {
        throw new UnsupportedOperationException("this is unimplemented");
    }

    /**
     * Gets filtered data from the database, Filter.NONE if no filter is required
     * @param filter the filter to apply from Filter enum 
     * 
     * Example: List<Park> parks = getFilteredData(Filter.CONTAINS_STR, "Yellowstone");
     * @return returns a collection of parks that match the filter requirements
     */
    public List<Park> hasActivity(Activities filter, String query) {
        throw new UnsupportedOperationException("this is unimplemented");
    }

    /**
     * Gets parks by zip code from the API.
     * 
     * @param zip The zip code to search for
     * @return JSON response string from the API
     */
    public String getParksByZip(String zip) {
        return NetUtils.getParksByZip(zip);
    }

    /**
     * Gets parks by state code from the API.
     * 
     * @param stateCode The state code to search for
     * @return JSON response string from the API
     */
    public String getParksByState(String stateCode) {
        return NetUtils.getParksByState(stateCode);
    }

    /**
     * Gets all parks from the API.
     * 
     * @return JSON response string containing all parks
     */
    public String getAllParks() {
        // You might want to implement this in NetUtils first
        throw new UnsupportedOperationException("getAllParks not implemented yet");
    }

    /**
     * Deserialize the response from the API into a Park object
     * @param Json the JSON received from the API
     * @return a park object
     */
    public static List<Park> deserializeResponse(String json) {
        ObjectMapper om = new ObjectMapper();
        try {
            ParkWrapper wrapper = om.readValue(json, ParkWrapper.class);
            return wrapper.data();
        } catch (Exception e) {
            System.err.println("Error deserializing JSON: " + e);
            return null;
        }
    }
}
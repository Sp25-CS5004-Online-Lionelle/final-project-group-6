package model;
import model.Records.Park;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Records.ParkWrapper;
import model.Records.Park;
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
     * Serialize the response from the API into a Park object
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
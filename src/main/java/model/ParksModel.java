package model;
import java.util.Collection;

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
     * @return returns a collection of parks that match the filter requirements
     */
    public Collection<Park> getFilteredData(Filter filter) {
        throw new UnsupportedOperationException("this is unimplemented");
    }

    /**
     * Serialize the response from the API into a Park object
     * @param Json the JSON received from the API
     * @return a park object
     */
    private static Park serializeResponse(String Json) {
        throw new UnsupportedOperationException("Unimplemented");
    }
}
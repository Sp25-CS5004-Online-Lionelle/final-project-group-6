package model;
import model.Records.Park;
import java.util.Collection;

/**
 * Interface representing the model to be used
 */
public interface IModel {
        /**
         * Updates the database with a data to be downloaded from the API
         * @param query
         * @return true if the database was updated successfully, false otherwise
         */
        public boolean updateDB(String query);

        /**
         * Gets filtered data from the database, Filter.NONE if no filter is required
         * @param filter the filter to apply from Filter enum 
         * @return returns a collection of parks that match the filter requirements
         */
        public Collection<Park> getFilteredData(Activities filter);
}
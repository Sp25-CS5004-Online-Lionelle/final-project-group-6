package model;
import model.Records.Park;
import java.util.List;

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
         * Gets filtered data from the database
         * @param selectedActivities the activities to filter by 
         * @return returns a list of parks that match the filter requirements
         */
        public List<Park> getFilteredParks(List<String> selectedActivities);
}
package model;

import model.Records.Park;
import model.Records.ParkWrapper;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
         * Update the database with a sub-list.
         * @param pl
         */
        public boolean updateDB(List<Park> pl);

        /**
         * Returns the list of parks currently being stored in the model.
         * 
         * @return a list of parks, or an empty list if none are stored
         */
        public List<Park> getParkList();

        /**
         * Get the park by its name.
         * 
         * @return the park with the specified name
         */
        public Park getParkByName(String parkName);

        /**
         * Get all parks that offer a specific activity.
         * 
         * @param activityName the name of the activity
         * @return a list of parks that offer the specified activity
         */
        public List<Park> getParksByActivityName(String activityName);

        /**
         * Get filtered parks based on a list of selected activities.
         * 
         * @param selectedActivities
         * @return filtered list of parks that match the selected activities
         */
        public List<Park> getFilteredParks(List<String> selectedActivities);

        /**
         * Get the park by its park code.
         * 
         * @return the park with the specified park code
         */
        public Park getParkByParkCode(String parkCode);

        /**
         * Returns the list of activities available in the parks.
         * 
         * @return a list of activity names, or an empty list if no activities are
         *         available
         */
        public List<String> getActivityList();

        /**
         * Takes list of parks and converts it into a json string with all the data
         * NOTE: I chose to write the value as a string for testing purposes, we could
         * alternatively changes this to 'writeValueAsBytes()'
         * We also could refactor to have this method write directly to the file
         * {@link https://github.com/FasterXML/jackson-databind}
         */
        public static String serializeList(List<Park> parks) throws JsonProcessingException {
                if (parks == null) {
                        return "";
                }
                ObjectMapper om = new ObjectMapper();
                ParkWrapper wrapper = new ParkWrapper(parks);
                om.enable(SerializationFeature.INDENT_OUTPUT);
                return om.writeValueAsString(wrapper);
        }

        /**
         * Deserialize the response from the API into a Park object
         * 
         * @param Json the JSON received from the API
         * @return a park object
         */
        public static List<Park> deserializeResponse(String json) throws JsonProcessingException, JsonMappingException {
                ObjectMapper om = new ObjectMapper();
                ParkWrapper wrapper = om.readValue(json, ParkWrapper.class);
                return wrapper.data();
        }

        /**
         * Gets a random park from all national parks.
         * @return true if a random park was successfully loaded, false otherwise
         */
        public boolean getRandomPark();
}
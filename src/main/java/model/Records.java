package model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public final class Records {
    /**
     * The fields of a Park object, an immutable record
     * 
     * @param name        the name of the park
     * @param states      the state code of the park
     * @param description a brief description of the park
     * @param activities  a list of activities available at the park (activity id, name)
     * @param images      a list of imageInfo objects (title, url, credit)
     * @param address     a list of the addresses of the park (postalCode, city, stateCode, line1)
     * @param parkCode    the park code of the park
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Park(String name, String states, String description, List<Activity> activities, List<Address> addresses, List<ParkImage> images, String parkCode) {
    };
    /**
     * Record representing an activity available.
     * Activity id is available if needed.
     * @param id   the id of the activity
     * @param name the name of the activity
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Activity(String id, String name) {
    };
    /**
     * Record representing an address to a National Park.
     * Note: To string returns a formatted address.
     * @param postalCode the postal code of the park
     * @param city       the city of the park
     * @param stateCode  the state code of the park
     * @param line1      the first line of the address (street address)
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Address(String postalCode, String city, String stateCode, String line1) {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (line1 != null && !line1.isEmpty()) {
                sb.append(line1);
            }
            if (city != null && !city.isEmpty()) {
                if (sb.length() > 0) sb.append(" ");
                sb.append(city);
            }
            if (stateCode != null && !stateCode.isEmpty()) {
                if (sb.length() > 0) sb.append(" ");
                sb.append(stateCode);
            }
            if (postalCode != null && !postalCode.isEmpty()) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(postalCode);
            }
            return sb.toString();
        }
    };
    /**
     * Record representing an activity available.
     * Activity id is available if needed.
     * @param title the title of the image
     * @param url   the url of the image
     * @param credit the credit for the image (photographer or source)
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ParkImage(String title, String url, String credit) {
    };
    /**
     * Wrapper class for the list of activities returned by the NPS API.
     * Used for deserialization of the parks endpoint from the NPS API.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ParkWrapper(List<Park> data) {
    };
    /**
     * Wrapper class for the list of activities returned by the NPS API
     * Used for deserialization of the activities endpoint from the NPS API.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ActivityWrapper(List<Activity> data) {
    };


}
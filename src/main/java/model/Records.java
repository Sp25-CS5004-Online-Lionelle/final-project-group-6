package model;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public final class Records {
    /**
     * The fields of a Park object, an immutable record
     * @param Name the name of the park
     * @param stateCode the state code of the park
     * @param Description a description of the park
     * @param Activities a list of activities available at the park
     * @param imageUrls a list of image URLs of the park
     * @param address the address of the park
     * @param parkCode the park code of the park
     */
    public record Park(String Name, String stateCode, String Description, List<String> Activities, 
        List<String> imageUrls, String address, String parkCode){};

    /**
     * Wrapper class for the list of activities returned by the NPS API
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ActivityWrapper(List<Activity> data){};

    /**
     * Record representing an activity available.
     * Activity id is available if needed.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Activity(String name){};
}
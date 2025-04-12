package model;

import model.Records.Park;
import java.util.List;

/**
 * Utility class for formatting park information for display.
 * This class serves as the single source of truth for all text formatting in the application.
 */
public class DisplayParks {
    /** Consistent separator to use between parks */
    private static final String PARK_SEPARATOR = "\n\n\n\n\n"; // Five line breaks for more visual separation
    
    /**
     * Formats a list of parks for display with separators between each park.
     * 
     * @param parks List of parks to format
     * @return Formatted string with all parks and separators
     */
    public static String formatParkListWithSeparators(List<Park> parks) {
        if (parks == null || parks.isEmpty()) {
            return "No parks found.";
        }

        StringBuilder display = new StringBuilder();
        
        for (int i = 0; i < parks.size(); i++) {
            // Add the formatted park
            display.append(formatBasicParkInfo(parks.get(i)));
            
            // Add separator only if not the last park
            if (i < parks.size() - 1) {
                display.append(PARK_SEPARATOR);
            }
        }

        return display.toString();
    }
    
    /**
     * Formats basic information about a single park (used for summary views).
     * This is the core formatting method that doesn't add any separators.
     * 
     * @param park The park to format
     * @return Formatted string with basic park information
     */
    public static String formatBasicParkInfo(Park park) {
        if (park == null) {
            return "No parks found.";
        }
        
        // Create a more compact summary for parks
        StringBuilder result = new StringBuilder();
        result.append("Park: ").append(park.name()).append("\n");
        result.append("Code: ").append(park.parkCode()).append("\n");
        result.append("State: ").append(park.states()).append("\n");
        
        // Truncate description to 150 characters if it's too long
        String description = park.description();
        if (description.length() > 150) {
            description = description.substring(0, 150) + "...";
        }
        result.append("Description: ").append(description);
        
        return result.toString();
    }

    /**
     * Formats detailed information about a park including activities.
     * 
     * @param park The park to format
     * @return Formatted string containing detailed park information
     */
    public static String formatParkDetails(Park park) {
        if (park == null) {
            return "No parks found.";
        }
        
        StringBuilder details = new StringBuilder();
        details.append(String.format("""
            Park: %s
            Code: %s
            State: %s
            Description: %s
            Address: %s
            """, 
            park.name(),
            park.parkCode(),
            park.states(),
            park.description(),
            park.addresses().get(0).toString()));

        if (!park.activities().isEmpty()) {
            details.append("\nActivities:\n");
            for (var activity : park.activities()) {
                details.append("- ").append(activity.name()).append("\n");
            }
        }

        return details.toString();
    }
    
    /**
     * Gets the standard separator used between parks.
     * @return The separator string
     */
    public static String getParkSeparator() {
        return PARK_SEPARATOR;
    }

    /**
     * Formats a park name and state for the saved parks list.
     * 
     * @param park The park to format
     * @return Formatted string in the format "Park Name, STATE"
     */
    public static String formatSavedParkListItem(Park park) {
        if (park == null) {
            return "Invalid park";
        }
        return String.format("%s, %s", park.name(), park.states());
    }
}

package model;

import model.Records.Park;
import java.util.List;

/**
 * Utility class for formatting park information for display.
 */
public class DisplayParks {
    /**
     * Formats a list of parks for display in the text panel.
     * Shows name, code, state, description, and address.
     * 
     * @param parks List of parks to format
     * @return Formatted string ready for display
     */
    public static String formatParksForDisplay(List<Park> parks) {
        if (parks == null || parks.isEmpty()) {
            return "No parks found.";
        }

        StringBuilder display = new StringBuilder();
        
        for (Park park : parks) {
            display.append(String.format("""
                Park: %s
                Code: %s
                State: %s
                Address: %s
                Description: %s
                """, 
                park.name(),
                park.parkCode(),
                park.states(),
                park.addresses().get(0).toString(),
                park.description()));
            
            display.append("\n----------------------------------------\n");
        }

        return display.toString();
    }
}

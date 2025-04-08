package view;
import javax.swing.*;
import java.awt.*;
import config.Settings;

/**
 * Panel for displaying and managing a list of saved parks.
 * Provides a scrollable list with add/remove functionality.
 */
public class SavedParksPanel extends JPanel {
    /** Application settings */
    private final Settings settings = Settings.getInstance();
    
    /** List for displaying saved parks */
    private JList<String> savedParksList;
    /** Model for the saved parks list */
    private DefaultListModel<String> parksListModel;
    /** Scroll container for the list */
    private JScrollPane scrollPane;
    /** Header label showing "Saved Parks" */
    private JLabel titleLabel;

    /**
     * Creates a new saved parks panel with proper layout and padding.
     */
    public SavedParksPanel() {
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(0, 
            settings.SMALL_PADDING, settings.PADDING, settings.SMALL_PADDING));
        initializeComponents();
    }

    /**
     * Sets up the title label and scrollable list.
     */
    private void initializeComponents() {
        // Title label
        titleLabel = new JLabel("Saved Parks");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(settings.TITLE_FONT);
        
        // Create list model
        parksListModel = new DefaultListModel<>();
        
        // Saved parks list
        savedParksList = new JList<>(parksListModel);
        savedParksList.setFont(settings.SAVED_LIST_FONT);
        savedParksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Scroll pane for list
        scrollPane = new JScrollPane(savedParksList);
        scrollPane.setPreferredSize(new Dimension(
            settings.SAVED_TEXT_WIDTH, settings.SAVED_TEXT_HEIGHT));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Add components to panel with padding
        add(titleLabel, BorderLayout.NORTH);
        
        // Create a wrapper panel for the scroll pane with padding
        JPanel scrollWrapper = new JPanel(new BorderLayout());
        scrollWrapper.add(scrollPane, BorderLayout.CENTER);
        scrollWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 
            settings.PADDING, 0));
        
        add(scrollWrapper, BorderLayout.CENTER);
    }

    /**
     * Updates the entire list of saved parks.
     * 
     * @param parksList String representation of parks, will be parsed into individual parks
     */
    public void updateSavedParksList(String parksList) {
        parksListModel.clear();
        
        if (parksList != null && !parksList.trim().isEmpty()) {
            String[] parks = parksList.split("\n");
            for (String park : parks) {
                // Remove any bullet points that might exist in the input
                if (park.startsWith("• ")) {
                    park = park.substring(2);
                }
                if (!park.trim().isEmpty()) {
                    parksListModel.addElement(park.trim());
                }
            }
        }
    }

    /**
     * Adds a single park to the end of the list.
     * 
     * @param parkName Name of the park to add
     */
    public void addParkToList(String parkName) {
        if (parkName != null && !parkName.trim().isEmpty()) {
            parksListModel.addElement(parkName.trim());
        }
    }

    /**
     * Clears all parks from the list.
     */
    public void clearList() {
        parksListModel.clear();
    }

    /**
     * Gets the current list of saved parks as a string.
     * 
     * @return String containing all saved parks
     */
    public String getSavedParksList() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parksListModel.getSize(); i++) {
            String park = parksListModel.getElementAt(i);
            sb.append(park).append("\n");
        }
        return sb.toString();
    }
    
    /**
     * Gets the selected park from the list.
     * 
     * @return The selected park name, or null if nothing selected
     */
    public String getSelectedPark() {
        return savedParksList.getSelectedValue();
    }
}

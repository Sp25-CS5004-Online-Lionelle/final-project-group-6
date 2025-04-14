package view;
import javax.swing.*;
import java.awt.*;
import config.Settings;
import model.DisplayParks;
import model.Records.Park;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel for displaying and managing a list of saved parks.
 * Provides a scrollable list with add/remove functionality.
 */
public class SavedParksPanel extends JPanel {
    /** Application settings. */
    private final Settings settings = Settings.getInstance();
    
    /** List for displaying saved parks. */
    private JList<String> savedParksList;
    /** Model for the saved parks list. */
    private DefaultListModel<String> parksListModel;
    /** List of actual Park objects corresponding to the displayed strings. */
    private List<Park> parks;
    /** Scroll container for the list. */
    private JScrollPane scrollPane;
    /** Header label showing "Saved Parks". */
    private JLabel titleLabel;

    /**
     * Creates a new saved parks panel with proper layout and padding.
     */
    public SavedParksPanel() {
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(0, 
            settings.SMALL_PADDING, settings.PADDING, settings.SMALL_PADDING));
        parks = new ArrayList<>();
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
     * Adds a park to the saved list.
     * 
     * @param park The park to add
     * @return true if the park was added, false if it was already in the list
     */
    public boolean addPark(Park park) {
        if (park == null) {
            return false;
        }
        
        // Check if park is already in the list
        for (Park p : parks) {
            if (p.parkCode().equals(park.parkCode())) {
                return false;
            }
        }
        
        // Add the park
        parks.add(park);
        parksListModel.addElement(DisplayParks.formatSavedParkListItem(park));
        return true;
    }

    /**
     * Removes the currently selected park from the list.
     * 
     * @return true if a park was removed, false if no park was selected
     */
    public boolean removeSelectedPark() {
        int selectedIndex = savedParksList.getSelectedIndex();
        if (selectedIndex >= 0) {
            parks.remove(selectedIndex);
            parksListModel.remove(selectedIndex);
            return true;
        }
        return false;
    }

    /**
     * Gets the currently selected park.
     * 
     * @return The selected Park object, or null if nothing is selected
     */
    public Park getSelectedPark() {
        int selectedIndex = savedParksList.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < parks.size()) {
            return parks.get(selectedIndex);
        }
        return null;
    }

    /**
     * Gets all saved parks.
     * 
     * @return List of saved Park objects
     */
    public List<Park> getSavedParks() {
        return new ArrayList<>(parks);
    }

    /**
     * Clears all parks from the list.
     */
    public void clearList() {
        parks.clear();
        parksListModel.clear();
    }
}

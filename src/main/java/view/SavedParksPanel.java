package view;
import javax.swing.*;
import java.awt.*;
import config.Settings;

/**
 * Panel for displaying and managing a list of saved parks.
 * Provides a scrollable text area with add/remove functionality.
 */
public class SavedParksPanel extends JPanel {
    /** Application settings */
    private final Settings settings = Settings.getInstance();
    /** Text area for the saved parks list */
    private JTextArea savedParksArea;
    /** Scroll container for the text area */
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
     * Sets up the title label and scrollable text area.
     */
    private void initializeComponents() {
        // Title label
        titleLabel = new JLabel("Saved Parks");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(settings.TITLE_FONT);
        
        // Saved parks text area
        savedParksArea = new JTextArea();
        savedParksArea.setEditable(false);
        savedParksArea.setLineWrap(true);
        savedParksArea.setWrapStyleWord(true);
        savedParksArea.setFont(settings.SAVED_LIST_FONT);
        
        // Scroll pane for text area
        scrollPane = new JScrollPane(savedParksArea);
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
     * @param parksList New list of parks to display
     */
    public void updateSavedParksList(String parksList) {
        savedParksArea.setText(parksList);
    }

    /**
     * Adds a single park to the end of the list.
     * 
     * @param parkName Name of the park to add
     */
    public void addParkToList(String parkName) {
        String currentText = savedParksArea.getText();
        savedParksArea.setText(currentText + "\n• " + parkName);
    }

    /**
     * Clears all parks from the list.
     */
    public void clearList() {
        savedParksArea.setText("");
    }

    /**
     * Gets the current list of saved parks as a string.
     * 
     * @return String containing all saved parks
     */
    public String getSavedParksList() {
        return savedParksArea.getText();
    }
}

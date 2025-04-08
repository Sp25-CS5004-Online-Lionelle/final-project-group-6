package view;

import config.Settings;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import model.Records.Park;
import model.ParkDisplayManager;
/**
 * Panel for displaying park search results text.
 * Contains a title header and scrollable list of parks.
 */
public class TextPanel extends JPanel {
    /** Settings for fonts, screen sizes, etc */
    private final Settings settings = Settings.getInstance();
    
    /** List for displaying park results */
    private JList<Park> resultsList;
    
    /** Custom cell renderer for park items */
    private ParkListCellRenderer cellRenderer;
    
    /** Scroll container for the list */
    private JScrollPane scrollPane;
    /** Panel for displaying single park details */
    private DetailViewPanel detailViewPanel;
    /** Panel using CardLayout to switch between list and detail views */
    private DisplayAreaPanel displayAreaPanel;
    
    /** Header label showing "Search Results" */
    private JLabel titleLabel;

    /** List of parks from the last successful update */
    private List<Park> currentParks;
    /** Currently selected park (from the summary list) */
    private Park selectedPark;
    /** Model containing the list of park summaries */
    private DefaultListModel<Park> summaryListModel;

    /**
     * Creates a new text panel with proper layout and padding.
     */
    public TextPanel() {
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(0, 
            settings.SMALL_PADDING, settings.PADDING, settings.SMALL_PADDING));
        initializeComponents();
    }

    /**
     * Sets up the title label and scrollable list.
     * Configures fonts, sizes, and adds components to the panel.
     */
    private void initializeComponents() {
        // Title label
        titleLabel = new JLabel("Search Results / Detailed View");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(settings.TITLE_FONT);

        // Results list with custom cell renderer
        resultsList = new JList<>();
        cellRenderer = new ParkListCellRenderer();
        resultsList.setCellRenderer(cellRenderer);
        resultsList.setFont(settings.CONTENT_FONT);
        resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsList.setVisibleRowCount(settings.TEXT_ROWS);
        
        // Important: Set fixed cell height to false to allow variable height cells
        resultsList.setFixedCellHeight(-1);
        // Set cell width to match the width of the list
        resultsList.setFixedCellWidth(-1);
        // Use this prototype value to help JList calculate initial sizes
        resultsList.setPrototypeCellValue(null);

        // Add selection listener to track selected park
        resultsList.addListSelectionListener(e -> {
            // Listener only relevant when summary list is visible
            if (e.getValueIsAdjusting()) {
                return;
            }
            
            int selectedIndex = resultsList.getSelectedIndex();
            ListModel<?> currentModel = resultsList.getModel(); 

            // Get the selected item directly from the model (which contains Park objects or null)
            if (selectedIndex >= 0 && selectedIndex < currentModel.getSize()) {
                selectedPark = (Park) currentModel.getElementAt(selectedIndex); // Cast needed
            } else {
                selectedPark = null;
            }
        });

        // Scroll pane
        scrollPane = new JScrollPane(resultsList);
        scrollPane.setPreferredSize(new Dimension(
            settings.MAIN_TEXT_WIDTH, settings.MAIN_TEXT_HEIGHT));
        scrollPane.setMinimumSize(new Dimension(
            settings.MIN_TEXT_WIDTH, settings.MIN_TEXT_HEIGHT));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Initialize detail view panel
        detailViewPanel = new DetailViewPanel();

        // Create the panel that manages the card layout, passing it the required components
        displayAreaPanel = new DisplayAreaPanel(scrollPane, detailViewPanel);

        // Add components to panel with padding
        add(titleLabel, BorderLayout.NORTH);
        
        // Create a wrapper panel for the display area with bottom padding
        JPanel displayWrapper = new JPanel(new BorderLayout());
        displayWrapper.add(displayAreaPanel, BorderLayout.CENTER); // Add display area panel
        displayWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 
            settings.PADDING, 0));
        
        add(displayWrapper, BorderLayout.CENTER);
    }

    /**
     * Updates the list displayed in the results area with condensed park information.
     * 
     * @param parks List of parks to display
     */
    public void updateResults(List<Park> parks) {
        // Update the list using the ParkDisplayManager and keep track of local state
        this.currentParks = parks;
        this.selectedPark = null;
        this.summaryListModel = ParkDisplayManager.updateParkList(
            parks,
            summaryListModel,
            resultsList,
            displayAreaPanel,
            scrollPane,
            () -> cellRenderer.reset()
        );
    }

    /**
     * Shows expanded details for the selected park in the list,
     * replacing the list content with only the details.
     */
    public void showSelectedParkDetails() {
        if (!ParkDisplayManager.showParkDetails(selectedPark, displayAreaPanel)) {
            System.err.println("showSelectedParkDetails called with null selectedPark");
        }
    }

    /**
     * Restores the display to the summary list view.
     * Should be called when navigating back from the detail view.
     */
    public void showSummaryListView() {
        // Ensure the summary model exists (although it should always exist now)
        if (summaryListModel == null) {
            updateResults(null); // Revert to empty state if summary model is missing
            System.err.println("Error: Summary list model was null when returning from detail view.");
            return;
        }

        // Use the ParkDisplayManager to restore the summary view
        ParkDisplayManager.restoreSummaryView(
            selectedPark,
            summaryListModel,
            resultsList,
            displayAreaPanel,
            scrollPane
        );
    }

    /**
     * Gets the currently selected park.
     * 
     * @return The selected park, or null if no park is selected or if "No parks" item is selected
     */
    public Park getSelectedPark() {
        return selectedPark;
    }
}
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import config.Settings;

/**
 * Panel containing buttons for all main application actions.
 * Provides a clean interface for attaching action listeners.
 */
public class ButtonPanel extends JPanel {
    /** Application settings */
    private final Settings settings = Settings.getInstance();
    
    /** Button for viewing all parks */
    private JButton viewAllButton;
    /** Button for showing a random park */
    private JButton randomButton;
    /** Button for saving results */
    private JButton saveButton;
    /** Button for loading saved lists */
    private JButton loadButton;
    /** Button for viewing park details */
    private JButton viewDetailButton;
    /** Button for adding a park to saved list */
    private JButton addParktoListButton;
    /** Button for removing a park from saved list */
    private JButton removeParkFromListButton;
    /** Button for filtering */
    private JButton filterButton;
    /** Button to go back from detail view */
    private JButton backButton;
    /**
     * Creates a new button panel with centered layout.
     */
    public ButtonPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        initializeComponents();
    }

    /**
     * Creates all buttons and adds them to the panel.
     */
    private void initializeComponents() {
        // Initialize all buttons with text from settings
        randomButton = createButton(settings.RANDOM_BUTTON_TEXT);
        saveButton = createButton(settings.SAVE_BUTTON_TEXT);
        loadButton = createButton(settings.LOAD_BUTTON_TEXT);
        viewDetailButton = createButton(settings.VIEW_DETAIL_BUTTON_TEXT);
        addParktoListButton = createButton(settings.ADD_TO_LIST_BUTTON_TEXT);
        filterButton = createButton(settings.FILTER_BUTTON_TEXT);
        removeParkFromListButton = createButton(settings.REMOVE_FROM_LIST_BUTTON_TEXT);
        backButton = createButton(settings.BACK_BUTTON_TEXT);
        backButton.setEnabled(false); // Initially disabled

        // Add all buttons to panel
        add(randomButton);
        add(saveButton);
        add(loadButton);
        add(viewDetailButton);
        add(addParktoListButton);
        add(filterButton);
        // add(removeParkFromListButton);
        add(backButton);
    }

    /**
     * Creates a button with consistent styling.
     *
     * @param text Button text
     * @return Configured button
     */
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(settings.CONTENT_FONT);
        return button;
    }

    /**
     * Adds listener for the "View All Parks" button.
     *
     * @param listener Action listener to add
     */
    public void addViewAllActionListener(ActionListener listener) {
        viewAllButton.addActionListener(listener);
    }

    /**
     * Adds listener for the "Random Park" button.
     *
     * @param listener Action listener to add
     */
    public void addRandomActionListener(ActionListener listener) {
        randomButton.addActionListener(listener);
    }

    /**
     * Adds listener for the "Save Results" button.
     *
     * @param listener Action listener to add
     */
    public void addSaveActionListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    /**
     * Adds listener for the "Open Existing List" button.
     *
     * @param listener Action listener to add
     */
    public void addLoadActionListener(ActionListener listener) {
        loadButton.addActionListener(listener);
    }

    /**
     * Adds listener for the "View Detail" button.
     *
     * @param listener Action listener to add
     */
    public void addViewDetailActionListener(ActionListener listener) {
        viewDetailButton.addActionListener(listener);
    }

    /**
     * Adds listener for the "Add Park to List" button.
     *
     * @param listener Action listener to add
     */
    public void addToListActionListener(ActionListener listener) {
        addParktoListButton.addActionListener(listener);
    }

    /**
     * Adds listener for the "Remove Park from List" button.
     *
     * @param listener Action listener to add
     */
    public void removeFromListActionListener(ActionListener listener) {
        removeParkFromListButton.addActionListener(listener);
    }

    /**
     * Adds listener for the "Filter Activities from List" button.
     * @param listener
     */
    public void addFilterActionListener(ActionListener listener) {
        filterButton.addActionListener(listener);
    }

    /**
     * Adds listener for the "Back" button.
     * @param listener Action listener to add
     */
    public void addBackActionListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    /**
     * Enables or disables the Back button.
     * @param enabled true to enable, false to disable
     */
    public void enableBackButton(boolean enabled) {
        backButton.setEnabled(enabled);
    }
}

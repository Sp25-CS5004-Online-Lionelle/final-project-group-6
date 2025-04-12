package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import config.Settings;

/**
 * Panel containing the search field and button.
 * Allows users to input search queries for parks.
 */
public class SearchPanel extends JPanel {
    /** Application settings. */
    private final Settings settings = Settings.getInstance();
    /** Text field for search input. */
    private JTextField searchField;
    /** Button to trigger search. */
    private JButton searchButton;

    /**
     * Creates a new search panel with centered layout.
     */
    public SearchPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        initializeComponents();
    }

    /**
     * Sets up the search label, input field, and button.
     */
    private void initializeComponents() {
        // Create and add search label
        JLabel searchLabel = new JLabel(settings.SEARCH_LABEL_TEXT);
        searchLabel.setFont(settings.CONTENT_FONT);
        
        // Create search field
        searchField = new JTextField(settings.SEARCH_FIELD_COLUMNS);
        
        // Create search button
        searchButton = new JButton(settings.SEARCH_BUTTON_TEXT);
        searchButton.setFont(settings.CONTENT_FONT);

        // Add components to panel
        add(searchLabel);
        add(searchField);
        add(searchButton);
    }

    /**
     * Gets the current search text, trimmed of whitespace.
     * 
     * @return Current search query
     */
    public String getSearchQuery() {
        return searchField.getText().trim();
    }

    /**
     * Adds listener for the search button.
     * 
     * @param listener Action listener to add
     */
    public void addSearchListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }
}

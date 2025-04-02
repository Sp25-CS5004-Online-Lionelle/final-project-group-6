package view;

import config.Settings;
import javax.swing.*;
import java.awt.*;

/**
 * Panel for displaying park search results text.
 * Contains a title header and scrollable text area.
 */
public class TextPanel extends JPanel {
    /** Settings for fonts, screen sizes, etc */
    private final Settings settings = Settings.getInstance();
    
    /** Text area for displaying results */
    private JTextArea resultsArea;
    
    /** Scroll container for the text area */
    private JScrollPane scrollPane;
    
    /** Header label showing "Search Results" */
    private JLabel titleLabel;

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
     * Sets up the title label and scrollable text area.
     * Configures fonts, sizes, and adds components to the panel.
     */
    private void initializeComponents() {
        // Title label
        titleLabel = new JLabel("Search Results");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(settings.TITLE_FONT);

        // Results area
        resultsArea = new JTextArea(settings.TEXT_ROWS, settings.TEXT_COLUMNS);
        resultsArea.setEditable(false);
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);
        resultsArea.setFont(settings.CONTENT_FONT);

        // Scroll pane
        scrollPane = new JScrollPane(resultsArea);
        scrollPane.setPreferredSize(new Dimension(
            settings.MAIN_TEXT_WIDTH, settings.MAIN_TEXT_HEIGHT));
        scrollPane.setMinimumSize(new Dimension(
            settings.MIN_TEXT_WIDTH, settings.MIN_TEXT_HEIGHT));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Add components to panel with padding
        add(titleLabel, BorderLayout.NORTH);
        
        // Create a wrapper panel for the scroll pane with bottom padding
        JPanel scrollWrapper = new JPanel(new BorderLayout());
        scrollWrapper.add(scrollPane, BorderLayout.CENTER);
        scrollWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 
            settings.PADDING, 0));
        
        add(scrollWrapper, BorderLayout.CENTER);
    }

    /**
     * Updates the text displayed in the results area.
     * 
     * @param text The new text to display
     */
    public void updateResults(String text) {
        resultsArea.setText(text);
    }
}
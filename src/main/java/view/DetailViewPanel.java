package view;

import config.Settings;
import model.DisplayParks;
import model.Records.Park;

import javax.swing.*;
import java.awt.*;

/**
 * A panel dedicated to displaying the detailed information of a single park.
 */
public class DetailViewPanel extends JPanel {

    private final Settings settings = Settings.getInstance();
    private JTextArea detailTextArea;
    private JScrollPane scrollPane;
    private JPanel contentPanel;

    public DetailViewPanel() {
        setLayout(new BorderLayout());
        
        // Create a content panel with enhanced margins
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(Color.WHITE);
        
        // Setup text area with improved formatting
        detailTextArea = new JTextArea();
        detailTextArea.setLineWrap(true);
        detailTextArea.setWrapStyleWord(true);
        detailTextArea.setEditable(false);
        detailTextArea.setFont(settings.CONTENT_FONT);
        // Increased padding inside the text area
        detailTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        // Make text area background match the panel
        detailTextArea.setBackground(contentPanel.getBackground());
        
        // Add text area to the content panel
        contentPanel.add(detailTextArea, BorderLayout.CENTER);
        
        // Create a scroll pane with the content panel
        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setPreferredSize(new Dimension(
            settings.MAIN_TEXT_WIDTH, settings.MAIN_TEXT_HEIGHT));
        scrollPane.setMinimumSize(new Dimension(
            settings.MIN_TEXT_WIDTH, settings.MIN_TEXT_HEIGHT));
        // Apply a subtle border to the scroll pane
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200))); 
        
        // Set scrolling behavior
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smoother scrolling
        
        // Add to main panel
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Updates the text area with the formatted details of the given park.
     * Scrolls the view to the top.
     * 
     * @param park The park whose details are to be displayed.
     */
    public void setParkDetails(Park park) {
        if (park != null) {
            // Set title based on park name
            detailTextArea.setText(DisplayParks.formatParkDetails(park));
            
        } else {
            // Handle case where a null park is somehow passed
            detailTextArea.setText("No details available.");
        }
        // Scroll to top after setting text
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0)); 
    }
} 
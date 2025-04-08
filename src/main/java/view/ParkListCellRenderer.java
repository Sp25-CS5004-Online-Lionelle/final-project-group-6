package view;

import config.Settings;
import model.DisplayParks;
import model.Records.Park;

import javax.swing.*;
import java.awt.*;

/**
 * Custom ListCellRenderer for displaying Park objects in a JList.
 * Uses a JTextArea to handle multi-line text and formatting.
 * All text formatting is delegated to DisplayParks class.
 */
public class ParkListCellRenderer extends JPanel implements ListCellRenderer<Park> {

    private final Settings settings = Settings.getInstance();
    private final JTextArea textArea;
    private final JSeparator separator;

    public ParkListCellRenderer() {
        setLayout(new BorderLayout(0, 15)); // Add gap between components
        
        // Create text area for park information
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false); // Make transparent to show panel background
        textArea.setFont(settings.CONTENT_FONT);
        textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Generous padding
        
        // Create separator
        separator = new JSeparator();
        separator.setForeground(Color.GRAY);
        
        // Add components
        add(textArea, BorderLayout.CENTER);
        add(separator, BorderLayout.SOUTH);
        
        // Set panel properties
        setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        setOpaque(true);
    }
    
    /**
     * Resets the renderer to its initial state.
     */
    public void reset() {
        textArea.setText("");
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Park> list, Park park,
            int index, boolean isSelected, boolean cellHasFocus) {

        // Clear the text area
        textArea.setText("");
        
        // Handle null case (No parks found)
        if (park == null) {
            textArea.setText("No parks found.");
            separator.setVisible(false);
            
        } else {
            // Format the park info
            String parkInfo = DisplayParks.formatBasicParkInfo(park);
            textArea.setText(parkInfo);
            
            // Only show separator if not the last item
            ListModel<?> model = list.getModel();
            separator.setVisible(index < model.getSize() - 1);
        }
        
        // Set background/foreground colors based on selection
        Color bg = isSelected ? list.getSelectionBackground() : list.getBackground();
        Color fg = isSelected ? list.getSelectionForeground() : list.getForeground();
        
        setBackground(bg);
        textArea.setForeground(fg);
        
        return this;
    }
} 
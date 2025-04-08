package model;

import java.util.List;
import model.Records.Park;
import view.DetailViewPanel;
import view.DisplayAreaPanel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.JScrollPane;

/**
 * Manages the display logic for parks in the application.
 * Handles the transition between summary views and detail views.
 * This class is part of the model layer that supports the view,
 * extracting display logic from the view classes to better follow MVC principles.
 */
public class ParkDisplayManager {
    
    /**
     * Updates the display when a new list of parks is available.
     * 
     * @param parks List of parks to display
     * @param listModel Model to populate with parks
     * @param parkList The JList component to update
     * @param displayAreaPanel The card layout panel to control view switching
     * @param scrollPane The scroll pane to reset
     * @param resetCallback Function to reset any renderers or other UI elements
     * @return The updated model
     */
    public static DefaultListModel<Park> updateParkList(
            List<Park> parks, 
            DefaultListModel<Park> listModel,
            JList<Park> parkList,
            DisplayAreaPanel displayAreaPanel,
            JScrollPane scrollPane,
            Runnable resetCallback) {
        
        // Create a fresh model
        DefaultListModel<Park> newModel = new DefaultListModel<>();
        
        // Populate the model with parks (or null for empty)
        if (parks == null || parks.isEmpty()) {
            newModel.addElement(null); // "No parks found" indicator
        } else {
            for (Park park : parks) {
                newModel.addElement(park);
            }
        }
        
        // Call any reset logic
        if (resetCallback != null) {
            resetCallback.run();
        }
        
        // Ensure we're in summary view mode
        displayAreaPanel.showSummaryView();
        
        // Update the list with the new model and reset selection
        parkList.setModel(newModel);
        parkList.clearSelection();
        
        // Schedule UI updates
        SwingUtilities.invokeLater(() -> {
            // Reset scroll position to top
            scrollPane.getVerticalScrollBar().setValue(0);
            // Force complete repaint
            displayAreaPanel.invalidate();
            displayAreaPanel.validate();
            displayAreaPanel.repaint();
        });
        
        return newModel;
    }
    
    /**
     * Shows detailed view of a selected park.
     * 
     * @param selectedPark The park to display in detail
     * @param displayAreaPanel The panel that controls view switching
     * @return true if the park was displayed, false if the park was null
     */
    public static boolean showParkDetails(Park selectedPark, DisplayAreaPanel displayAreaPanel) {
        if (selectedPark == null) {
            return false;
        }
        
        // Update the detail view panel and switch to detail view
        displayAreaPanel.showDetailView(selectedPark);
        return true;
    }
    
    /**
     * Restores the summary list view and tries to re-select the previously selected park.
     * 
     * @param selectedPark The currently selected park
     * @param summaryListModel The model containing all parks
     * @param parkList The JList component to update
     * @param displayAreaPanel The card layout panel to control view switching
     * @param scrollPane The scroll pane to reset
     */
    public static void restoreSummaryView(
            Park selectedPark, 
            DefaultListModel<Park> summaryListModel,
            JList<Park> parkList,
            DisplayAreaPanel displayAreaPanel,
            JScrollPane scrollPane) {
        
        // Tell the display area to switch back to the summary view
        displayAreaPanel.showSummaryView();
        
        // Try to re-select the park that was previously selected in the model
        if (selectedPark != null) { 
            // Find the selectedPark within the summaryListModel
            int foundIndex = -1;
            for (int i = 0; i < summaryListModel.getSize(); i++) {
                if (selectedPark.equals(summaryListModel.getElementAt(i))) {
                    foundIndex = i;
                    break;
                }
            }
            final int indexToSelect = foundIndex; // Final variable for lambda
 
            if (indexToSelect >= 0 && indexToSelect < summaryListModel.getSize()) {
                parkList.setSelectedIndex(indexToSelect);
                // Scroll to the selection (needs to be done after layout potentially)
                SwingUtilities.invokeLater(() -> parkList.ensureIndexIsVisible(indexToSelect));
            } else {
                parkList.setSelectedIndex(-1); // Clear selection if park not found 
                SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0)); // Scroll list to top
            }
        } else {
            parkList.setSelectedIndex(-1); // Clear selection if none was selected
            SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0)); // Scroll list to top
        }
    }
} 
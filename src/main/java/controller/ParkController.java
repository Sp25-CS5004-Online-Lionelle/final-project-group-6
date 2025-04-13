package controller;

import model.ParksModel;
import model.Records.Park;
import view.IView;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;

public final class ParkController implements IController {
    private final ParksModel model;
    private final IView view;
    private final Random random;

    public ParkController(ParksModel model, IView view) {
        this.model = model;
        this.view = view;
        this.random = new Random();
    }

    @Override
    public List<ActionListener> initActionListeners() {
        List<ActionListener> listeners = new ArrayList<>();

        // Search button listener
        ActionListener searchListener = e -> {
            String query = view.getSearchPanel().getSearchQuery();
            handleSearch(query);
        };
        view.getSearchPanel().addSearchListener(searchListener);
        listeners.add(searchListener);

        // Random park listener
        ActionListener randomListener = e -> handleRandomPark();
        view.getButtonPanel().addRandomActionListener(randomListener);
        listeners.add(randomListener);

        // Filter listener
        ActionListener filterListener = e -> handleFilter();
        view.getButtonPanel().addFilterActionListener(filterListener);
        listeners.add(filterListener);

        // View details listener
        ActionListener viewDetailsListener = e -> handleViewDetails();
        view.getButtonPanel().addViewDetailActionListener(viewDetailsListener);
        listeners.add(viewDetailsListener);

        // Save results listener
        ActionListener saveListener = e -> saveParksToFile("src/main/resources/userSavedParks.json");
        //{
            //try {
            //    saveParksToFile("file path");
            //} catch (UnsupportedOperationException ex) {
            //    view.getTextPanel().updateResults(List.of());
            //}
        //};
        view.getButtonPanel().addSaveActionListener(saveListener);
        listeners.add(saveListener);

        // Load saved list listener
        ActionListener loadListener = e -> handleOpenExistingList();
        view.getButtonPanel().addLoadActionListener(loadListener);
        listeners.add(loadListener);

        // Back button listener
        ActionListener backListener = e -> handleBack();
        view.getButtonPanel().addBackActionListener(backListener);
        listeners.add(backListener);

        // Add park to list listener
        ActionListener addListener = e -> handleAddPark();
        view.getButtonPanel().addToListActionListener(addListener);
        listeners.add(addListener);

        // Remove park from list listener
        ActionListener removeListener = e -> handleRemovePark();
        view.getButtonPanel().addRemoveFromListActionListener(removeListener);
        listeners.add(removeListener);

        return listeners;
    }

    @Override
    public void runApp() {
        // Initialize the view
        view.initializeFrame();
    }

    private void handleSearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            view.getTextPanel().updateResults(List.of());
            view.getButtonPanel().enableBackButton(false);
            return;
        }

        // Store response in model
        boolean success = model.updateDB(query);
        if (!success) {
            view.getTextPanel().updateResults(List.of());
            view.getButtonPanel().enableBackButton(false);
            return;
        }
        // Update the text panel with formatted results
        view.getTextPanel().updateResults(model.getParkList());
        view.getButtonPanel().enableBackButton(false);
    }

    private void handleViewAll() {
        boolean success = model.updateDB("ALL");
        if (!success) {
            view.getTextPanel().updateResults(List.of());
            view.getButtonPanel().enableBackButton(false);
            return;
        }
        view.getTextPanel().updateResults(model.getParkList());
        view.getButtonPanel().enableBackButton(false);
    }

    private void handleRandomPark() {
        if (model.getParkList().isEmpty()) {
            boolean success = model.updateDB("ALL");
            if (!success) {
                view.getTextPanel().updateResults(List.of());
                view.getButtonPanel().enableBackButton(false);
                return;
            }
        }

        if (!model.getParkList().isEmpty()) {
            Park randomPark = model.getParkList().get(random.nextInt(model.getParkList().size()));
            view.getTextPanel().updateResults(List.of(randomPark));
            view.getButtonPanel().enableBackButton(false);
        } else {
            view.getTextPanel().updateResults(List.of());
            view.getButtonPanel().enableBackButton(false);
        }
    }

    private void handleFilter() {
        if (model.getParkList().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please perform a search first to load parks.",
                    "No Parks Loaded", JOptionPane.INFORMATION_MESSAGE);
            view.getTextPanel().updateResults(List.of());
            view.getButtonPanel().enableBackButton(false);
            return;
        } else {
            List<String> selectedActivities = view.promptActivities(model.getActivityList());
            if (selectedActivities == null) {
                return;
            }
            view.getTextPanel().updateResults(model.getFilteredParks(selectedActivities));
            view.getButtonPanel().enableBackButton(false);
        }
    }

    /**
     * Handles viewing details of the selected park.
     * Shows expanded information in the same list area.
     */
    private void handleViewDetails() {
        // Try to get selected park from main search results first
        Park mainPark = view.getTextPanel().getSelectedPark();
        
        // If no park selected in main results, try saved parks list
        final Park selectedPark = (mainPark != null) ? mainPark : view.getSavedParksPanel().getSelectedPark();
        
        // If still no park selected, show warning
        if (selectedPark == null) {
            JOptionPane.showMessageDialog(null, "Please select a park first.",
                    "No Park Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Show the details and update images
        view.getTextPanel().showSelectedParkDetails();
        view.showLoadingWhileTask(new Runnable() {
            @Override
            public void run() {
                view.getImagePanel().updateImages(model.downloadImages(selectedPark, 3));
            }
        });
        view.getButtonPanel().enableBackButton(true);
    }

    /**
     * Saves the current list of parks to a file.
     * 
     * @param filePath Path to save the file
     * @throws UnsupportedOperationException if the method is not yet implemented
     */
    private void saveParksToFile(String filePath) {
        // Get the current search results from the model.
        try {
            // If the list is empty/null, show message and stop.
            List<Park> currentResults = model.getParkList();
            if (currentResults == null || currentResults.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No search results to save.", "Info", JOptionPane.INFORMATION_MESSAGE );
                return;
            }
            // Load parks that are already saved in the file.
            List<Park> existingParks = model.loadUserSavedParksFromFile();
            // If there is nothing loaded/file is empty, starts a new list.
            if (existingParks == null) {
                existingParks = new ArrayList<>();
            }
            // Checks if the parks already saved in the saved list (based on park code).
            for (Park park : currentResults) {
                boolean alreadyExists = false;
                for (Park existing : existingParks) {
                    if (existing.parkCode().equalsIgnoreCase(park.parkCode())) {
                        // If match found it won't add it again to the list.
                        alreadyExists = true;
                        break;
                    }
                }
                // If the park wasn't in the list, it will add it to the saved parks.
                if (!alreadyExists) {
                    existingParks.add(park);
                }
            }

            // Convert the final list to a JSON string.
            String updatedJson = ParksModel.serializeList(existingParks);
            // Write that json string to the file at the given path. 
            try (FileWriter writer = new FileWriter(filePath)) {
                // Writes content into file.
                writer.write(updatedJson);
            }
            // Lets user know that the save was successful.
            JOptionPane.showMessageDialog(null, "Search Results added to userSavedParka.json", "Sucess", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            // If something went wrong, error message will appear.
            JOptionPane.showMessageDialog(null, "Failed to save results:" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            // Print full error message for debugging.
            e.printStackTrace();
        }
    }

    /**
     * Loads a list of parks from the local file.
     */
    private void handleOpenExistingList() {
        // Load the user saved park list from database
        List<Park> userSavedParks = this.model.loadUserSavedParksFromFile();
        // Display the user saved park list in the text panel
        view.getTextPanel().updateResults(userSavedParks);
        view.getButtonPanel().enableBackButton(false);
    }

    /**
     * Handles returning from the detail view to the summary list.
     */
    private void handleBack() {
        view.getTextPanel().showSummaryListView();
        view.getButtonPanel().enableBackButton(false);
    }

    /**
     * Handles removing a park from the saved list.
     */
    private void handleRemovePark() {
        if (!view.getSavedParksPanel().removeSelectedPark()) {
            JOptionPane.showMessageDialog(null, "Please select a park to remove.",
                    "No Park Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Handles adding the currently selected park to the saved list.
     */
    private void handleAddPark() {
        Park selectedPark = view.getTextPanel().getSelectedPark();
        if (selectedPark == null) {
            JOptionPane.showMessageDialog(null, "Please select a park to add.",
                    "No Park Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!view.getSavedParksPanel().addPark(selectedPark)) {
            JOptionPane.showMessageDialog(null, "This park is already in your saved list.",
                    "Park Already Saved", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
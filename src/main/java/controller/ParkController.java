package controller;

import model.ParksModel;
import model.Records.Park;
import view.IView;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.DisplayParks;
import model.NetUtils;

public final class ParkController implements IController {
    private final ParksModel model;
    private final IView view;
    private List<Park> currentParks; // Stores current search results
    private final Random random;

    public ParkController(ParksModel model, IView view) {
        this.model = model;
        this.view = view;
        this.currentParks = new ArrayList<>();
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

        // View all parks listener
        ActionListener viewAllListener = e -> handleViewAll();
        view.getButtonPanel().addViewAllActionListener(viewAllListener);
        listeners.add(viewAllListener);

        // Random park listener
        ActionListener randomListener = e -> handleRandomPark();
        view.getButtonPanel().addRandomActionListener(randomListener);
        listeners.add(randomListener);

        // Save results listener
        ActionListener saveListener = e -> {
            try {
                saveParksToFile("file path");
            } catch (UnsupportedOperationException ex) {
                view.getTextPanel().updateResults("Save operation not yet implemented.");
            }
        };
        view.getButtonPanel().addSaveActionListener(saveListener);
        listeners.add(saveListener);

        // Load saved list listener
        ActionListener loadListener = e -> {
            try {
                loadParksFromFile("file path");
            } catch (UnsupportedOperationException ex) {
                view.getTextPanel().updateResults("Load operation not yet implemented.");
            }
        };
        view.getButtonPanel().addLoadActionListener(loadListener);
        listeners.add(loadListener);

        return listeners;
    }

    @Override
    public void runApp() {
        // Initialize the view
        view.initializeFrame();
    }

    private void handleSearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            view.getTextPanel().updateResults("Please enter a valid state code or zip code");
            return;
        }

        query = query.trim().toUpperCase();
        String response;

        // Get response from API based on input type
        if (query.matches("\\d{5}")) {
            response = NetUtils.getParksByZip(query);
        } else {
            response = NetUtils.getParksByState(query);
        }

        // Parse JSON response into Park records
        currentParks = ParksModel.deserializeResponse(response);
        
        if (currentParks == null || currentParks.isEmpty()) {
            view.getTextPanel().updateResults("No parks found for: " + query);
            return;
        }

        // Update the text panel with formatted results using DisplayParks
        view.getTextPanel().updateResults(DisplayParks.formatParksForDisplay(currentParks));
    }

    private void handleViewAll() {
        String response = NetUtils.getParksByState("ALL");
        currentParks = ParksModel.deserializeResponse(response);
        view.getTextPanel().updateResults(DisplayParks.formatParksForDisplay(currentParks));
    }

    private void handleRandomPark() {
        if (currentParks.isEmpty()) {
            handleViewAll(); // Load all parks first if no parks are loaded
        }
        
        if (!currentParks.isEmpty()) {
            Park randomPark = currentParks.get(random.nextInt(currentParks.size()));
            view.getTextPanel().updateResults(DisplayParks.formatParksForDisplay(List.of(randomPark)));
        }
    }

    /**
     * Saves the current list of parks to a file.
     * 
     * @param filePath Path to save the file
     * @throws UnsupportedOperationException if the method is not yet implemented
     */
    private void saveParksToFile(String filePath) {
        throw new UnsupportedOperationException("Method saveParksToFile() not yet implemented");
    }

    /**
     * Loads a list of parks from a file.
     * 
     * @param filePath Path to load the file from
     * @throws UnsupportedOperationException if the method is not yet implemented
     */
    private void loadParksFromFile(String filePath) {
        throw new UnsupportedOperationException("Method loadParksFromFile() not yet implemented");
    }
}
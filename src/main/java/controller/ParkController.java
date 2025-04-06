package controller;

import model.ParksModel;
import model.Records.Park;
import view.IView;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.DisplayParks;

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

        ActionListener filterListener = e -> handleFilter();;
        view.getButtonPanel().addFilterActionListener(filterListener);
        listeners.add(filterListener);

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

        // Store response in model
        boolean success = model.updateDB(query);
        if (!success) {
            view.getTextPanel().updateResults("No parks found for: " + query);
            return;
        }
        // Update the text panel with formatted results using DisplayParks
        view.getTextPanel().updateResults(DisplayParks.formatParksForDisplay(model.getParkList()));
    }

    private void handleViewAll() {
        boolean success = model.updateDB("ALL");
        if (!success) {
            view.getTextPanel().updateResults("No parks found for: ALL") ;
            return;
        }
        view.getTextPanel().updateResults(DisplayParks.formatParksForDisplay(model.getParkList()));
    }

    private void handleRandomPark() {
        if (model.getParkList().isEmpty()) {
            handleViewAll(); // Load all parks first if no parks are loaded
        }
        
        if (!model.getParkList().isEmpty()) {
            Park randomPark = model.getParkList().get(random.nextInt(model.getParkList().size()));
            view.getTextPanel().updateResults(DisplayParks.formatParksForDisplay(List.of(randomPark)));
        }
    }

    private void handleFilter() {
        if (model.getParkList().isEmpty()) {
            view.getTextPanel().updateResults("No parks available to filter. Please perform a search first.");
            return;
        } else {
            List<String> selectedActivities = view.promptActivities(model.getActivityList());
            view.getTextPanel().updateResults(DisplayParks.formatParksForDisplay(model.getFilteredParks(selectedActivities)));
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
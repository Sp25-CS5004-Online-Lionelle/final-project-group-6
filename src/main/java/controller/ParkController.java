package controller;

import model.ParksModel;
import model.Records.Park;
import view.IView;
import java.awt.event.ActionListener;
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
        ActionListener saveListener = e -> {
            try {
                saveParksToFile("file path");
            } catch (UnsupportedOperationException ex) {
                view.getTextPanel().updateResults(List.of());
            }
        };
        view.getButtonPanel().addSaveActionListener(saveListener);
        listeners.add(saveListener);

        // Load saved list listener
        ActionListener loadListener = e -> {
            try {
                loadParksFromFile("file path");
            } catch (UnsupportedOperationException ex) {
                view.getTextPanel().updateResults(List.of());
            }
        };
        view.getButtonPanel().addLoadActionListener(loadListener);
        listeners.add(loadListener);

        // Back button listener
        ActionListener backListener = e -> handleBack();
        view.getButtonPanel().addBackActionListener(backListener);
        listeners.add(backListener);

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
        if (view.getTextPanel().getSelectedPark() == null) {
            JOptionPane.showMessageDialog(null, "Please select a park first.", 
                "No Park Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Park selectedPark = view.getTextPanel().getSelectedPark();
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

    /**
     * Handles returning from the detail view to the summary list.
     */
    private void handleBack() {
        view.getTextPanel().showSummaryListView();
        view.getButtonPanel().enableBackButton(false);
    }
}
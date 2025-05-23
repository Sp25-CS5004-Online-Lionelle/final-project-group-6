package controller;

import model.Records.Park;
import model.FileManager;
import view.IView;
import model.IModel;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import model.NetUtils;

public final class ParkController implements IController {

    /** Instance of the parks model used for handling data. */
    private final IModel model;

    /** Instance of the parks view. */
    private final IView view;

    /**
     * Constructor for Park controller.
     * 
     * @param model
     * @param view
     */
    public ParkController(IModel model, IView view) {
        this.model = model;
        this.view = view;

        //load preexisting data
        List<Park> savedParks = FileManager.loadSavedParks();
        if (savedParks != null) {
            view.getSavedParksPanel().updateSavedList(savedParks);
        }
        FileManager.updateSavedList(view.getSavedParksPanel().getSavedParks());
    }

    /**
     * Initializes action listeners, passes them to the view.
     */
    @Override
    public void initActionListeners() {

        // Search button listener
        ActionListener searchListener = e -> {
            String query = view.getSearchPanel().getSearchQuery();
            view.showLoadingWhileTask(new Runnable() {
                @Override
                public void run() {
                    handleSearch(query);
                }
            });
        };
        view.getSearchPanel().addSearchListener(searchListener);

        // Random park listener
        ActionListener randomListener = e -> handleRandomPark();
        view.getButtonPanel().addRandomActionListener(randomListener);

        // Filter listener
        ActionListener filterListener = e -> handleFilter();
        view.getButtonPanel().addFilterActionListener(filterListener);

        // View details listener
        ActionListener viewDetailsListener = e -> handleViewDetails();
        view.getButtonPanel().addViewDetailActionListener(viewDetailsListener);

        // Save results listener
        ActionListener saveListener = e ->
            handleSaveResults();

        view.getButtonPanel().addSaveActionListener(saveListener);

        // Load saved list listener
        ActionListener loadListener = e -> handleOpenExistingList();
        view.getButtonPanel().addLoadActionListener(loadListener);

        // Back button listener
        ActionListener backListener = e -> handleBack();
        view.getButtonPanel().addBackActionListener(backListener);

        // Add park to list listener
        ActionListener addListener = e -> handleAddPark();
        view.getButtonPanel().addToListActionListener(addListener);

        // Remove park from list listener
        ActionListener removeListener = e -> handleRemovePark();
        view.getButtonPanel().addRemoveFromListActionListener(removeListener);
    }

    /**
     * Handles the event of searching for parks based on the query, user clicked search button.
     * @param query
     */
    private void handleSearch(String query) {
        view.getButtonPanel().enableBackButton(false);
        if (query == null || query.trim().isEmpty()) {
            view.getTextPanel().updateResults(List.of());
            return;
        }
        // Store response in model, update UI
        if (!model.updateDB(query)) {
            view.getTextPanel().updateResults(List.of());
            return;
        }
        // Update the text panel with formatted results
        view.getTextPanel().updateResults(model.getParkList());
    }

    /**
     * Randomly selects a park from all national parks.
     */
    private void handleRandomPark() {
        boolean success = model.getRandomPark();
        if (!success) {
            view.getTextPanel().updateResults(List.of());
            view.getButtonPanel().enableBackButton(false);
            return;
        }
        
        if (!model.getParkList().isEmpty()) {
            // We should only have one park in the list
            Park randomPark = model.getParkList().get(0);
            view.getTextPanel().updateResults(List.of(randomPark));
            view.getButtonPanel().enableBackButton(false);
        } else {
            view.getTextPanel().updateResults(List.of());
            view.getButtonPanel().enableBackButton(false);
        }
    }

    /**
     * Handles filtering parks based on selected activities, user selected filter button.
     */
    private void handleFilter() {
        if (model.getParkList().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please perform a search first to filter parks.",
                    "No Parks Loaded", JOptionPane.INFORMATION_MESSAGE);
            view.getTextPanel().updateResults(List.of());
            view.getButtonPanel().enableBackButton(false);
            return;
        } else {
            List<String> selectedActivities = view.promptActivities(model.getActivityList());
            if (selectedActivities == null || selectedActivities.isEmpty()) {
                return; // Do nothing if nothing was selected
            }
            view.getTextPanel().updateResults(model.getFilteredParks(selectedActivities));
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
                view.getImagePanel().updateImages(NetUtils.downloadImages(selectedPark, 3));
            }
        });
        view.getButtonPanel().enableBackButton(true);
    }


    /**
     * Loads a list of parks from the local file.
     */
    private void handleOpenExistingList() {

        List<String> selection = view.promptSaveAction("Which list do you want to open?");
        if (selection == null || selection.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nothing Saved.",
                    "No List Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selection.size() > 1) {
            JOptionPane.showMessageDialog(null, "Please select only one list to open.",
                    "No List Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selection.contains("Search Results")) { // display saved search results
            List<Park> savedSearch = FileManager.loadSavedSearch();
            view.getTextPanel().updateResults(savedSearch);
            model.updateDB(savedSearch);
        } else { // display saved park list
            List<Park> savedList = FileManager.loadSavedParks();
            view.getTextPanel().updateResults(savedList);
            model.updateDB(savedList);
        }
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
        } else {
            // sync the model's saved list with the view's saved parks
            FileManager.updateSavedList(view.getSavedParksPanel().getSavedParks());
        }
    }

    private void handleSaveResults() {
        if (FileManager.saveSearchToFile(model.getParkList())) {
            JOptionPane.showMessageDialog(null, 
            "Search Results added to userSavedParks.json", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to save results:", "Error", JOptionPane.ERROR_MESSAGE);
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
        } else {
            FileManager.updateSavedList(view.getSavedParksPanel().getSavedParks());
        }
    }
}

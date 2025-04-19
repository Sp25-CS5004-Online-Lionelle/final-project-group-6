package view;
import java.util.List;

/**
 * Interface representing the main view of the application.
 */
public interface IView {

    /**
     * Initializes and displays the main application window.
     */
    void initializeFrame();

    /**
     * Gets the search panel component.
     * @return the SearchPanel component
     */
    SearchPanel getSearchPanel();

    /**
     * Gets the button panel component.
     * @return the ButtonPanel component
     */
    ButtonPanel getButtonPanel();

    /**
     * Gets the text panel component.
     * @return the TextPanel component
     */
    TextPanel getTextPanel();

    /**
     * Gets the saved parks panel component.
     * @return the SavedParksPanel component
     */
    SavedParksPanel getSavedParksPanel();

    /**
     * Gets the image panel component.
     * @return the ImagePanel component
     */
    ImagePanel getImagePanel();

    /**
     * Receives a list of activities to prompt for.
     * JChooser or JComboBox for example.
     * @param activityList
     * @return the selected activities
     */
    List<String> promptActivities(List<String> activityList);

    /**
     * Prompts user to choose which save action(s) to take.
     * 
     * @param message
     * @return List of selected actions
     */
    List<String> promptSaveAction(String message);

    /**
     * Shows a loading indicator while a task is running.
     * @param task
     */
    void showLoadingWhileTask(Runnable task);
}

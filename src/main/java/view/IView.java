package view;

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
     */
    SearchPanel getSearchPanel();

    /**
     * Gets the button panel component.
     */
    ButtonPanel getButtonPanel();

    /**
     * Gets the text panel component.
     */
    TextPanel getTextPanel();

    /**
     * Gets the saved parks panel component.
     */
    SavedParksPanel getSavedParksPanel();

}
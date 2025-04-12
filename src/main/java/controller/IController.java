package controller;

/**
 * Interface for a controller that manages action listeners for interactive components.
 */
public interface IController {
    /**
     * Creates action listeners for all interactive components.
     * This includes search, view all, random park, and save/load functionality.
     */
    void initActionListeners();

}

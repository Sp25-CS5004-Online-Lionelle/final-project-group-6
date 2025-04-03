package controller;
import java.util.*;
import java.awt.event.ActionListener;

public interface IController {
    /**
     * Creates action listeners for all interactive components.
     * This includes search, view all, random park, and save/load functionality.
     * 
     * @return List of action listeners to be attached to view components
     */
    List<ActionListener> initActionListeners();

    /**
     * Initializes and starts the application.
     * Sets up the view, attaches listeners, and displays the main window.
     */
    void runApp();
}
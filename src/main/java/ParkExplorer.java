import controller.ParkController;
import controller.IController;
import model.ParksModel;
import view.*;
import javax.swing.SwingUtilities;

/**
 * Main entry point for the Parks Explorer application.
 * Initializes the MVC components and starts the application.
 */
public final class ParkExplorer {

    /**
     * Private constructor for utility class.
     */
    private ParkExplorer() {
    }
    
    /**
     * Main entry point for the program.
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Create MVC components
                ParksModel model = new ParksModel();
                IView view = new JFrameView();
                IController controller = new ParkController(model, view);
                
                // Initialize action listeners
                controller.initActionListeners();
                
                // Start the application
                view.initializeFrame();
                
            } catch (Exception e) {
                System.err.println("Error starting application: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}

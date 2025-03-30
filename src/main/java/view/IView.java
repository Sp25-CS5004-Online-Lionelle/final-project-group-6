package view;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Interface representing the view to be displayed
 */
public interface IView {

    /**
     * Sets the action listeners that are received from the controller.
     * @param listeners the action listeners to be set
     */
    public void setActionListeners(List<ActionListener> listeners);

    /**
     * Runs the app doing tasks including setting and displaying the frame.
     */
    public void initializeFrame();
}
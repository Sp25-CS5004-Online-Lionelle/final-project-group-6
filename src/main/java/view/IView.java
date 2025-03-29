package view;
import java.awt.event.ActionListener;
import java.util.List;

public interface IView {

    public void setActionListeners(List<ActionListener> listeners);
    public void runApp();
}
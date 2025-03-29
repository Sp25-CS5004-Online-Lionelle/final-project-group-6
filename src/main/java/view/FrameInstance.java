package view;
import javax.swing.JFrame;
import javax.swing.JComponent;

public class FrameInstance {

    /**
     * The main frame of the application.
     */
    private static JFrame frame;

    /**
     * The upper panel that holds the text box and buttons.
     */
    private static JComponent controlPanel;

    /**
     * The middle panel that holds the image gallery.
     */
    private static JComponent imageView;

    /**
     * The lower text panel that displays the text.
     */
    private static JComponent textView;

    public void setFrame(JFrame frame) {
        throw new UnsupportedOperationException("Unimplemented");
    }

    public void updateControlPanel(JComponent newComponent) {
        throw new UnsupportedOperationException("Unimplemented");
    }

    public void updateImageView(JComponent newComponent) {
        throw new UnsupportedOperationException("Unimplemented");
    }

    public void updateTextView(JComponent newComponent) {
        throw new UnsupportedOperationException("Unimplemented");
    }
}
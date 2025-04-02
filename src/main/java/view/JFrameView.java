package view;

import javax.swing.*;

import config.Settings;

import java.awt.*;

/**
 * The main frame of the Parks Explorer application, organizing all UI panels.
 * It implements the {@link IView} interface and serves as the primary window.
 * This class sets up the overall layout including the top bar (logo, search, buttons)
 * and the main content area (image gallery and text panels).
 */
public class JFrameView extends JFrame implements IView {
    /** Singleton instance of application settings. */
    private final Settings settings = Settings.getInstance();

    /** The main container panel using BorderLayout for overall structure. */
    private JPanel mainPanel;
    /** The panel holding the central content (image gallery and text areas) using GridBagLayout. */
    private JPanel contentPanel;
    /** Panel responsible for displaying park images. */
    private ImagePanel imageGalleryPanel;
    /** Panel containing the search input field and button. */
    private SearchPanel searchPanel;
    /** Panel displaying park search results. */
    private TextPanel textPanel;
    /** Panel containing various action buttons. */
    private ButtonPanel buttonPanel;
    /** A simple panel holding the application logo label. */
    private JPanel logoPanel;
    /** Panel displaying the list of saved parks. */
    private SavedParksPanel savedParksPanel;
    /** Split pane separating the results text panel and the saved parks panel. */
    private JSplitPane splitPane;
    /** Label used to display the application logo image or fallback text. */
    private JLabel logoLabel;

    /**
     * Constructs the main application frame.
     * Initializes frame properties, panels, and components.
     */
    public JFrameView() {
        setFrameProperties();
        initializePanels();
        initializeComponents();
    }

    /**
     * Configures the basic properties of the JFrame, such as title,
     * size (initial and minimum), default close operation, and centering on screen.
     * Reads dimension values from the {@link Settings}.
     */
    private void setFrameProperties() {
        setTitle("Parks Explorer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(settings.FRAME_WIDTH, settings.FRAME_HEIGHT);
        setMinimumSize(new Dimension(settings.MIN_FRAME_WIDTH, settings.MIN_FRAME_HEIGHT));
        setLocationRelativeTo(null);
    }

    /**
     * Creates instances of all the necessary JPanel containers used within the frame.
     * Sets their layout managers and borders where applicable.
     */
    private void initializePanels() {
        mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        contentPanel = new JPanel(new GridBagLayout());
        imageGalleryPanel = new ImagePanel();
        searchPanel = new SearchPanel();
        textPanel = new TextPanel();
        buttonPanel = new ButtonPanel();
        logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        savedParksPanel = new SavedParksPanel();
    }

    /**
     * Initializes the main UI components and arranges them within the panels.
     * Sets up the layout constraints for the image gallery and the text area container.
     * Creates the split pane for the results and saved parks lists.
     */
    private void initializeComponents() {
        initializeLogo();
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        
        // Add image gallery
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.4;
        contentPanel.add(imageGalleryPanel, gbc);
        
        // Create a panel to hold both text panels with padding
        JPanel textContainer = new JPanel(new BorderLayout(0, 0));
        textContainer.setBorder(BorderFactory.createEmptyBorder(0, 
            settings.PADDING, 0, settings.PADDING));
        
        // Create split pane with TextPanel and SavedParksPanel
        splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            textPanel,
            savedParksPanel
        );
        splitPane.setResizeWeight(settings.SPLIT_RATIO);
        splitPane.setDividerLocation(settings.SPLIT_RATIO);
        splitPane.setBorder(null);
        splitPane.setDividerSize(settings.DIVIDER_SIZE);
        
        textContainer.add(splitPane, BorderLayout.CENTER);
        
        gbc.gridy = 1;
        gbc.weighty = 0.6;
        contentPanel.add(textContainer, gbc);
    }

    /**
     * Initializes the logo label. Scales the image according to {@link Settings} and displays it.
     * If loading fails, displays fallback text "ParkExplorer" using the configured logo font.
     */
    private void initializeLogo() {
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/sampleLogo.png"));
            Image scaledImage = logoIcon.getImage().getScaledInstance(
                settings.LOGO_WIDTH, settings.LOGO_HEIGHT, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoPanel.add(logoLabel);
        } catch (Exception e) {
            logoLabel = new JLabel("ParkExplorer");
            logoLabel.setFont(settings.LOGO_FONT);
            logoPanel.add(logoLabel);
        }
    }

    /**
     * Assembles the final layout of the main frame and makes it visible.
     * This method should be called after the view object is fully constructed.
     * It arranges the top panel (logo, search, buttons) and the content panel
     * within the main panel, adds the main panel to the frame, and shows the window.
     */
    @Override
    public void initializeFrame() {
        // Assemble the main layout
        
        // Create a top panel for logo, search, and buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(logoPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the top panel and content panel to the main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add the fully assembled mainPanel to the frame
        add(mainPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * The main entry point for running the application directly from this view class.
     * Ensures the GUI is created and updated on the Event Dispatch Thread (EDT).
     * Creates an instance of {@link JFrameView} and calls {@link #initializeFrame()} to display it.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Ensure GUI updates happen on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create an instance of the view
                JFrameView view = new JFrameView();
                // Call the method to initialize and display the frame
                view.initializeFrame();
            }
        });
    }

    /**
     * Gets the search panel component.
     * 
     * @return The SearchPanel instance
     */
    public SearchPanel getSearchPanel() {
        return searchPanel;
    }

    /**
     * Gets the button panel component.
     * 
     * @return The ButtonPanel instance
     */
    public ButtonPanel getButtonPanel() {
        return buttonPanel;
    }

    /**
     * Gets the text panel component.
     * 
     * @return The TextPanel instance
     */
    public TextPanel getTextPanel() {
        return textPanel;
    }

    /**
     * Gets the saved parks panel component.
     * 
     * @return The SavedParksPanel instance
     */
    public SavedParksPanel getSavedParksPanel() {
        return savedParksPanel;
    }
}


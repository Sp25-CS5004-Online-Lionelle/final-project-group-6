package view;

import controller.IController;
import controller.MockController;
import model.Park;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class WireFrame extends JFrame {
    private JTextField searchField;
    private JTextArea resultsArea;
    private JPanel imageGalleryPanel;
    private JLabel[] galleryImages;
    private JLabel logoLabel;
    private JLabel pageCounter;
    private JPanel mainPanel;
    private JPanel searchPanel;
    private JPanel contentPanel;
    private JPanel buttonPanel;
    private JPanel logoPanel;
    private IController controller;
    private static final int TARGET_IMAGE_WIDTH = 900;
    private static final int TARGET_IMAGE_HEIGHT = 400;
    private static final int GALLERY_IMAGE_WIDTH = 280;
    private static final int GALLERY_IMAGE_HEIGHT = 210;
    
    public WireFrame() {
        controller = new MockController();
        setTitle("Parks Explorer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setMinimumSize(new Dimension(800, 700));
        setLocationRelativeTo(null);
        
        // Initialize panels
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        contentPanel = new JPanel(new GridBagLayout());
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        // Initialize image gallery with more spacing
        imageGalleryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        imageGalleryPanel.setPreferredSize(new Dimension(TARGET_IMAGE_WIDTH, TARGET_IMAGE_HEIGHT));
        imageGalleryPanel.setMinimumSize(new Dimension(400, 200));
        imageGalleryPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        // Create three image labels for the gallery
        galleryImages = new JLabel[3];
        for (int i = 0; i < 3; i++) {
            try {
                ImageIcon placeholderIcon = new ImageIcon(getClass().getResource("/placeholder.jpg"));
                Image scaledImage = scaleImage(placeholderIcon.getImage(), GALLERY_IMAGE_WIDTH, GALLERY_IMAGE_HEIGHT);
                galleryImages[i] = new JLabel(new ImageIcon(scaledImage));
            } catch (Exception e) {
                galleryImages[i] = new JLabel("Park Image " + (i + 1), SwingConstants.CENTER);
                galleryImages[i].setBackground(Color.WHITE);
                galleryImages[i].setOpaque(true);
            }
            galleryImages[i].setPreferredSize(new Dimension(GALLERY_IMAGE_WIDTH, GALLERY_IMAGE_HEIGHT));
            galleryImages[i].setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            imageGalleryPanel.add(galleryImages[i]);
        }
        
        // Logo
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/sampleLogo.png"));
            Image scaledImage = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoPanel.add(logoLabel);
        } catch (Exception e) {
            logoLabel = new JLabel("ParkExplorer");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
            logoPanel.add(logoLabel);
        }
        
        // Search components
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton viewAllButton = new JButton("View All Parks");
        JButton randomButton = new JButton("Random Park");
        JButton saveButton = new JButton("Save Results");
        JButton loadButton = new JButton("Load/Open Existing List");
        
        // Results area
        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);
        resultsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        scrollPane.setMinimumSize(new Dimension(400, 200));
        
        // Page counter
        pageCounter = new JLabel("Page 1 of 1");
        pageCounter.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Add components to panels
        searchPanel.add(new JLabel("Search by State/Zip:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        buttonPanel.add(viewAllButton);
        buttonPanel.add(randomButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        
        // Set up GridBagConstraints for content panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Add image gallery at the top
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.4;
        gbc.gridwidth = 2;
        contentPanel.add(imageGalleryPanel, gbc);
        
        // Add scroll pane below image gallery
        gbc.gridy = 1;
        gbc.weighty = 0.6;
        contentPanel.add(scrollPane, gbc);
        
        // Add page counter at the bottom
        gbc.gridy = 2;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(pageCounter, gbc);
        
        // Create a panel for search and buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(logoPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add panels to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listeners
        searchButton.addActionListener(e -> handleSearch());
        viewAllButton.addActionListener(e -> handleViewAll());
        randomButton.addActionListener(e -> handleRandom());
    }
    
    private void handleSearch() {
        String query = searchField.getText();
        MockController mockController = (MockController) controller; // Cast to MockController to access search method
        List<Park> results = mockController.searchParks(query);
        displayResults(results);
    }
    
    private void handleViewAll() {
        MockController mockController = (MockController) controller;
        List<Park> allParks = mockController.getAllParks();
        displayResults(allParks);
    }
    
    private void handleRandom() {
        MockController mockController = (MockController) controller;
        Park randomPark = mockController.getRandomPark();
        if (randomPark != null) {
            resultsArea.setText(randomPark.toString());
            updateParkImage(randomPark.getImageUrl());
        }
    }
    
    private void displayResults(List<Park> parks) {
        StringBuilder sb = new StringBuilder();
        for (Park park : parks) {
            sb.append(park.toString()).append("\n\n");
        }
        resultsArea.setText(sb.toString());
        pageCounter.setText("Page 1 of 1");
    }
    
    private void updateParkImage(String imageUrl) {
        try {
            // For now, just show the placeholder image in all three slots
            ImageIcon placeholderIcon = new ImageIcon(getClass().getResource("/placeholder2.jpg"));
            Image scaledImage = scaleImage(placeholderIcon.getImage(), GALLERY_IMAGE_WIDTH, GALLERY_IMAGE_HEIGHT);
            for (JLabel imageLabel : galleryImages) {
                imageLabel.setIcon(new ImageIcon(scaledImage));
            }
        } catch (Exception e) {
            for (JLabel imageLabel : galleryImages) {
                imageLabel.setIcon(null);
                imageLabel.setText("Image: " + imageUrl);
            }
        }
    }
    
    private Image scaleImage(Image originalImage, int targetWidth, int targetHeight) {
        // Get original dimensions
        int originalWidth = originalImage.getWidth(null);
        int originalHeight = originalImage.getHeight(null);
        
        // If original image is invalid, return it as is
        if (originalWidth <= 0 || originalHeight <= 0) {
            return originalImage;
        }
        
        // Calculate scaling factors for both dimensions
        double widthScale = (double) targetWidth / originalWidth;
        double heightScale = (double) targetHeight / originalHeight;
        
        // Use the smaller scaling factor to ensure image fits in both dimensions
        double scale = Math.min(widthScale, heightScale);
        
        // Calculate new dimensions
        int scaledWidth = (int) (originalWidth * scale);
        int scaledHeight = (int) (originalHeight * scale);
        
        // Create and return scaled image
        return originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WireFrame().setVisible(true);
        });
    }
}

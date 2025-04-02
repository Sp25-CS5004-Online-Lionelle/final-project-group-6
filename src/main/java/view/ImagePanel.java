package view;
import javax.swing.*;
import java.awt.*;
import config.Settings;

/**
 * Panel displaying a gallery of park images.
 * Shows three images side by side with placeholder support.
 */
public class ImagePanel extends JPanel {
    /** Application settings */
    private final Settings settings = Settings.getInstance();
    /** Array of image display labels */
    private JLabel[] galleryImages;

    /**
     * Creates a new image gallery panel with centered layout.
     */
    public ImagePanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 
            settings.SMALL_PADDING, settings.SMALL_PADDING));
        setPreferredSize(new Dimension(
            settings.TARGET_IMAGE_WIDTH, settings.TARGET_IMAGE_HEIGHT));
        setMinimumSize(new Dimension(
            settings.MIN_IMAGE_WIDTH, settings.MIN_IMAGE_HEIGHT));

        initializeGallery();
    }

    /**
     * Creates three image labels with placeholder images.
     */
    private void initializeGallery() {
        galleryImages = new JLabel[3];
        for (int i = 0; i < 3; i++) {
            try {
                ImageIcon placeholderIcon = new ImageIcon(getClass().getResource("/placeholder.jpg"));
                Image scaledImage = scaleImage(placeholderIcon.getImage(), 
                    settings.GALLERY_IMAGE_WIDTH, settings.GALLERY_IMAGE_HEIGHT);
                galleryImages[i] = new JLabel(new ImageIcon(scaledImage));
            } catch (Exception e) {
                galleryImages[i] = new JLabel("Park Image " + (i + 1), SwingConstants.CENTER);
                galleryImages[i].setBackground(Color.WHITE);
                galleryImages[i].setOpaque(true);
            }
            galleryImages[i].setPreferredSize(new Dimension(
                settings.GALLERY_IMAGE_WIDTH, settings.GALLERY_IMAGE_HEIGHT));
            add(galleryImages[i]);
        }
    }

    /**
     * Updates all gallery images with a new image.
     * Falls back to text if image loading fails.
     * 
     * @param imageUrl URL of the image to display
     */
    public void updateImages(String imageUrl) {
        try {
            ImageIcon placeholderIcon = new ImageIcon(getClass().getResource("/placeholder2.jpg"));
            Image scaledImage = scaleImage(placeholderIcon.getImage(), 
                settings.GALLERY_IMAGE_WIDTH, settings.GALLERY_IMAGE_HEIGHT);
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
    
    /**
     * Scales an image while maintaining aspect ratio.
     * 
     * @param originalImage Image to scale
     * @param targetWidth Desired width
     * @param targetHeight Desired height
     * @return Scaled image
     */
    private Image scaleImage(Image originalImage, int targetWidth, int targetHeight) {
        int originalWidth = originalImage.getWidth(null);
        int originalHeight = originalImage.getHeight(null);
        
        if (originalWidth <= 0 || originalHeight <= 0) {
            return originalImage;
        }
        
        double widthScale = (double) targetWidth / originalWidth;
        double heightScale = (double) targetHeight / originalHeight;
        double scale = Math.min(widthScale, heightScale);
        
        int scaledWidth = (int) (originalWidth * scale);
        int scaledHeight = (int) (originalHeight * scale);
        
        return originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
    }
}
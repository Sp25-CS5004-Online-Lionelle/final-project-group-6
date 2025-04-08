package view;
import javax.swing.*;
import java.awt.*;
import config.Settings;
import model.Records.Park;
import model.Records.ParkImage;
import java.util.List;

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
     * Updates the gallery with images from a park.
     * Falls back to placeholder if no images are available.
     * 
     * @param park The park whose images to display
     */
    public void updateImages(Park park) {
        List<ParkImage> images = park.images();
        if (images == null || images.isEmpty()) {
            // If no images, show placeholder
            try {
                ImageIcon placeholderIcon = new ImageIcon(getClass().getResource("/placeholder.jpg"));
                Image scaledImage = scaleImage(placeholderIcon.getImage(), 
                    settings.GALLERY_IMAGE_WIDTH, settings.GALLERY_IMAGE_HEIGHT);
                for (JLabel imageLabel : galleryImages) {
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                    imageLabel.setText("");
                }
            } catch (Exception e) {
                for (JLabel imageLabel : galleryImages) {
                    imageLabel.setIcon(null);
                    imageLabel.setText("No images available");
                }
            }
            return;
        }
        int numImages = Math.min(images.size(), galleryImages.length);
        for (int i = 0; i < numImages; i++) {
            try {
                ParkImage parkImage = images.get(i);
                ImageIcon icon = new ImageIcon(new java.net.URL(parkImage.url()));
                Image scaledImage = scaleImage(icon.getImage(), 
                    settings.GALLERY_IMAGE_WIDTH, settings.GALLERY_IMAGE_HEIGHT);
                galleryImages[i].setIcon(new ImageIcon(scaledImage));
                galleryImages[i].setText("");
            } catch (Exception e) {
                galleryImages[i].setIcon(null);
                galleryImages[i].setText("Failed to load image");
            }
        }

        // Clear any remaining slots
        for (int i = numImages; i < galleryImages.length; i++) {
            galleryImages[i].setIcon(null);
            galleryImages[i].setText("");
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
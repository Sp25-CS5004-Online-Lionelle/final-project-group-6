package config;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.awt.Font;
import java.io.InputStream;

/**
 * Manages application-wide settings loaded from config.xml using a Singleton
 * pattern.
 * Parses the XML configuration file to provide easy access to various settings
 * like frame dimensions, fonts, layout parameters, and component properties.
 */
public class Settings {
    private static Settings instance;
    private Document config;

    // --- Fields for Settings ---
    /** Frame width in pixels. */
    public final int FRAME_WIDTH;
    /** Frame height in pixels. */
    public final int FRAME_HEIGHT;
    /** Minimum frame width in pixels. */
    public final int MIN_FRAME_WIDTH;
    /** Minimum frame height in pixels. */
    public final int MIN_FRAME_HEIGHT;

    /** Target width for the image panel container. */
    public final int TARGET_IMAGE_WIDTH;
    /** Target height for the image panel container. */
    public final int TARGET_IMAGE_HEIGHT;
    /** Width for individual gallery images. */
    public final int GALLERY_IMAGE_WIDTH;
    /** Height for individual gallery images. */
    public final int GALLERY_IMAGE_HEIGHT;
    /** Minimum width for the image panel. */
    public final int MIN_IMAGE_WIDTH;
    /** Minimum height for the image panel. */
    public final int MIN_IMAGE_HEIGHT;

    /** Preferred width for the main results text area scroll pane. */
    public final int MAIN_TEXT_WIDTH;
    /** Preferred height for the main results text area scroll pane. */
    public final int MAIN_TEXT_HEIGHT;
    /** Preferred width for the saved parks text area scroll pane. */
    public final int SAVED_TEXT_WIDTH;
    /** Preferred height for the saved parks text area scroll pane. */
    public final int SAVED_TEXT_HEIGHT;
    /** Minimum width for text area scroll panes. */
    public final int MIN_TEXT_WIDTH;
    /** Minimum height for text area scroll panes. */
    public final int MIN_TEXT_HEIGHT;
    /** Preferred number of rows for text areas. */
    public final int TEXT_ROWS;
    /** Preferred number of columns for text areas. */
    public final int TEXT_COLUMNS;

    /** Font used for panel titles. */
    public final Font TITLE_FONT;
    /** Font used for general content text and buttons. */
    public final Font CONTENT_FONT;
    /** Font used for the saved parks list text area. */
    public final Font SAVED_LIST_FONT;
    /** Font used for the logo text fallback. */
    public final Font LOGO_FONT;

    /** Resize weight for the split pane divider (e.g., 0.7 for 70/30 split). */
    public final double SPLIT_RATIO;
    /** Size of the split pane divider in pixels. */
    public final int DIVIDER_SIZE;
    /** Standard padding size in pixels used around major components. */
    public final int PADDING;
    /** Smaller padding size in pixels used for internal component spacing. */
    public final int SMALL_PADDING;

    /** Width for the scaled logo image. */
    public final int LOGO_WIDTH;
    /** Height for the scaled logo image. */
    public final int LOGO_HEIGHT;

    /** Preferred number of columns for the search text field. */
    public final int SEARCH_FIELD_COLUMNS;
    /** Text for the search field label. */
    public final String SEARCH_LABEL_TEXT;
    /** Text for the search button. */
    public final String SEARCH_BUTTON_TEXT;

    /** Text for the 'View All Parks' button. */
    public final String VIEW_ALL_BUTTON_TEXT;
    /** Text for the 'Random Park' button. */
    public final String RANDOM_BUTTON_TEXT;
    /** Text for the 'Save Results' button. */
    public final String SAVE_BUTTON_TEXT;
    /** Text for the 'Open Existing List' button. */
    public final String LOAD_BUTTON_TEXT;
    /** Text for the 'View Detail' button. */
    public final String VIEW_DETAIL_BUTTON_TEXT;
    /** Text for the 'Add Park to List' button. */
    public final String ADD_TO_LIST_BUTTON_TEXT;
    /** Text for the 'Remove Park from List' button. */
    public final String REMOVE_FROM_LIST_BUTTON_TEXT;

    /**
     * Private constructor to prevent external instantiation (Singleton pattern).
     * Loads the configuration and initializes all final setting fields.
     * Throws a RuntimeException if the configuration cannot be loaded or parsed
     * correctly.
     */
    private Settings() {
        loadConfig(); // Load the XML document first

        Element root = config.getDocumentElement(); // Get the root <config> element
        // If loadConfig failed, root might be null or based on default - proceed
        // carefully or add checks

        // --- Initialize fields from the loaded config ---

        // Frame settings
        Element frame = (Element) root.getElementsByTagName("frame").item(0);
        FRAME_WIDTH = getIntValue(frame, "width");
        FRAME_HEIGHT = getIntValue(frame, "height");
        MIN_FRAME_WIDTH = getIntValue(frame, "min_width");
        MIN_FRAME_HEIGHT = getIntValue(frame, "min_height");

        // Image panel settings
        Element imagePanel = (Element) root.getElementsByTagName("image_panel").item(0);
        TARGET_IMAGE_WIDTH = getIntValue(imagePanel, "target_width");
        TARGET_IMAGE_HEIGHT = getIntValue(imagePanel, "target_height");
        GALLERY_IMAGE_WIDTH = getIntValue(imagePanel, "gallery_width");
        GALLERY_IMAGE_HEIGHT = getIntValue(imagePanel, "gallery_height");
        MIN_IMAGE_WIDTH = getIntValue(imagePanel, "min_width");
        MIN_IMAGE_HEIGHT = getIntValue(imagePanel, "min_height");

        // Text areas settings
        Element textAreas = (Element) root.getElementsByTagName("text_areas").item(0);
        MAIN_TEXT_WIDTH = getIntValue(textAreas, "main_width");
        MAIN_TEXT_HEIGHT = getIntValue(textAreas, "main_height");
        SAVED_TEXT_WIDTH = getIntValue(textAreas, "saved_width");
        SAVED_TEXT_HEIGHT = getIntValue(textAreas, "saved_height");
        MIN_TEXT_WIDTH = getIntValue(textAreas, "min_width");
        MIN_TEXT_HEIGHT = getIntValue(textAreas, "min_height");
        TEXT_ROWS = getIntValue(textAreas, "rows");
        TEXT_COLUMNS = getIntValue(textAreas, "columns");

        // Font settings
        Element fonts = (Element) root.getElementsByTagName("fonts").item(0);
        TITLE_FONT = createFont((Element) fonts.getElementsByTagName("title").item(0));
        CONTENT_FONT = createFont((Element) fonts.getElementsByTagName("content").item(0));
        SAVED_LIST_FONT = createFont((Element) fonts.getElementsByTagName("saved_list").item(0));
        Element logoFontElement = (Element) fonts.getElementsByTagName("logoFont").item(0);
        if (logoFontElement == null) {
            throw new RuntimeException("Configuration Error: Missing <logoFont> tag inside <fonts>");
        }
        LOGO_FONT = createFont(logoFontElement);

        // Layout settings
        Element layout = (Element) root.getElementsByTagName("layout").item(0);
        SPLIT_RATIO = Double.parseDouble(getTextContent(layout, "split_ratio"));
        DIVIDER_SIZE = getIntValue(layout, "divider_size");
        PADDING = getIntValue(layout, "padding");
        SMALL_PADDING = getIntValue(layout, "small_padding");

        // Logo dimension settings
        Element logoDimElement = (Element) root.getElementsByTagName("logo").item(0);
        if (logoDimElement == null) {
            throw new RuntimeException(
                    "Configuration Error: Missing <logo> tag (for dimensions) directly under <config>");
        }
        LOGO_WIDTH = getIntValue(logoDimElement, "width");
        LOGO_HEIGHT = getIntValue(logoDimElement, "height");

        // Search panel settings
        Element searchPanel = (Element) root.getElementsByTagName("search_panel").item(0);
        SEARCH_FIELD_COLUMNS = getIntValue(searchPanel, "field_columns");
        SEARCH_LABEL_TEXT = getTextContent(searchPanel, "label_text");
        SEARCH_BUTTON_TEXT = getTextContent(searchPanel, "button_text");

        // Button panel text
        Element buttonPanel = (Element) root.getElementsByTagName("button_panel").item(0);
        Element buttons = (Element) buttonPanel.getElementsByTagName("buttons").item(0);
        VIEW_ALL_BUTTON_TEXT = getTextContent(buttons, "view_all");
        RANDOM_BUTTON_TEXT = getTextContent(buttons, "random");
        SAVE_BUTTON_TEXT = getTextContent(buttons, "save");
        LOAD_BUTTON_TEXT = getTextContent(buttons, "load");
        VIEW_DETAIL_BUTTON_TEXT = getTextContent(buttons, "view_detail");
        ADD_TO_LIST_BUTTON_TEXT = getTextContent(buttons, "add_to_list");
        REMOVE_FROM_LIST_BUTTON_TEXT = getTextContent(buttons, "remove_from_list");
    }

    /**
     * Returns the singleton instance of the Settings class.
     * Creates the instance on the first call.
     *
     * @return The single instance of Settings.
     */
    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    /**
     * Loads the config.xml file from the classpath (expected in the view package).
     * Parses the XML file into a Document object stored in the 'config' field.
     * Throws a RuntimeException if the file cannot be found or parsed.
     */
    private void loadConfig() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Load config.xml from the same package as Settings.class
            InputStream is = getClass().getResourceAsStream("/config.xml");
            if (is == null) {
                // Consider reverting to useDefaultValues() or throwing a more specific error
                throw new RuntimeException(
                        "Could not find config.xml in view package. Ensure it's in src/main/java/view/");
            }
            config = builder.parse(is);
        } catch (Exception e) {
            // Wrap parsing/IO exceptions
            throw new RuntimeException("Failed to load or parse config.xml: " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to create a default configuration document in memory.
     * This is currently unused but can be called from loadConfig on failure.
     */
    private void useDefaultValues() {
        // This method is kept for potential future use but is not called by default
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            config = builder.newDocument();
            Element root = config.createElement("config");
            config.appendChild(root);

            // Example: Add default frame settings
            Element frame = config.createElement("frame");
            root.appendChild(frame);
            addElement(frame, "width", "1000");
            addElement(frame, "height", "800");
            addElement(frame, "min_width", "800");
            addElement(frame, "min_height", "700");

            // Example: Add default logo settings
            Element logo = config.createElement("logo");
            root.appendChild(logo);
            addElement(logo, "width", "300");
            addElement(logo, "height", "100");

            // Add other default settings here...

        } catch (Exception e) {
            // If even default creation fails, something is seriously wrong
            throw new RuntimeException("Failed to create default configuration", e);
        }
    }

    /**
     * Helper method to add a new XML element with text content to a parent element.
     * Used by useDefaultValues.
     *
     * @param parent The parent XML element.
     * @param name   The tag name of the new element.
     * @param value  The text content for the new element.
     */
    private void addElement(Element parent, String name, String value) {
        Element element = config.createElement(name);
        element.setTextContent(value);
        parent.appendChild(element);
    }

    /**
     * Retrieves the text content of the first element with the specified tag name
     * within the given parent element and parses it as an integer.
     *
     * @param element The parent XML element.
     * @param tagName The tag name of the child element containing the integer
     *                value.
     * @return The integer value parsed from the child element's text content.
     * @throws RuntimeException if the tag is not found or the content is not a
     *                          valid integer.
     */
    private int getIntValue(Element element, String tagName) {
        // Assumes getTextContent handles basic null checks or throws appropriately
        return Integer.parseInt(getTextContent(element, tagName));
    }

    /**
     * Retrieves the text content of the first element with the specified tag name
     * within the given parent element.
     *
     * @param element The parent XML element.
     * @param tagName The tag name of the child element whose text content is
     *                needed.
     * @return The text content of the found child element.
     * @throws RuntimeException if the element or the tag is not found.
     */
    private String getTextContent(Element element, String tagName) {
        // Basic check - relies on caller ensuring element is not null
        var nodeList = element.getElementsByTagName(tagName);
        // Simplified check - assumes item(0) exists if nodeList is not empty
        if (nodeList == null || nodeList.getLength() == 0) {
            throw new RuntimeException("Configuration Error: Could not find required tag <" + tagName
                    + "> inside element <" + element.getTagName() + ">");
        }
        return nodeList.item(0).getTextContent();
    }

    /**
     * Creates a Font object based on the name, style, and size specified
     * within the provided XML font element.
     *
     * @param fontElement The XML element containing <name>, <style>, and <size>
     *                    tags.
     * @return A new Font object configured according to the XML element.
     * @throws RuntimeException if required tags are missing or values are invalid.
     */
    private Font createFont(Element fontElement) {
        // Assumes fontElement is not null (checked by caller)
        String name = getTextContent(fontElement, "name");
        String style = getTextContent(fontElement, "style");
        int size = getIntValue(fontElement, "size");

        // Determine font style constant
        int fontStyle = Font.PLAIN; // Default to PLAIN
        if ("BOLD".equalsIgnoreCase(style)) {
            fontStyle = Font.BOLD;
        } else if ("ITALIC".equalsIgnoreCase(style)) {
            fontStyle = Font.ITALIC;
        } else if ("BOLDITALIC".equalsIgnoreCase(style) || "BOLD_ITALIC".equalsIgnoreCase(style)) {
            fontStyle = Font.BOLD | Font.ITALIC;
        }

        return new Font(name, fontStyle, size);
    }
}

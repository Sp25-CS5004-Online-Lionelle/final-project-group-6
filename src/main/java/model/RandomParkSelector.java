package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class responsible for selecting random parks from a list of park codes.
 * Follows Single Responsibility Principle by handling only random park selection logic.
 */
public class RandomParkSelector {
    /** Path to the file containing all national park codes. */
    private static final String PARK_CODES_FILE = "data/ParkCodes.txt";

    /** Random number generator for selecting random parks. */
    private final Random random;

    /** List of all national park codes. */
    private List<String> parkCodes;

    /**
     * Constructor initializes the random number generator and loads park codes.
     */
    public RandomParkSelector() {
        this.random = new Random();
        this.parkCodes = new ArrayList<>();
        loadParkCodes();
    }

    /**
     * Constructor with dependency injection for testing.
     * @param random Random number generator
     * @param parkCodesPath Path to park codes file
     */
    public RandomParkSelector(Random random, String parkCodesPath) {
        this.random = random;
        this.parkCodes = new ArrayList<>();
        loadParkCodes(parkCodesPath);
    }

    /**
     * Loads the list of park codes from the default file location.
     */
    private void loadParkCodes() {
        loadParkCodes(PARK_CODES_FILE);
    }

    /**
     * Loads the list of park codes from the specified file.
     * Only loads codes that are exactly 4 characters long (standard NPS format).
     * @param filePath path to the park codes file
     */
    private void loadParkCodes(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Path.of(filePath));
            for (String line : lines) {
                String code = line.trim();
                // Only add 4-character codes which is the standard format for National Parks
                if (code.length() == 4) {
                    parkCodes.add(code);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load park codes from file: " + e.getMessage());
        }
    }

    /**
     * Gets a random park code from the loaded list.
     * @return a random park code, or null if no codes are loaded
     */
    public String getRandomParkCode() {
        if (parkCodes == null || parkCodes.isEmpty()) {
            System.err.println(DisplayParks.formatBasicParkInfo(null));
            return null;
        }
        return parkCodes.get(random.nextInt(parkCodes.size()));
    }

    /**
     * Gets the list of loaded park codes.
     * @return list of park codes
     */
    public List<String> getParkCodes() {
        return new ArrayList<>(parkCodes);
    }
} 

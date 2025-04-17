package model;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import javax.swing.ImageIcon;
import io.github.cdimascio.dotenv.Dotenv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Records.ActivityWrapper;
import model.Records.Park;

public final class NetUtils {

    /**
     * Base URL for the NPS API parks endpoint with placeholders for state code and
     * API key.
     * Format:
     * https://developer.nps.gov/api/v1/parks?stateCode={state}&limit=20&api_key={key}
     */
    private static final String NPS_API_BASE_URL = "https://developer.nps.gov/api/v1/parks?stateCode=%s&limit=20&api_key=%s";

    /**
     * Base URL for the NPS API parks endpoint to get a specific park by parkCode.
     * Format:
     * https://developer.nps.gov/api/v1/parks?parkCode={parkCode}&api_key={key}
     */
    private static final String NPS_API_PARK_BY_CODE_URL = "https://developer.nps.gov/api/v1/parks?parkCode=%s&api_key=%s";

    /**
     * Dotenv instance to load environment variables from .env file.
     */
    private static final Dotenv dotenv = Dotenv.load();

    /**
     * NPS API key loaded from .env file using the NPS_API_KEY environment variable.
     */
    private static final String API_KEY = dotenv.get("NPS_API_KEY");

    /**
     * Prevent instantiation.
     */
    private NetUtils() {
    }

    /**
     * Gets parks for a specific state code.
     * 
     * @param stateCode two-letter state code (e.g., "WA", "CA")
     * @return JSON response as string
     */
    public static String getParksByState(String stateCode) {
        String url = String.format(NPS_API_BASE_URL, stateCode, API_KEY);
        try {
            InputStream response = getUrlContents(url);
            return new String(response.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Error reading response: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets the contents of a URL as an InputStream.
     *
     * @param urlStr the URL to get the contents of
     * @return the contents of the URL as an InputStream, or the null InputStream if
     *         the connection
     *         fails
     */
    private static InputStream getUrlContents(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setRequestProperty("accept", "application/json");

            int status = con.getResponseCode();
            if (status == 200) {
                return con.getInputStream();
            } else {
                System.err.println("Failed to connect to " + urlStr + ". Status: " + status);
            }
        } catch (Exception ex) {
            System.err.println("Failed to connect to " + urlStr + ": " + ex.getMessage());
        }
        return InputStream.nullInputStream();
    }

    /**
     * Get a deserialized list of activities from the API, there are currently 40
     * total.
     * Can be used later for filtering, and/or a dropdown in the UI.
     * 
     * @return a String list of activities
     */
    public static List<String> getListOfActivities() throws JsonProcessingException, IOException {

        String activitiesBaseUrl = "https://developer.nps.gov/api/v1/activities?limit=50&api_key=%s";

        InputStream response = getUrlContents(String.format(activitiesBaseUrl, API_KEY));
        String json = new String(response.readAllBytes(), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        ActivityWrapper wrapper = objectMapper.readValue(json, ActivityWrapper.class);

        return wrapper
                .data()
                .stream()
                .map(activity -> activity.name())
                .toList();

    }

    /**
     * Format a request to the API from a Zip code.
     * 
     * @param zip to search api
     * @return returns the formatted url
     */
    public static String getParksByZip(String zip) {
        String stateCode = ZipConverter.convertZipToState(zip);
        if (stateCode == null) {
            return null;
        }
        return getParksByState(stateCode);
    }

    /**
     * Gets a specific park by its park code.
     * 
     * @param parkCode the unique code for the park (e.g., "YELL", "GRCA")
     * @return JSON response as string
     */
    public static String getParkByParkCode(String parkCode) {
        String url = String.format(NPS_API_PARK_BY_CODE_URL, parkCode, API_KEY);
        try {
            InputStream response = getUrlContents(url);
            return new String(response.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Error reading response: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets parks by their name.
     * 
     * @param parkName the name of the park to search for
     * @return JSON response as a string
     */
    public static String getParksByName(String parkName) {
        // Base URL for searching parks by name
        String parksByNameUrl = "https://developer.nps.gov/api/v1/parks?q=%s&limit=20&api_key=%s";
        String url = String.format(parksByNameUrl, parkName.replace(" ", "%20"), API_KEY);

        try {
            InputStream response = getUrlContents(url);
            return new String(response.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Error reading response for park name: " + e.getMessage());
            return null;
        }
    }

    /**
     * Download images from the url field of a park object.
     * Uses URL object to download the images.
     * 
     * @param park Park object containing image URLs
     * @param numImages Maximum number of images to download
     * @return List of ImageIcon objects
     */
    public static List<ImageIcon> downloadImages(Park park, int numImages) {
        if (park == null || park.images() == null || park.images().isEmpty()) {
            System.err.println("No images available for park");
            return new ArrayList<>();
        }

        List<String> urls = park.images()
                .stream()
                .map(img -> img.url())
                .toList();

        List<ImageIcon> icons = new ArrayList<>();

        for (int i = 0; i < numImages; i++) {
            if (i >= urls.size()) {
                break;
            }
            try {
                ImageIcon icon = new ImageIcon(new java.net.URL(urls.get(i)));
                icons.add(icon);
            } catch (Exception e) {
                System.err.println("Failed to load image: " + e.getMessage());
            }
        }
        return icons;
    }
}

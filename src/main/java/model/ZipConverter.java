package model;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

/* 
 * Converts a standard US zipcode into two letter state code
 * 
 * By utiliizng Zippopotam.us api,
 * 
 * see documentation: https://zippopotam.us/
 */
public final class ZipConverter {
    /**
     * URL format required for the API request.
     */
    private static final String ZIP_CODE_API_BASE_URL = "https://api.zippopotam.us/us/%s";
    /**
     * Jackson object mapper to read json data and extract state code
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    /* Prevent instantiation. */
    private ZipConverter() {
    }

    /**
     * Converts a US zip code to its corresponding two-letter state code using Jackson for JSON parsing
     * @param zipCode the US postal code to convert
     * @return two-letter state abbreviation (e.g., "CA", "NY") or null if conversion fails
     */
    public static String convertZipToState(String zipCode) {
        try {
            String urlStr = String.format(ZIP_CODE_API_BASE_URL, zipCode);
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            
            if (conn.getResponseCode() != 200) {
                System.err.println("Failed to get data for zip code: " + zipCode);
                return null;
            }

            // Parse JSON response using Jackson
            JsonNode root = mapper.readTree(conn.getInputStream());
            JsonNode places = root.get("places");
            
            if (places != null && places.isArray() && places.size() > 0) {
                return places.get(0).get("state abbreviation").asText();
            }
            
            return null;
            
        } catch (IOException e) {
            System.err.println("Error converting zip code: " + e.getMessage());
            return null;
        }
    }

}

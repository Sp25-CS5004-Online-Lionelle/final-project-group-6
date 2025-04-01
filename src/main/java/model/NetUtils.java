package model;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import io.github.cdimascio.dotenv.Dotenv;

public class NetUtils {
    /**
     * Base URL for the NPS API parks endpoint with placeholders for state code and API key.
     * Format: https://developer.nps.gov/api/v1/parks?stateCode={state}&limit=20&api_key={key}
     */
    private static final String NPS_API_BASE_URL = "https://developer.nps.gov/api/v1/parks?stateCode=%s&limit=20&api_key=%s";

    /**
     * Dotenv instance to load environment variables from .env file
     */
    private static final Dotenv dotenv = Dotenv.load();

    /**
     * NPS API key loaded from .env file using the NPS_API_KEY environment variable
     */
    private static final String API_KEY = dotenv.get("NPS_API_KEY");

    /**
     * Prevent instantiation.
     */
    private NetUtils() {
        // Prevent instantiation
    }

    /**
     * Gets parks for a specific state code
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
     * @return the contents of the URL as an InputStream, or the null InputStream if the connection
     *         fails
     *
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
     * Format a request to the API from a Zip code.
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

    // public static void main(String[] args) {
    //     String response = getParksByState("WA");
    //     String responseZip = getParksByZip("98034");
    //     System.out.println("Zip code search: " + responseZip);
    //     System.out.println("Response: " + response);
    // }
}
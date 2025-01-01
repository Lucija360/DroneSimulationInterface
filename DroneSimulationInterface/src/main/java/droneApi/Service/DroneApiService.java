package droneApi.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import java.io.InputStream;


import java.util.ArrayList;
import java.util.List;
import droneApi.Entities.Drone;

@Service
public class DroneApiService {

	 // Logger for logging application messages
    private static final Logger logger = LoggerFactory.getLogger(DroneApiService.class);
    
    // Base URL for the Drone API loaded from configuration file
    private final String apiUrl = loadApiUrl();
    
    // RestTemplate for making HTTP requests
    private final RestTemplate restTemplate = new RestTemplate();
    
    // Authorization token loaded from configuration file
    private final String token = loadApiToken();
    
    /**
     * Loads the API base URL from a configuration file.
     * @return the API base URL as a string.
     */
    private String loadApiUrl() {
        try {
            // Load the file as a classpath resource
            InputStream inputStream = new ClassPathResource("config.json").getInputStream();
            String content = new String(inputStream.readAllBytes());

            // Parse the JSON content
            JSONObject config = new JSONObject(content);
            return config.getString("api_url");
        } catch (Exception ex) {
            logger.error("Failed to load API URL from config file.", ex);
            throw new RuntimeException("API URL loading failed", ex);
        }
    }

    
    /**
     * Loads the API token from a configuration file.
     * @return the API token as a string.
     */
    private String loadApiToken() {
        try {
            // Load the file as a classpath resource
            InputStream inputStream = new ClassPathResource("config.json").getInputStream();
            String content = new String(inputStream.readAllBytes());

            // Parse the JSON content
            JSONObject config = new JSONObject(content);
            return "Token " + config.getString("api_token");
        } catch (Exception ex) {
            logger.error("Failed to load API token from config file.", ex);
            throw new RuntimeException("Token loading failed", ex);
        }
    }
    
    
    /**
     * Verifies server accessibility.
     * @return a success message if the server is accessible.
     */
    public String verifyServerAccessibility() {
        logger.trace("Entered verifyServerAccessibility method.");
        try {
            String url = apiUrl;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                logger.trace("Server is accessible.");
                return "Server is accessible";
            } else {
                throw new RuntimeException("Server returned unexpected status: " + response.getStatusCode());
            }
        } catch (Exception ex) {
            logger.error("Failed to verify server accessibility.", ex);
            throw new RuntimeException("Failed to verify server accessibility.", ex);
        }
    }
    
    /**
     * Fetches a list of drones from the Drone API.
     * @return a list of Drone objects.
     */   
    public List<Drone> fetchDrones() {
        logger.trace("Entered fetchDrones method.");
        
        // Initialize an empty list to store drones
        List<Drone> drones = new ArrayList<>();

        try {
        	// Construct the request URL
        	 String url = apiUrl + "drones/?format=json";
            
            // Create and set HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            headers.set("User-Agent", "JavaDroneApp");

            // Perform the HTTP GET request
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            // Check if the response status is OK
            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                var droneArray = jsonResponse.getJSONArray("results");

                // Loop through each drone in the response array
                for (int i = 0; i < droneArray.length(); i++) {
                    var obj = droneArray.getJSONObject(i);
                    
                    // Create a Drone object and populate its fields
                    Drone drone = new Drone(
                        obj.getString("serial_number"),
                        obj.getString("model"),
                        obj.getDouble("speed"),
                        obj.getDouble("battery_status"),
                        obj.getString("status")
                    );
                    
                    // Add the drone to the list
                    drones.add(drone);
                }
                logger.trace("fetchDrones completed successfully.");
            }
        } catch (HttpClientErrorException ex) {
        	
        	// Log HTTP-specific errors and rethrow the exception
            logger.error("HTTP Error: {}", ex.getStatusCode(), ex);
            throw ex;
        } catch (Exception ex) {
        	
        	// Log unexpected errors and rethrow as a runtime exception
            logger.error("Unexpected Error: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to fetch drones", ex);
        }
        
        // Return the list of fetched drones
        return drones;
    }
    
    /**
     * Placeholder method to fetch drone types.
     * TODO: Implement this method to fetch drone types from the API.
     */
    public List<String> fetchDroneTypes() {
        logger.warn("fetchDroneTypes is not implemented yet.");
        return new ArrayList<>();
    }

    /**
     * Placeholder method to fetch drone dynamics.
     * TODO: Implement this method to fetch drone dynamics from the API.
     */
    public List<String> fetchDroneDynamics() {
        logger.warn("fetchDroneDynamics is not implemented yet.");
        return new ArrayList<>();
    }
    
    
}
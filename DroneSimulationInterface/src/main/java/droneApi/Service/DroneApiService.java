package droneApi.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import java.io.InputStream;


import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import java.util.ArrayList;
import java.util.List;
import droneApi.Entities.Drone;
import droneApi.Entities.DroneDynamics;
import droneApi.Entities.DroneType;

@Service
public class DroneApiService {

	// Logger for logging application messages
    private static final Logger logger = LoggerFactory.getLogger(DroneApiService.class);
    
    // Base URL for the Drone API loaded from configuration file
    private final String apiUrl = loadApiUrl();
    
    // RestTemplate for making HTTP requests
    private final RestTemplate restTemplate = createRestTemplate();
    
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
            return config.getString("api_token"); 
        } catch (Exception ex) {
            logger.error("Failed to load API token from config file.", ex);
            throw new RuntimeException("Token loading failed", ex);
        }
    }
    
    /**
     * Creates a RestTemplate that bypasses SSL validation.
     */
    private RestTemplate createRestTemplate() {
        try {
            // Create an SSLContext that trusts all certificates
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }}, new SecureRandom());

            // Set the SSLContext to the default HttpsURLConnection
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            // Disable hostname verification
            HostnameVerifier allowAllHosts = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allowAllHosts);

            // Return RestTemplate
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            return new RestTemplate(requestFactory);
        } catch (Exception ex) {
            logger.error("Failed to create RestTemplate with bypassed SSL.", ex);
            throw new RuntimeException("Failed to create RestTemplate", ex);
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
    public List<Drone> fetchDrones(int limit, int offset) {
    	logger.trace("Entered fetchDrones method with limit={} and offset={}", limit, offset);

        
        // Initialize an empty list to store drones
        List<Drone> drones = new ArrayList<>();
        // Construct the request URL
        String url = apiUrl + "drones/?format=json&limit=" + limit + "&offset=" + offset;


        try {
        	
        	// Dynamically load the API token
            String token = loadApiToken(); // Load the token from config.json
        	
            // Create and set HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token); // Use Token-based authentication
            headers.set("Accept", "application/json");
            headers.set("User-Agent", "JavaDroneApp"); // Include a user agent for better API identification


            // Create the entity with headers
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Perform the HTTP GET request
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            
            // Check if the response status is OK
            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                var droneArray = jsonResponse.getJSONArray("results");

                for (int i = 0; i < droneArray.length(); i++) {
                    var obj = droneArray.getJSONObject(i);

                    // Create a Drone object and populate its fields
                    Drone drone = new Drone(
                        obj.getInt("id"),
                        obj.getString("dronetype"),
                        obj.getString("created"),
                        obj.getString("serialnumber"),
                        obj.getInt("carriage_weight"),
                        obj.getString("carriage_type")
                    );

                    // Add the drone to the list
                    drones.add(drone);
                }
                // Handle pagination if "next" field is not null
                String nextUrl = jsonResponse.optString("next", null);
                if (nextUrl != null && !nextUrl.isEmpty()) {
                    logger.debug("Fetching next page of drones from URL: {}", nextUrl);
                    drones.addAll(fetchDronesFromNextPage(nextUrl));
                }

                logger.trace("fetchDrones completed successfully.");
            } else {
                logger.error("Unexpected response status: {}", response.getStatusCode());
            }
        } catch (HttpClientErrorException ex) {
            logger.error("HTTP Error: Status {}, Body: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to fetch drones", ex);
        }

        return drones;
    }
    
    
    /**
     * Recursively fetches drone types from the next page URL.
     */
    private List<Drone> fetchDronesFromNextPage(String nextUrl) {
        List<Drone> drones = new ArrayList<>();
        try {
        	 // Dynamically load the API token
            String token = loadApiToken(); // Load the token from config.json

            // Create and set HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token); // Use Token-based authentication
            headers.set("Accept", "application/json");
            headers.set("User-Agent", "JavaDroneApp"); // Include a user agent for better API identification

            logger.debug("Requesting next page of drones from URL: {}", nextUrl);
            // Create the entity with headers
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(nextUrl, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                var droneArray = jsonResponse.getJSONArray("results");

                for (int i = 0; i < droneArray.length(); i++) {
                    var obj = droneArray.getJSONObject(i);

                    Drone drone = new Drone(
                        obj.getInt("id"),
                        obj.getString("dronetype"), // URL for dronetype, handle as needed
                        obj.getString("created"),
                        obj.getString("serialnumber"),
                        obj.getInt("carriage_weight"),
                        obj.getString("carriage_type")
                    );
                    drones.add(drone);
                }

                // Recursive call if "next" field exists
                String next = jsonResponse.optString("next", null);
                if (next != null && !next.isEmpty()) {
                    drones.addAll(fetchDronesFromNextPage(next));
                }
            }
        } catch (HttpClientErrorException ex) {
            logger.error("HTTP Error: Status {}, Body: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to fetch drones from next page", ex);
        }
        return drones;
    }
    
    
    /**
     * Fetches a list of drone types from the Drone API.
     * @return a list of Drone type objects.
     * NOTE : Method and endpoint are functional -> data retrieval works
     */   
    public List<DroneType> fetchDroneTypes(int limit, int offset) {
        logger.trace("Entered fetchDroneTypes method.");
        
        // Construct the request URL
        List<DroneType> droneTypes = new ArrayList<>();
        
        // Construct the request URL
        String url = apiUrl + "dronetypes/?format=json&limit=" + limit + "&offset=" + offset;
        
        try {
        	// Load the token dynamically using the loadApiToken method
            String token = loadApiToken(); // Dynamically loads the token
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token); // Use Token-based authentication
            headers.set("User-Agent", "JavaDroneApp");
            headers.set("Accept", "application/json");

         // Create the entity with headers
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Perform the HTTP GET request
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // Check if the response status is OK
            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                var typesArray = jsonResponse.getJSONArray("results");

                // Loop through each drone in the response array
                for (int i = 0; i < typesArray.length(); i++) {
                    var obj = typesArray.getJSONObject(i);
                    
                    // Create a DroneType object and populate its fields
                    DroneType droneType = new DroneType(
                        obj.getInt("id"),
                        obj.getString("manufacturer"),
                        obj.getString("typename"),
                        obj.getInt("weight"),
                        obj.getInt("max_speed"),
                        obj.getInt("battery_capacity"),
                        obj.getInt("control_range"),
                        obj.has("max_carriage") ? obj.getInt("max_carriage") : null
                    );
                    
                    // Add the drone to the list
                    droneTypes.add(droneType);
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
        return droneTypes;
    }

    /**
     * Fetches a list of drone dynamics from the Drone API.
     * @return a list of Drone dynamics objects.
     */   
    public List<DroneDynamics> fetchDroneDynamics(int limit, int offset) {
        logger.trace("Entered fetchDroneDynamics method.");

        // Initialize an empty list to store drone dynamics
        List<DroneDynamics> droneDynamicsList = new ArrayList<>();
        
        // Construct the request URL
        String url = apiUrl + "dronedynamics/?format=json&limit=" + limit + "&offset=" + offset;
    
        try {
        	// Dynamically load the API token
            String token = loadApiToken(); // Load the token from config.json

            // Create and set HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token); // Use Token-based authentication
            headers.set("User-Agent", "JavaDroneApp"); // Include a user agent for better API identification
            headers.set("Accept", "application/json");

            // Create the entity with headers
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Perform the HTTP GET request
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // Check if the response status is OK
            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                var dynamicsArray = jsonResponse.getJSONArray("results");

                // Loop through each drone dynamic in the response array
                for (int i = 0; i < dynamicsArray.length(); i++) {
                    var obj = dynamicsArray.getJSONObject(i);

                    // Create a DroneDynamic object and populate its fields
                    DroneDynamics droneDynamics = new DroneDynamics(
                        obj.getString("drone"),
                        obj.getString("timestamp"),
                        obj.getInt("speed"),
                        obj.getString("align_roll"),
                        obj.getString("align_pitch"),
                        obj.getString("align_yaw"),
                        obj.getString("longitude"),
                        obj.getString("latitude"),
                        obj.getInt("battery_status"),
                        obj.getString("last_seen"),
                        obj.getString("status")
                    );

                    // Add the drone dynamic to the list
                    droneDynamicsList.add(droneDynamics);
                }
                logger.trace("fetchDroneDynamics completed successfully.");
            }
        } catch (HttpClientErrorException ex) {
            // Log HTTP-specific errors and rethrow the exception
            logger.error("HTTP Error: {}", ex.getStatusCode(), ex);
            throw ex;
        } catch (Exception ex) {
            // Log unexpected errors and rethrow as a runtime exception
            logger.error("Unexpected Error: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to fetch drone dynamics", ex);
        }
        
        // Return the list of fetched drone dynamics
        return droneDynamicsList;
    }
    
    // The method for calculating the average speed of drones
    /**
     * Calculates the average speed of all drones from the "DroneDynamics" data.
     * @param limit Limit on the number of entries retrieved
     * @param offset Offset for pagination.
     * @return Average speed of all drones as a double.
     */
    public double calculateAverageSpeed(int limit, int offset) {
        logger.trace("Entered calculateAverageSpeed method with limit={} and offset={}", limit, offset);

        // Retrieving the list of DroneDynamics
        List<DroneDynamics> droneDynamicsList = fetchDroneDynamics(limit, offset);
        
        // If no data is available, return the average as 0
        if (droneDynamicsList.isEmpty()) {
            logger.info("No DroneDynamics data available.");
            return 0;
        }

        // Sum of speeds and counter for the number of data points
        double totalSpeed = 0;
        int count = 0;

        // Iterate through the list of DroneDynamics and sum the speed
        for (DroneDynamics dynamics : droneDynamicsList) {
            // Geschwindigkeitswert aus der DroneDynamics entnehmen
            totalSpeed += dynamics.getSpeed();
            count++;
        }

        // Calculation of the average
        double averageSpeed = totalSpeed / count;

        logger.info("Calculated average speed: {}", averageSpeed);
        
        // Returning the average
        return averageSpeed;
    }
}
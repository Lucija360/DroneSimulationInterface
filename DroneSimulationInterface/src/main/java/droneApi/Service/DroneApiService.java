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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import droneApi.Entities.Drone;
import droneApi.Entities.DroneDynamics;
import droneApi.Entities.DroneType;

@Service
public class DroneApiService implements DroneService {

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
    
 // DateTimeFormatter for formatting timestamps
    private static final DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM. dd, yyyy, h:mm a");

    /**
     * Formats the raw timestamp into a human-readable format.
     * @param rawTimestamp		The raw timestamp string (ISO 8601 format).
     * @return A formatted date string.
     */
    private String FormatDate(String rawTimestamp) {
    	try {
    		OffsetDateTime dateTime = OffsetDateTime.parse(rawTimestamp, inputFormatter);
    		return dateTime.format(outputFormatter);
    	} catch (Exception ex) {
    		logger.error("Failed to format date: {}", ex.getMessage());
    		return "Invalid Date";
    	}
    }
    
    /**
     * Public wrapper method for external access to formatDate
     * @param rawTimestamp
     * @return Formatted Timestamp
     */ 
    public String getFormatDate(String rawTimestamp) {
    	logger.trace("Entered getFormatDate with ISO 8601: {}", rawTimestamp);
    	return FormatDate(rawTimestamp);
    }
    
    
    /**
     * Generalized method (API call) to fetch data from raw URL and apply formatting logic.
     * raw URL: URL response and pointing to another endpoint (e.g., http://dronesim.facets-labs.com/api/dronetypes/id/)
     * API call to drone type URL to return the name and other details.
     * @param rawUrl 				The raw URL to fetch data to get more details.
     * @param formatFunction 	A lambda or method reference to apply formatting to the fetched data.
     * @param defaultValue 		A default value to return in case of an error.
     * @return The formatted data or the default value.
     */
    private <T> T fetchAndFormat(String rawUrl, Function<JSONObject, T> formatFunction, T defaultValue) {
    	try {        	
        	// Create the HTTP request with authorization and other necessary headers
        	HttpHeaders headers = new HttpHeaders();
        	headers.set("Authorization", token);	// API token for authentication
        	headers.set("Accept", "application/json");
        	headers.set("User-Agent", "JavaDroneApp"); // Include a user agent for better API identification
        	
        	// Create the entity with headers
        	HttpEntity<String> entity = new HttpEntity<>(headers);
        	
        	// Log the request URL
        	logger.debug("Fetching drone details from URL; {}", rawUrl);
        	
        	// Send the HTTP GET request to fetch the drone type details (name, etc,...)
        	ResponseEntity<String> response = restTemplate.exchange(rawUrl, HttpMethod.GET, entity, String.class);
        	
        	// Parse and process the response
        	// Check if the response status is OK
        	if (response.getStatusCode() == HttpStatus.OK) {
        		// Parse the JSON response to inspect the structure
        		JSONObject jsonResponse = new JSONObject(response.getBody());
        		logger.debug("Drone Type Details Response {}:{}", rawUrl, jsonResponse.toString());
        		
                // Apply the formatting function
                return formatFunction.apply(jsonResponse);
            } else {
                logger.error("Failed to fetch data from URL: {}. Status code: {}", rawUrl, response.getStatusCode());
            }
        } catch (Exception ex) {
            logger.error("Error fetching data from URL: {}. Error: {}", rawUrl, ex.getMessage());
        }
        return defaultValue;
    }

    /**
     * Fetch and format the drone type name based on the URL response by fetching raw data from method fetchDrones
     * @param droneTypeUrl 		The URL of the drone type.
     * @return The formatted drone type name or "Unknown Drone Type" if unavailable.
     */
    public String fetchDroneTypeName(String droneTypeUrl) {
        return fetchAndFormat(droneTypeUrl, jsonResponse -> {
        	// If the "manufacturer" and "typename" fields exist, construct the name
    		if (jsonResponse.has("manufacturer") && jsonResponse.has("typename")) {
    			String manufacturer = jsonResponse.getString("manufacturer");
    			String typename = jsonResponse.getString("typename");
    			
    			// Combine manufacturer and typename into the required format
    			return manufacturer + ": " + typename;	//e.g., "GoPro: Karma"
            }
            return "Unknown Drone Type";
        }, "Unknown Drone Type");
    }
    
    /**
     * Public wrapper method for external access to fetchDroneTypeName
     * @param droneTypeUrl
     * @return Formatted String (need to choose another word to fit)
     */ 
    public String getDroneTypeName(String droneTypeUrl) {
    	logger.trace("Entered getDroneTypeName with URL: {}", droneTypeUrl);
    	return fetchDroneTypeName(droneTypeUrl);
    }

    /**
     * Fetch and format data for the Drone field by fetching DroneDynamics.
     * Ensure the created field is adjusted to UTC timezone (+00:00).
     * @param DroneUrl 		The URL to fetch drone details from raw URL fetching from drone.
     * @return The formatted drone details data or "Unknown Drone" if unavailable.
     */
    public String fetchDroneDetails(String DroneUrl) {
        return fetchAndFormat(DroneUrl, jsonResponse -> {
        	// If the "serialnumber" and "created" fields exist, construct the name
    		if (jsonResponse.has("serialnumber") && jsonResponse.has("created")) {
    			String serialnumber = jsonResponse.getString("serialnumber");
    			String createdRaw = jsonResponse.getString("created");
    			
    			try {
    				//Parse and adjust the timestamp to UTC (+00:00)
    				//Parse the raw Timestamp
    				OffsetDateTime createdDateTime = OffsetDateTime.parse(createdRaw);
    				//Adjust to UTC timezone
                    OffsetDateTime createdInUtc = createdDateTime.withOffsetSameInstant(ZoneOffset.UTC);                    
                    // Format the adjusted timestamp as "yyyy-MM-dd HH:mm:ss.SSSSSS+00:00" with explicit +00:00 instead of Z
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSXXX");
                    String formattedCreated = createdInUtc.format(formatter).replace("Z", "+00:00");
                    
    				// Combine "serialnumber" and "created" data into the required format
                    return "Drone: " + serialnumber + " (created: " + formattedCreated + ")";	//e.g., "Drone: PoD8-2029-804760 (created: 2025-01-10 20:25:25.402566+00:00)"
    			} catch (Exception ex) {
    				logger.error("Failed to process created field: {}. Error; {}", createdRaw, ex.getMessage());
    				return "Drone: " + serialnumber + " (created: Invalid Date)";
    			}    			
            }
            return "Unknown Drone";
        }, "Unknown Drone");
    }
    
    /**
     * Public wrapper method for external access to fetchDroneDetails
     * @param DroneUrl
     * @return Formatted String ((need to choose another word to fit)
     */ 
    public String getDroneDetails(String DroneUrl) {
    	logger.trace("Entered getDroneDetails with URL: {}", DroneUrl);
    	return fetchDroneDetails(DroneUrl);
    }
    
    
    /**
     * Fetches a list of drones from the Drone API.
     * @param limit Number of results to fetch.
     * @param offset Index to start fetching from.
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
               
                // Stream processing for raw data
                drones = IntStream.range(0, droneArray.length())	// Convert the JSON array to a Stream
                		.mapToObj(i -> {
                			try {
                				var obj = droneArray.getJSONObject(i);
                				return new Drone(
                						obj.getInt("id"),
                						obj.getString("dronetype"),	// Keep raw URL for dronetype
                						obj.getString("created"),	// Keep raw created timestamp
                						obj.getString("serialnumber"),
                						obj.getInt("carriage_weight"),
                						obj.getString("carriage_type")
                						);                				         				
                			} catch (Exception e) {
                				logger.error("Error processing drone at index: {}: {}", i, e.getMessage());
                				return null;	//Handle error gracefully
                			}
                		})
                		.filter(Objects::nonNull)	// Remove null entries
                		.collect(Collectors.toList());	//Collect into a List
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
                
             // Stream processing for raw data
                drones = IntStream.range(0, droneArray.length())	// Convert the JSON array to a Stream
                		.mapToObj(i -> {
                			try {
                				var obj = droneArray.getJSONObject(i);
                				return new Drone(
                						obj.getInt("id"),
                						obj.getString("dronetype"),	// Keep raw URL for dronetype
                						obj.getString("created"),	// Keep raw created timestamp
                						obj.getString("serialnumber"),
                						obj.getInt("carriage_weight"),
                						obj.getString("carriage_type")
                						);                				         				
                			} catch (Exception e) {
                				logger.error("Error processing drone at index: {}: {}", i, e.getMessage());
                				return null;	//Handle error gracefully
                			}
                		})
                		.filter(Objects::nonNull)	// Remove null entries
                		.collect(Collectors.toList());	//Collect into a List
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
     * Fetches a list of drone types from the Drone API. 
     * @param limit Number of results to fetch.
     * @param offset Index to start fetching from.
     * @return a list of Drone type objects.
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
     * @param limit Number of results to fetch.
     * @param offset Index to start fetching from.
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
                    
                    // Format longitude and latitude to six decimal places
                    String formattedLongitude = String.format("%.6f", Double.parseDouble(obj.getString("longitude")));
                    String formattedLatitude = String.format("%.6f", Double.parseDouble(obj.getString("latitude")));

                    // Create a DroneDynamic object and populate its fields
                    DroneDynamics droneDynamics = new DroneDynamics(
                        obj.getString("drone"),
                        obj.getString("timestamp"),
                        obj.getInt("speed"),
                        obj.getString("align_roll"),
                        obj.getString("align_pitch"),
                        obj.getString("align_yaw"),
                        formattedLongitude,
                        formattedLatitude,
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
    
    
    /**
     * Fetch drone data from the external API by drone ID.
     * @param id The ID of the drone to fetch.
     * @return A Drone object containing drone details.
     */
    public Drone fetchDroneById(int id) {
        logger.trace("Entered fetchDroneById method with id={}", id);

        // Construct the request URL for fetching the drone by ID
        String url = apiUrl + "drones/" + id + "/?format=json";

        try {
            // Dynamically load the API token
            String token = loadApiToken(); // Load the token from config.json

            // Prepare HTTP headers with authentication token
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
                JSONObject obj = new JSONObject(response.getBody());

                // Extract raw data for dronetype and created fields
                // Preffered for Maintainability
                String rawDroneTypeUrl = obj.getString("dronetype");
                String rawCreated = obj.getString("created");
                
                // Construct and return the Drone object with raw data
                return new Drone(
                    obj.getInt("id"),
                    rawDroneTypeUrl,	// Keep raw URL for dronetype
                    rawCreated,			// Keep raw timestamp as a String
                    obj.getString("serialnumber"),
                    obj.getInt("carriage_weight"),
                    obj.getString("carriage_type")
                );
            }
        } catch (HttpClientErrorException ex) {
            // Log HTTP-specific errors and rethrow the exception
            logger.error("HTTP Error: Status={}, Response={}", ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
            throw ex;
        } catch (Exception ex) {
            // Log unexpected errors and rethrow as a runtime exception
            logger.error("Unexpected Error: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to fetch drone by ID", ex);
        }

        // Return null if the drone was not found or an error occurred
        return null;
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
            // Take speed value from DroneDynamics
            totalSpeed += dynamics.getSpeed();
            count++;
        }

        // Calculation of the average
        double averageSpeed = totalSpeed / count;

        logger.info("Calculated average speed: {}", averageSpeed);
        
        // Returning the average
        return averageSpeed;
    }
    public Map<String, Long> getTopManufacturers(int topN, int limit, int offset) {
        logger.trace("Entered getTopManufacturers method with topN={}, limit={}, offset={}", topN, limit, offset);

        // Retrieve the list of all drone types
        List<DroneType> droneTypes = fetchDroneTypes(limit, offset);

        // Map to store manufacturer counts
        Map<String, Long> manufacturerCount = new HashMap<>();

        // Count the occurrences of each manufacturer
        for (DroneType droneType : droneTypes) {
            String manufacturer = droneType.getManufacturer();
            manufacturerCount.put(manufacturer, manufacturerCount.getOrDefault(manufacturer, 0L) + 1);
        }

        // Sort the manufacturers by count in descending order
        List<Map.Entry<String, Long>> sortedEntries = new ArrayList<>(manufacturerCount.entrySet());
        sortedEntries.sort((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()));

        // Collect the top N manufacturers
        Map<String, Long> topManufacturers = new LinkedHashMap<>();
        int count = 0;
        for (Map.Entry<String, Long> entry : sortedEntries) {
            if (count >= topN) break;
            topManufacturers.put(entry.getKey(), entry.getValue());
            count++;
        }

        logger.info("Top {} manufacturers retrieved successfully.", topN);
        return topManufacturers;
    

    }
   

}


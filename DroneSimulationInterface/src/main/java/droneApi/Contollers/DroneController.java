package droneApi.Contollers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import droneApi.Service.DroneApiService;
import droneApi.Entities.Drone;
import droneApi.Entities.DroneDynamics;
import droneApi.Entities.DroneType;

/**
 * REST controller for managing drone-related endpoints.
 */
@RestController
@RequestMapping("/api")
public class DroneController {

	// Logger for logging application events
    private static final Logger logger = LoggerFactory.getLogger(DroneController.class);
    
    // Service that handles drone-related logic
    private final DroneApiService droneApiService;

    /**
     * Constructor that initializes the DroneApiService using dependency injection.
     * @param droneApiService the service that interacts with the drone API.
     */
    @Autowired
    public DroneController(DroneApiService droneApiService) {
        this.droneApiService = droneApiService;
    }
    
    /**
     * Endpoint to verify server accessibility.
     * Maps to GET requests at "/".
     * @return a ResponseEntity with a success message or error response.
     */
    @Operation(summary = "Check server accessibility", description = "Checks if the server is accessible.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Server is accessible."),
        @ApiResponse(responseCode = "500", description = "Error checking server accessibility.")
    })
    @GetMapping("/")
    public ResponseEntity<String> verifyServer() {
        logger.trace("Entered verifyServer endpoint.");
        try {
            String status = droneApiService.verifyServerAccessibility();
            logger.trace("Server accessibility verified successfully.");
            return ResponseEntity.ok(status);
        } catch (Exception ex) {
            logger.error("Failed to verify server accessibility: {}", ex.getMessage(), ex);
            return ResponseEntity.status(500).body("Failed to verify server accessibility.");
        }
    }
    
    /**
     * Endpoint to retrieve a list of available drones.
     * Maps to GET requests at "/api/drones/".
     * @return a ResponseEntity containing a list of Drone objects or an error response.
     */
    @Operation(summary = "Retrieve list of drones", description = "Fetches a list of available drones.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of drones."),
        @ApiResponse(responseCode = "500", description = "Error retrieving list of drones.")
    })
    @GetMapping(value = "/drones/", produces = "application/json")
    public ResponseEntity<List<Drone>> getDrones( @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
    	logger.trace("Entered getDrones endpoint with limit={} and offset={}", limit, offset);
        try {
        	
        	// Fetch the list of drones from the service layer
        	List<Drone> drones = droneApiService.fetchDrones(limit, offset);
            
            logger.trace("Successfully retrieved drones.");
            
            // Return the list of drones with a 200 OK status
            return ResponseEntity.ok(drones);
        } catch (Exception ex) {
        	
        	// Log an error if drone retrieval fails
            logger.error("Failed to retrieve drones: {}", ex.getMessage(), ex);
            
            // Return a 500 Internal Server Error response
            return ResponseEntity.status(500).build();
        }
    }
    
    
    /**
     * Endpoint to retrieve drone types.
     * Maps to GET requests at "/api/dronetypes/".
     * @param limit Number of results to return per page (default: 10).
     * @param offset Index to start fetching results from (default: 0).
     * @return a ResponseEntity containing a list of drone types or an error response.
     */
    @Operation(summary = "Retrieve list of drone types", description = "Fetches a list of available drone types.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of drone types."),
        @ApiResponse(responseCode = "500", description = "Error retrieving list of drone types.")
    })
    @GetMapping(value = "/dronetypes/", produces = "application/json")
    public ResponseEntity<List<DroneType>> getDroneTypes(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        logger.trace("Entered getDroneTypes endpoint with limit={} and offset={}", limit, offset);

        try {
        	
        	// Fetch the list of drone types from the service layer
            List<DroneType> droneTypes = droneApiService.fetchDroneTypes(limit, offset);
            
            logger.trace("Successfully retrieved drone types.");
            
            // Return the list of drones with a 200 OK status
            return ResponseEntity.ok(droneTypes);
        } catch (Exception ex) {
        	
        	// Log an error if drone type retrieval fails
            logger.error("Failed to retrieve drone types: {}", ex.getMessage(), ex);
            
            // Return a 500 Internal Server Error response
            return ResponseEntity.status(500).build();
        }
    }
    
    
    /**
     * Endpoint to retrieve drone dynamics.
     * Maps to GET requests at "/api/dronedynamics/".
     * @param limit Number of results to return per page (default: 10).
     * @param offset Index to start fetching results from (default: 0).
     * @return a ResponseEntity containing dynamic data for drones or an error response.
     */
    @Operation(summary = "Retrieve drone dynamics", description = "Fetches dynamic data for drones.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved drone dynamics."),
        @ApiResponse(responseCode = "500", description = "Error retrieving drone dynamics.")
    })
    @GetMapping(value = "/dronedynamics/",  produces = "application/json")
    public ResponseEntity<List<DroneDynamics>> getDroneDynamics(@RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
    	logger.trace("Entered getDroneDynamics endpoint with limit={} and offset={}", limit, offset);
    	
    	 try {
    		 	// Fetch the list of drone dynamics from the service layer
    	        List<DroneDynamics> droneDynamics = droneApiService.fetchDroneDynamics(limit, offset);
    	        
    	        logger.trace("Successfully retrieved drone dynamics.");
    	        
    	        // Return the list of drones with a 200 OK status
    	        return ResponseEntity.ok(droneDynamics);
    	    } catch (Exception ex) {
    	    	
    	    	// Log an error if drone dynamics retrieval fails
    	        logger.error("Failed to retrieve drone dynamics: {}", ex.getMessage(), ex);
    	        
    	        // Return a 500 Internal Server Error response
    	        return ResponseEntity.status(500).build();
    	    }
     }
    
    
    /**
     * Endpoint to calculate the average speed of all drones
     * Maps to GET requests at "/api/drones/average-speed/all"
     * @param limit the maximum number of entries to consider 
     * @param offset the starting point for pagination 
     * @return a ResponseEntity containing the average speed as a double or an error response
     */
    @Operation(
    	    summary = "Calculate average speed of all drones",
    	    description = "Fetches the average speed of all drones based on DroneDynamics data."
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", description = "Successfully calculated the average speed."),
    	    @ApiResponse(responseCode = "500", description = "Error calculating the average speed.")
    	})
    	@GetMapping(value = "/drones/average-speed/all", produces = "application/json")
    	public ResponseEntity<Double> getAverageSpeed(
    	        @RequestParam(defaultValue = "100") int limit,
    	        @RequestParam(defaultValue = "0") int offset) {
    	       logger.trace("Entered getAverageSpeed endpoint with limit={} and offset={}", limit, offset);
    	
    	    try {
    	        // Calling the method in the service
    	        double averageSpeed = droneApiService.calculateAverageSpeed(limit, offset);
    	        
    	        // Log the calculated average speed along with the input parameters
    	        logger.info("Calculated average speed: {} for limit={} and offset={}", averageSpeed, limit, offset);


    	        // Return of the calculated average speed
    	        return ResponseEntity.ok(averageSpeed);
    	    } catch (Exception ex) {
    	        // error handling
    	        logger.error("Error while calculating the average speed.", ex);
    	        return ResponseEntity.status(500).build();
    	    }
    	}
}
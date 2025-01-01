package droneApi.Contollers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import droneApi.Service.DroneApiService;
import droneApi.Entities.Drone;

/**
 * REST controller for managing drone-related endpoints.
 */
@RestController
@RequestMapping("/api/drones")
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
     * Maps to GET requests at "/api/drones/list".
     * @return a ResponseEntity containing a list of Drone objects or an error response.
     */
    @GetMapping("/drones/list")
    public ResponseEntity<List<Drone>> getDrones() {
        logger.trace("Entered getDrones endpoint.");
        try {
        	
        	// Fetch the list of drones from the service layer
            List<Drone> drones = droneApiService.fetchDrones();
            
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
     * Maps to GET requests at "/api/dronetypes".
     * @return a ResponseEntity containing a list of drone types or an error response.
     */
    @GetMapping("/dronetypes")
    public ResponseEntity<List<String>> getDroneTypes() {
        logger.trace("Entered getDroneTypes endpoint.");
        try {
            List<String> droneTypes = droneApiService.fetchDroneTypes();
            logger.trace("Successfully retrieved drone types.");
            return ResponseEntity.ok(droneTypes);
        } catch (Exception ex) {
            logger.error("Failed to retrieve drone types: {}", ex.getMessage(), ex);
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * Endpoint to retrieve drone dynamics.
     * Maps to GET requests at "/api/dronedynamics".
     * @return a ResponseEntity containing dynamic data for drones or an error response.
     */
    @GetMapping("/dronedynamics")
    public ResponseEntity<List<String>> getDroneDynamics() {
        logger.trace("Entered getDroneDynamics endpoint.");
        try {
            List<String> droneDynamics = droneApiService.fetchDroneDynamics();
            logger.trace("Successfully retrieved drone dynamics.");
            return ResponseEntity.ok(droneDynamics);
        } catch (Exception ex) {
            logger.error("Failed to retrieve drone dynamics: {}", ex.getMessage(), ex);
            return ResponseEntity.status(500).build();
        }
    }
    
}

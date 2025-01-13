package droneApi.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import droneApi.Service.DroneApiService;
import droneApi.Entities.Drone;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import droneApi.Entities.DroneDynamics;
import droneApi.Entities.DroneType;

/**
 * REST-Controller für die Verwaltung von Drone-Endpunkten.
 * 
 */
@RestController
@RequestMapping("/api")
public class DroneController {

    private static final Logger logger = LoggerFactory.getLogger(DroneController.class);
    
    private final DroneApiService droneApiService;

    @Autowired
    public DroneController(DroneApiService droneApiService) {
        this.droneApiService = droneApiService;
    }
    
    @Operation(summary = "Überprüft die Server-Erreichbarkeit", description = "Prüft, ob der Server erreichbar ist.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Server ist erreichbar."),
        @ApiResponse(responseCode = "500", description = "Fehler bei der Überprüfung der Server-Erreichbarkeit.")
    })
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<String> verifyServer() {
        logger.trace("Eingang in den verifyServer Endpunkt.");
        try {
            String status = droneApiService.verifyServerAccessibility();
            logger.trace("Server-Erreichbarkeit erfolgreich überprüft.");
            return ResponseEntity.ok(status);
        } catch (Exception ex) {
            logger.error("Fehler bei der Überprüfung der Server-Erreichbarkeit: {}", ex.getMessage(), ex);
            return ResponseEntity.status(500).body("Fehler bei der Überprüfung der Server-Erreichbarkeit.");
        }
    }
    
    @Operation(summary = "Liste der Drohnen abrufen", description = "Holt eine Liste aller verfügbaren Drohnen.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste der Drohnen erfolgreich abgerufen."),
        @ApiResponse(responseCode = "500", description = "Fehler beim Abrufen der Drohnen.")
    })
    @GetMapping(value = "/drones", produces = "application/json")
    public ResponseEntity<List<Drone>> getDrones(@RequestParam(defaultValue = "10") int limit,
                                                  @RequestParam(defaultValue = "0") int offset) {
        logger.trace("Eingang in den getDrones Endpunkt mit limit={} und offset={}", limit, offset);
        try {
            List<Drone> drones = droneApiService.fetchDrones(limit, offset);
            logger.trace("Drohnen erfolgreich abgerufen.");
            return ResponseEntity.ok(drones);
        } catch (Exception ex) {
            logger.error("Fehler beim Abrufen der Drohnen: {}", ex.getMessage(), ex);
            return ResponseEntity.status(500).build();
        }
    }
    
    @Operation(summary = "Drohnen-Typen abrufen", description = "Holt eine Liste aller verfügbaren Drohnen-Typen.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste der Drohnen-Typen erfolgreich abgerufen."),
        @ApiResponse(responseCode = "500", description = "Fehler beim Abrufen der Drohnen-Typen.")
    })
    @GetMapping(value = "/dronetypes", produces = "application/json")
    public ResponseEntity<List<DroneType>> getDroneTypes(@RequestParam(defaultValue = "10") int limit,
                                                          @RequestParam(defaultValue = "0") int offset) {
        logger.trace("Eingang in den getDroneTypes Endpunkt mit limit={} und offset={}", limit, offset);
        try {
            List<DroneType> droneTypes = droneApiService.fetchDroneTypes(limit, offset);
            logger.trace("Drohnen-Typen erfolgreich abgerufen.");
            return ResponseEntity.ok(droneTypes);
        } catch (Exception ex) {
            logger.error("Fehler beim Abrufen der Drohnen-Typen: {}", ex.getMessage(), ex);
            return ResponseEntity.status(500).build();
        }
    }
    
    @Operation(summary = "Drohnen-Dynamik abrufen", description = "Holt dynamische Daten für Drohnen.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Drohnen-Dynamik erfolgreich abgerufen."),
        @ApiResponse(responseCode = "500", description = "Fehler beim Abrufen der Drohnen-Dynamik.")
    })
    @GetMapping(value = "/dronedynamics", produces = "application/json")
    public ResponseEntity<List<DroneDynamics>> getDroneDynamics(@RequestParam(defaultValue = "10") int limit,
                                                                @RequestParam(defaultValue = "0") int offset) {
        logger.trace("Eingang in den getDroneDynamics Endpunkt mit limit={} und offset={}", limit, offset);
        try {
            List<DroneDynamics> droneDynamics = droneApiService.fetchDroneDynamics(limit, offset);
            logger.trace("Drohnen-Dynamik erfolgreich abgerufen.");
            return ResponseEntity.ok(droneDynamics);
        } catch (Exception ex) {
            logger.error("Fehler beim Abrufen der Drohnen-Dynamik: {}", ex.getMessage(), ex);
            return ResponseEntity.status(500).build();
        }
    }
}

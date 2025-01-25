package droneApi.Service;

import java.util.List;

import droneApi.Entities.Drone;

/**
 * Interface defining drone-related operations.
 */
public interface DroneService {
    
    /**
     * Fetches drone details by ID.
     * @param id The ID of the drone.
     * @return Drone object if found, null otherwise.
     */
    Drone fetchDroneById(int id);

    /**
     * Calculates the average speed of all drones.
     * @param limit Number of results to fetch.
     * @param offset Index to start fetching from.
     * @return Average speed of all drones.
     */
    double calculateAverageSpeed(int limit, int offset);
    
    /**
     * Fetches a list of drones.
     * @param limit Number of results to fetch.
     * @param offset Index to start fetching from.
     * @return List of Drone objects.
     */
    List<Drone> fetchDrones(int limit, int offset);

    /**
     * Fetches a list of drone types.
     * @param limit Number of results to fetch.
     * @param offset Index to start fetching from.
     * @return List of DroneType objects.
     */
    List<droneApi.Entities.DroneType> fetchDroneTypes(int limit, int offset);

    /**
     * Fetches a list of drone dynamics.
     * @param limit Number of results to fetch.
     * @param offset Index to start fetching from.
     * @return List of DroneDynamics objects.
     */
    List<droneApi.Entities.DroneDynamics> fetchDroneDynamics(int limit, int offset);
    
    
}

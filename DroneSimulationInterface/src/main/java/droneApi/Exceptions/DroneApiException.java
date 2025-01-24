package droneApi.Exceptions;

/**
 * Custom exception for Drone API-related errors.
 */
public class DroneApiException extends RuntimeException {
    public DroneApiException(String message) {
        super(message);
    }

    public DroneApiException(String message, Throwable cause) {
        super(message, cause);
    }
}

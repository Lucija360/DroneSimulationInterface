package droneApi.Entities;

/**
 * Entity class representing a Drone.
 */
public class Drone {
	
	 // Unique identifier for the drone
    private String serialNumber;
    
    // Model name of the drone
    private String model;
    
    // Maximum speed of the drone in meters per second
    private double speed;
    
    // Current battery level of the drone in percentage
    private double batteryStatus;
    
    // Operational status of the drone
    private String status;
    

    /**
     * Constructor to initialize all fields of the Drone entity.
     * @param serialNumber Unique serial number of the drone
     * @param model Model name of the drone
     * @param speed Maximum speed of the drone
     * @param batteryStatus Current battery level of the drone
     * @param status Operational status of the drone
     */
    public Drone(String serialNumber, String model, double speed, double batteryStatus, String status) {
        this.serialNumber = serialNumber;
        this.model = model;
        this.speed = speed;
        this.batteryStatus = batteryStatus;
        this.status = status;
    }

    // Getters and Setters
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }

    public double getBatteryStatus() { return batteryStatus; }
    public void setBatteryStatus(double batteryStatus) { this.batteryStatus = batteryStatus; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
}
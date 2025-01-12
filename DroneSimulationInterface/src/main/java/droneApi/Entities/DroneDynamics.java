package droneApi.Entities;

/**
 * Entity class representing dynamic data of a Drone.
 */
public class DroneDynamics {
	
		// Name or identifier of the drone
	  	private String drone;
	  	
	  	// Timestamp indicating the recorded time of the dynamic data
	    private String timestamp;
	    
	    // Current speed of the drone
	    private int speed;
	    
	    // Drone's alignment on the roll axis
	    private String alignRoll;
	    
	    // Drone's alignment on the pitch axis
	    private String alignPitch;
	    
	    // Drone's alignment on the yaw axis
	    private String alignYaw;
	    
	    // Longitude of the drone's position
	    private String longitude;
	    
	    // Latitude of the drone's position
	    private String latitude;
	    
	    // Current battery status of the drone in percentage
	    private int batteryStatus;
	    
	    // Last recorded time the drone was active
	    private String lastSeen;
	    
	    // Current operational status of the drone
	    private String status;
	    
	    
	    /**
	     * Constructor to initialize all fields of DroneDynamics.
	     *
	     * @param drone         Name or identifier of the drone
	     * @param timestamp     Recorded time of the dynamic data
	     * @param speed         Current speed of the drone
	     * @param alignRoll     Alignment of the drone on the roll axis
	     * @param alignPitch    Alignment of the drone on the pitch axis
	     * @param alignYaw      Alignment of the drone on the yaw axis
	     * @param longitude     Longitude of the drone's position
	     * @param latitude      Latitude of the drone's position
	     * @param batteryStatus Current battery level of the drone
	     * @param lastSeen      Last recorded active time of the drone
	     * @param status        Operational status of the drone
	     */
	    public DroneDynamics(String drone, String timestamp, int speed, String alignRoll, String alignPitch,
	                         String alignYaw, String longitude, String latitude, int batteryStatus, String lastSeen,
	                         String status) {
	        this.drone = drone;
	        this.timestamp = timestamp;
	        this.speed = speed;
	        this.alignRoll = alignRoll;
	        this.alignPitch = alignPitch;
	        this.alignYaw = alignYaw;
	        this.longitude = longitude;
	        this.latitude = latitude;
	        this.batteryStatus = batteryStatus;
	        this.lastSeen = lastSeen;
	        this.status = status;
	    }
	    
	    // Getters and Setters for each field
	    public String getDrone() { return drone; }
	    public void setDrone(String drone) { this.drone = drone; }

	    public String getTimestamp() { return timestamp; }
	    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

	    public int getSpeed() { return speed; }
	    public void setSpeed(int speed) { this.speed = speed; }

	    public String getAlignRoll() { return alignRoll; }
	    public void setAlignRoll(String alignRoll) { this.alignRoll = alignRoll; }

	    public String getAlignPitch() { return alignPitch; }
	    public void setAlignPitch(String alignPitch) { this.alignPitch = alignPitch; }

	    public String getAlignYaw() { return alignYaw; }
	    public void setAlignYaw(String alignYaw) { this.alignYaw = alignYaw; }

	    public String getLongitude() { return longitude; }
	    public void setLongitude(String longitude) { this.longitude = longitude; }

	    public String getLatitude() { return latitude; }
	    public void setLatitude(String latitude) { this.latitude = latitude; }

	    public int getBatteryStatus() { return batteryStatus; }
	    public void setBatteryStatus(int batteryStatus) { this.batteryStatus = batteryStatus; }

	    public String getLastSeen() { return lastSeen; }
	    public void setLastSeen(String lastSeen) { this.lastSeen = lastSeen; }

	    public String getStatus() { return status; }
	    public void setStatus(String status) { this.status = status; }
}

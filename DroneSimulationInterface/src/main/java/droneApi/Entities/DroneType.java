package droneApi.Entities;

/**
 * Entity class representing a Drone Type.
 */
public class DroneType {

		// Unique identifier for the drone type
	 	private int id;
	 	
	 	// Manufacturer name of the drone type
	    private String manufacturer;
	    
	    // Name of the drone type
	    private String typename;
	    
	    // Weight of the drone type
	    private int weight;
	    
	    // Maximum speed of the drone type
	    private int maxSpeed;
	    
	    // Maximum battery capacity of the drone type
	    private int batteryCapacity;
	    
	    // Control range of the drone type
	    private int controlRange;
	    
	    // Maximum carriage capacity, optional field
	    private Integer maxCarriage; 
	    
	    
	    /**
	     * Constructor to initialize all fields of the DroneType entity.
	     *
	     * @param id              Unique identifier for the drone type
	     * @param manufacturer    Manufacturer name of the drone type
	     * @param typename        Name of the drone type
	     * @param weight          Weight of the drone type
	     * @param maxSpeed        Maximum speed of the drone type
	     * @param batteryCapacity Maximum battery capacity of the drone type
	     * @param controlRange    Control range of the drone type
	     * @param maxCarriage     Maximum carriage capacity, optional
	     */
	    public DroneType(int id, String manufacturer, String typename, int weight, int maxSpeed, 
	                     int batteryCapacity, int controlRange, Integer maxCarriage) {
	        this.id = id;
	        this.manufacturer = manufacturer;
	        this.typename = typename;
	        this.weight = weight;
	        this.maxSpeed = maxSpeed;
	        this.batteryCapacity = batteryCapacity;
	        this.controlRange = controlRange;
	        this.maxCarriage = maxCarriage;
	    }
	    
	    
	    // Getters and Setters for each fiel
	    public int getId() { return id; }
	    public void setId(int id) { this.id = id; }

	    public String getManufacturer() { return manufacturer; }
	    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

	    public String getTypename() { return typename; }
	    public void setTypename(String typename) { this.typename = typename; }

	    public int getWeight() { return weight; }
	    public void setWeight(int weight) { this.weight = weight; }

	    public int getMaxSpeed() { return maxSpeed; }
	    public void setMaxSpeed(int maxSpeed) { this.maxSpeed = maxSpeed; }

	    public int getBatteryCapacity() { return batteryCapacity; }
	    public void setBatteryCapacity(int batteryCapacity) { this.batteryCapacity = batteryCapacity; }

	    public int getControlRange() { return controlRange; }
	    public void setControlRange(int controlRange) { this.controlRange = controlRange; }

	    public Integer getMaxCarriage() { return maxCarriage; }
	    public void setMaxCarriage(Integer maxCarriage) { this.maxCarriage = maxCarriage; }
}

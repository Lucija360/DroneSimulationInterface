package droneApi.Entities;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity class representing a Drone Type.
 */
public class DroneType {

		@JsonProperty("id")
		// Unique identifier for the drone type
	 	private int id;
	 	
		@JsonProperty("manufacturer")
	 	// Manufacturer name of the drone type
	    private String manufacturer;
	    
		@JsonProperty("typename")
	    // Name of the drone type
	    private String typename;
	    
		@JsonProperty("weight")
	    // Weight of the drone type
	    private int weight;
	    
		@JsonProperty("max_speed")
	    // Maximum speed of the drone type
	    private int maxSpeed;
	    
		@JsonProperty("battery_capacity")
	    // Maximum battery capacity of the drone type
	    private int batteryCapacity;
	    
		@JsonProperty("control_range")
	    // Control range of the drone type
	    private int controlRange;
	    
		 @JsonProperty("max_carriage")
	    // Maximum carriage capacity, optional field
	    private Integer maxCarriage; 
	    
	    // Default constructor required for Jackson deserialization
	    public DroneType() {
	    }
	    
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

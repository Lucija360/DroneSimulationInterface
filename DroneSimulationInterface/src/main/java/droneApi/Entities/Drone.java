package droneApi.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Entity class representing a Drone.
 */
public class Drone extends AbstractDroneEntity {
	

    @JsonProperty("dronetype") // Maps JSON field "dronetype" to this field
    private String dronetypeRaw;	// Keeps the raw URL for dronetype
     
   
    @JsonProperty("carriage_weight") // Maps JSON field "carriage_weight" to this field
    // Weight of the drone's carriage
    private int carriageWeight;
     
    @JsonProperty("carriage_type") // Maps JSON field "carriage_type" to this field
    // Type of the drone's carriage (SEN, ACT, NOT)
    private String carriageType;


    // Default constructor required for JSON deserialization
    public Drone() {
        super(0, "", "");  // Default values for superclass fields
    }
     
    /**
     * Constructor to initialize all fields of the Drone entity.
     * @param id 				Unique identifier for the drone
     * @param dronetypeRaw		Raw URL for the drone type
     * @param createdRaw		Raw timestamp when the drone was created
     * @param serialnumber 		Unique serial number for the drone
     * @param carriageWeight 	Maximum weight capacity of the drone
     * @param carriageType 		Type of carriage the drone is equipped with
     */
    public Drone(int id, String dronetypeRaw, String createdRaw, String serialnumber, int carriageWeight, String carriageType) {
        super(id, serialnumber, createdRaw);	//Pass raw timestamp to superclass
        this.dronetypeRaw = dronetypeRaw;	// Store raw dronetype        
        this.carriageWeight = carriageWeight;
        this.carriageType = carriageType;
    }

    // Override abstract method to provide drone-specific details
    @Override
    public String getEntityDetails() {
        return "Drone ID: " + id + ", Type: " + dronetypeRaw + ", Created: " + createdRaw;
    }

    // Getters and setters for new fields
    public String getDronetypeRaw() { return dronetypeRaw; }
    public void setDronetypeRaw(String dronetypeRaw) { this.dronetypeRaw = dronetypeRaw; }
    
    public int getCarriageWeight() { return carriageWeight; }
    public void setCarriageWeight(int carriageWeight) { this.carriageWeight = carriageWeight; }

    public String getCarriageType() { return carriageType; }
    public void setCarriageType(String carriageType) { this.carriageType = carriageType; }
}
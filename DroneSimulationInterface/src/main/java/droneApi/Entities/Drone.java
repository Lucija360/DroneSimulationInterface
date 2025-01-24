package droneApi.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Entity class representing a Drone.
 */
public class Drone extends AbstractDroneEntity {
	

    @JsonProperty("dronetype") // Maps JSON field "dronetype" to this field
    private String dronetype;	// The field now holds the name of the drone type, not the URL.
     
   
    @JsonProperty("carriage_weight") // Maps JSON field "carriage_weight" to this field
    // Weight of the drone's carriage
    private int carriageWeight;
     
    @JsonProperty("carriage_type") // Maps JSON field "carriage_type" to this field
    // Type of the drone's carriage (SEN, ACT, NOT)
    private String carriageType;


    // Default constructor required for JSON deserialization
    public Drone() {
        super(0, "", new Date());  // Default values for superclass fields
    }
     
    /**
     * Constructor to initialize all fields of the Drone entity.
     * @param id Unique identifier for the drone
     * @param dronetype Type of the drone (human-readable name)
     * @param created Date and time when the drone was created
     * @param serialnumber Unique serial number for the drone
     * @param carriageWeight Maximum weight capacity of the drone
     * @param carriageType Type of carriage the drone is equipped with
     */
    public Drone(int id, String dronetype, Date created, String serialnumber, int carriageWeight, String carriageType) {
        super(id, serialnumber, created);
        this.dronetype = dronetype;
        this.carriageWeight = carriageWeight;
        this.carriageType = carriageType;
    }

    // Override abstract method to provide drone-specific details
    @Override
    public String getEntityDetails() {
        return "Drone ID: " + id + ", Type: " + dronetype + ", Created: " + created;
    }

    // Getters and setters for new fields
    public String getDronetype() { return dronetype; }
    public void setDronetype(String dronetype) { this.dronetype = dronetype; }

    public int getCarriageWeight() { return carriageWeight; }
    public void setCarriageWeight(int carriageWeight) { this.carriageWeight = carriageWeight; }

    public String getCarriageType() { return carriageType; }
    public void setCarriageType(String carriageType) { this.carriageType = carriageType; }
}
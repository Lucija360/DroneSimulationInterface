package droneApi.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity class representing a Drone.
 */
public class Drone {
	
	// Unique identifier for the drone
    private int id;
    
    @JsonProperty("dronetype") // Maps JSON field "dronetype" to this field
    private String dronetype;
     
     @JsonProperty("created") // Maps JSON field "created" to this field
    // Timestamp when the drone was created
    private String created;
    
     @JsonProperty("serialnumber") // Maps JSON field "serialnumber" to this field
    // Unique serial number of the drone
    private String serialnumber;
     @JsonProperty("carriage_weight") // Maps JSON field "carriage_weight" to this field
    // Weight of the drone's carriage
    private int carriageWeight;
     @JsonProperty("carriage_type") // Maps JSON field "carriage_type" to this field
    // Type of the drone's carriage (SEN, ACT, NOT)
    private String carriageType;

     public Drone() {}
     
    /**
     * Constructor to initialize all fields of the Drone entity.
     * @param id Unique identifier for the drone
     * @param dronetype Type of the drone
     * @param created Date and time when the drone was created
     * @param serialnumber Unique serial number for the drone
     * @param carriageWeight Maximum weight capacity of the drone
     * @param carriageType Type of carriage the drone is equipped with
     */
    public Drone(int id, String dronetype, String created, String serialnumber, int carriageWeight, String carriageType) {
        this.id = id;
        this.dronetype = dronetype;
        this.created = created;
        this.serialnumber = serialnumber;
        this.carriageWeight = carriageWeight;
        this.carriageType = carriageType;
    }

    // Getter and setter methods for all fields
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDronetype() { return dronetype; }
    public void setDronetype(String dronetype) { this.dronetype = dronetype; }

    public String getCreated() { return created; }
    public void setCreated(String created) { this.created = created; }

    public String getSerialnumber() { return serialnumber; }
    public void setSerialnumber(String serialnumber) { this.serialnumber = serialnumber; }

    public int getCarriageWeight() { return carriageWeight; }
    public void setCarriageWeight(int carriageWeight) { this.carriageWeight = carriageWeight; }

    public String getCarriageType() { return carriageType; }
    public void setCarriageType(String carriageType) { this.carriageType = carriageType; }
}
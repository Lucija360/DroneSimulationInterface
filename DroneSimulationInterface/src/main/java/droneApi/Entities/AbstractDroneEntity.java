package droneApi.Entities;

import java.util.Date;

/**
 * Abstract class representing common properties of drone entities.
 */
public abstract class AbstractDroneEntity {
    
    protected int id;          // Unique identifier
    protected String serialNumber;  // Drone serial number
    protected Date created;    // Timestamp of creation

    // Constructor for common attributes
    public AbstractDroneEntity(int id, String serialNumber, Date created) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.created = created;
    }

    // Abstract method to be implemented by subclasses
    public abstract String getEntityDetails();

    // Getters and Setters for shared properties
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public Date getCreated() { return created; }
    public void setCreated(Date created) { this.created = created; }
}

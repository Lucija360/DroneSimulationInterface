package droneApi.Gui;

import java.awt.GridLayout;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import droneApi.Entities.Drone;

class DroneDetailsWindow extends JFrame {
	
	private static final Logger logger = LoggerFactory.getLogger(Drone.class);	// Logger defined here at the class level
																				// logs message for entire class

	// DateTimeFormatter for formatting timestamps
    private static final DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM. dd, yyyy, h:mm a");

    public DroneDetailsWindow(Drone drone) {
        setTitle("Drone Details");
        setSize(400, 300);
        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel("Drone ID:"));
        add(new JLabel(String.valueOf(drone.getId())));

        add(new JLabel("Dronetype:"));
        add(new JLabel(formatDroneType(drone.getDronetypeRaw())));

        add(new JLabel("Created:"));
        add(new JLabel(formatDate(drone.getCreatedRaw())));

        add(new JLabel("Serial Number:"));
        add(new JLabel(drone.getSerialNumber()));

        add(new JLabel("Carriage Weight:"));
        add(new JLabel(drone.getCarriageWeight() + " g"));

        add(new JLabel("Carriage Type:"));
        add(new JLabel(drone.getCarriageType()));

        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Formats the raw timestamp into a human-readable format.
     * @param rawTimestamp		The raw timestamp string (ISO 8601 format).
     * @return A formatted date string.
     */
    private String formatDate(String rawTimestamp) {
    	try {
    		OffsetDateTime dateTime = OffsetDateTime.parse(rawTimestamp, inputFormatter);
    		return dateTime.format(outputFormatter);
    	} catch (Exception ex) {
    		logger.error("Failed to format date: {}", ex.getMessage());
    		return "Invalid Date";
    	}
    }
    
    /**
     * Formats the raw dronetype URL into a human-readable format.
     * @param rawDroneType		The raw dronetype URL.
     * @return A formatted dronetype name.
     */
    private String formatDroneType(String rawDroneType) {
    	if (rawDroneType == null || rawDroneType.isEmpty()) {
    		return "Unknown Drone Type";
    	}
    	
    	// Extract ID or details from the URL if needed
    	return rawDroneType.replace("http://dronesim.facets-labs.com/api/dronetypes/", "Type-").replace("/", "");
    	
    }
}

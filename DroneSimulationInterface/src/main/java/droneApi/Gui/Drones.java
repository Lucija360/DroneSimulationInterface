package droneApi.Gui;

import droneApi.Entities.Drone;
import droneApi.Service.DroneApiService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Drones extends JFrame {
	
	private static final Logger logger = LoggerFactory.getLogger(Drone.class);	// Logger defined here at the class level
																				// logs message for entire class

    private JTable dronesTable; // Table for displaying drone data
    private DefaultTableModel tableModel; // Table model for dynamic updates
    
    // DateTimeFormatter for formatting timestamps
    private static final DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM. dd, yyyy, h:mm a");

    private final DroneApiService droneApiService;	//Injected service to fetch data
    
    public Drones() {
    	this.droneApiService = new DroneApiService();	//Initialize service instance
    	
        setTitle("Drone Simulation - Drones");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Header label
        JLabel headerLabel = new JLabel("Drones", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Table for displaying drone data
        String[] columnNames = {"ID", "Dronetype", "Created", "Serialnumber", "Carriage Weight", "Carriage Type"};
        Object[][] data = {
            {1, "Quadcopter", "2025-01-01", "SN12345", 15.5, "Package"},
            {2, "Hexacopter", "2025-01-02", "SN67890", 20.0, "Medical"},
            {3, "Octocopter", "2025-01-03", "SN54321", 25.0, "Survey"}
        };

        // Initialize table model and table
        tableModel = new DefaultTableModel(data, columnNames);
        dronesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(dronesTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Back to Main Menu button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
            dispose(); // Close the Drones window
        });
        buttonPanel.add(backButton);

        // Fetch Drones from API button
        JButton fetchButton = new JButton("Fetch Drones from API");
        fetchButton.addActionListener(e -> fetchAndDisplayDrones());
        buttonPanel.add(fetchButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);
    }

    /**
     * Fetch drone data from the API and display it in the table.
     */
    private void fetchAndDisplayDrones() {
        try {
            // Fetch drones using the DroneController API endpoint
            List<Drone> drones = droneApiService.fetchDrones(10, 0); // Fetch data from the service
            														// with a limit and offset

            // Clear existing rows in the table model
            tableModel.setRowCount(0);

            // Add fetched drones to the table model
            for (Drone drone : drones) {
            	String droneTypeName = fetchandformatDroneType(drone.getDronetypeRaw());	//Format raw data for human-readable drone type name
            	String formattedDate = formatDate(drone.getCreatedRaw());	// Format createdRaw          	
                           
            	Object[] row = {
                    drone.getId(),
                    droneTypeName,	// Human-readable drone type name
                    formattedDate,	//Formatted date for display
                    drone.getSerialNumber(),
                    drone.getCarriageWeight(),
                    drone.getCarriageType()
                };
                tableModel.addRow(row);
            }

            JOptionPane.showMessageDialog(this, "Drones fetched successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
        	logger.error("Error fetching drones: {}", ex.getMessage());
            JOptionPane.showMessageDialog(this, "Failed to fetch drones: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Fetch drones from the DroneController API.
     * @param limit The number of drones to fetch.
     * @param offset The starting point for fetching drones.
     * @return List of Drone objects.
     */
    private List<Drone> fetchDronesFromApi(int limit, int offset) {
        List<Drone> drones = new ArrayList<>();
        String apiUrl = "http://localhost:8080/api/drones/?limit=" + limit + "&offset=" + offset;

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Drone[]> response = restTemplate.getForEntity(apiUrl, Drone[].class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                for (Drone drone : response.getBody()) {
                    drones.add(drone);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error fetching drones from API: " + ex.getMessage(), ex);
        }

        return drones;
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
     * Fetch and formats the raw dronetype URL into a human-readable format.
     * @param rawDroneType		The raw dronetype URL.
     * @return A formatted dronetype name.
     */
    private String fetchandformatDroneType(String rawDroneType) {
    	if (rawDroneType == null || rawDroneType.isEmpty()) {
    		return "Unknown Drone Type";
    	} 
    	try {
    		return droneApiService.getDroneTypeName(rawDroneType); // Resolve name via service
    	} catch (Exception ex) {
	        logger.error("Error fetching drone type name: {}", ex.getMessage());
	        return "Unknown Drone Type";
    }
}
    	
    	

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Drones drones = new Drones();
            drones.setVisible(true);
        });
    }
}

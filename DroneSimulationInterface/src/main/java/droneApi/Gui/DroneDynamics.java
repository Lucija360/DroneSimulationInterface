package droneApi.Gui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVWriter;

import droneApi.Service.DroneApiService;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DroneDynamics extends JFrame {

	private JTable dynamicsTable;
    private DefaultTableModel tableModel;
    
    private final DroneApiService droneApiService;	//Injected service to fetch data
    
    public DroneDynamics() {
    	this.droneApiService = new DroneApiService();	// Initialize service instance
    	
        setTitle("Drone Simulation - Drone Dynamics");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Drone Dynamics Layout
        JPanel dynamicsPanel = new JPanel();
        dynamicsPanel.setLayout(new BorderLayout(10, 10));

        // Drone Dynamics Header
        JLabel headerLabel = new JLabel("Drone Dynamics", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        dynamicsPanel.add(headerLabel, BorderLayout.NORTH);

        // Table for Drone Dynamics Data
        String[] columnNames = {
            "Drone", "Timestamp", "Speed", "Roll", "Pitch", "Yaw",
            "Longitude", "Latitude", "Battery (mAh)", "Last Seen", "Status"
        };
        tableModel = new DefaultTableModel(columnNames, 0);
        dynamicsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(dynamicsTable);
        dynamicsPanel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));

        // Refresh Button
        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.addActionListener(e -> fetchAndDisplayDroneDynamics(true));
        buttonPanel.add(refreshButton);
        
        JButton exportButton = new JButton("Export to CSV");
        exportButton.addActionListener(e -> exportDroneDynamicsToCSV());
        buttonPanel.add(exportButton);


        // Back to main menu button
        JButton backToMenuButton = new JButton("Back to Main Menu");
        backToMenuButton.addActionListener(e -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
            dispose(); // Close the Drone Dynamics window
        });
        buttonPanel.add(backToMenuButton);

        dynamicsPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(dynamicsPanel);
        setLocationRelativeTo(null);

        // Fetch data automatically on page load (without message)
        fetchAndDisplayDroneDynamics(false);
    }

    /**
     * Fetches and displays drone dynamics from the API.
     * This method retrieves drone dynamics data and populates the table for visualization.
     * @param showMessage Whether to show success or error messages.
     */
    private void fetchAndDisplayDroneDynamics(boolean showMessage) {
        try {
        	
        	// Fetch a list of drone dynamics data from the API with pagination (limit = 10, offset = 0)
            List<droneApi.Entities.DroneDynamics> dynamics = droneApiService.fetchDroneDynamics(10, 0);

            // Clear any existing rows in the table before inserting new data
            tableModel.setRowCount(0);

            // Populate the table with fetched data
            for (droneApi.Entities.DroneDynamics drone : dynamics) {
            	String formattedTimestamp = droneApiService.getFormatDate(drone.getTimestamp());	//Format Timestamp
                String formattedLastSeen = droneApiService.getFormatDate(drone.getLastSeen());	// Format LastSeen
            	String droneTypeName = droneApiService.getDroneDetails(drone.getDrone());	//Format raw data for human-readable
            	String droneDetails = droneApiService.getDroneDetails(drone.getDrone());	//Format data
            	
            	Object[] row = {
                    droneDetails,	// Formatted drone field with adjusted created timestamp
                    formattedTimestamp,	//Human-readable timestamp
                    drone.getSpeed() + " km/h",
                    drone.getAlignRoll(),
                    drone.getAlignPitch(),
                    drone.getAlignYaw(),
                    drone.getLongitude(),
                    drone.getLatitude(),
                    drone.getBatteryStatus() + " mAh",  // Displaying battery in mAh
                    formattedLastSeen,	//Human-readable Last seen
                    drone.getStatus()
                };
                // Add the populated row to the table model for display
                tableModel.addRow(row);
            }
            
            // If showMessage is true, display a success message to the user
            if (showMessage) {
                JOptionPane.showMessageDialog(this, "Drone dynamics data refreshed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
        	
        	// Show an error message if fetching data fails
            JOptionPane.showMessageDialog(this, "Failed to fetch drone dynamics: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Fetch drone dynamics from the DroneController API.
     * This method makes an HTTP request to retrieve drone dynamics data.
     * @param limit Number of results to fetch.
     * @param offset Index to start fetching from.
     * @return List of DroneDynamics objects.
     */
    private List<droneApi.Entities.DroneDynamics> fetchDroneDynamicsFromApi(int limit, int offset) {
    	
    	// Create an empty list to store the drone dynamics data
        List<droneApi.Entities.DroneDynamics> dynamics = new ArrayList<>();
        
        // Create an empty list to store the drone dynamics data
        String apiUrl = "http://localhost:8080/api/dronedynamics/?limit=" + limit + "&offset=" + offset;
        
        try {
        	// Create an instance of RestTemplate to perform the HTTP request
            RestTemplate restTemplate = new RestTemplate();
            
            // Make a GET request to the API and receive a response containing an array of DroneDynamics objects
            ResponseEntity<droneApi.Entities.DroneDynamics[]> response = restTemplate.getForEntity(apiUrl, droneApi.Entities.DroneDynamics[].class);

            // Check if the API response was successful (HTTP 2xx codes) and contains a valid response body
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                for (droneApi.Entities.DroneDynamics drone : response.getBody()) {
                    dynamics.add(drone);
                }
            }
        } catch (Exception ex) {
        	
        	// If an error occurs during the API call, throw a runtime exception with details
            throw new RuntimeException("Error fetching drone dynamics from API: " + ex.getMessage(), ex);
        }

        // Return the list of fetched drone dynamics data
        return dynamics;
    }
    
    
    /**
     * Exports the drone dynamics data displayed in the table to a CSV file.
     */
    private void exportDroneDynamicsToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Drone Dynamics Data to CSV");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath() + ".csv";

            try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
                // Improved headers with better readability
                String[] header = {
                    "Drone ID", "Timestamp", "Speed (km/h)", "Roll (°)", "Pitch (°)", "Yaw (°)",
                    "Longitude", "Latitude", "Battery Status (mAh)", "Last Seen", "Status"
                };
                writer.writeNext(header);

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm a");

                // Writing formatted data rows
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String[] row = new String[tableModel.getColumnCount()];

                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        Object value = tableModel.getValueAt(i, j);
                        if (value instanceof Date) {
                            row[j] = dateFormat.format((Date) value);  // Format date for readability
                        } else if (value instanceof Number) {
                            row[j] = String.format("%.2f", ((Number) value).doubleValue());  // Format numbers
                        } else {
                            row[j] = value.toString();
                        }
                    }

                    writer.writeNext(row);
                }

                JOptionPane.showMessageDialog(this, "Drone dynamics exported successfully to:\n" + filePath, "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error exporting drone dynamics: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DroneDynamics dynamics = new DroneDynamics();
            dynamics.setVisible(true);
        });
    }
}
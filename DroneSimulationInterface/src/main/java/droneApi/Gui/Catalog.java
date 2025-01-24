package droneApi.Gui;

import droneApi.Entities.DroneType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class Catalog extends JFrame {

	  private JTable droneTypesTable;
	  private DefaultTableModel tableModel;
	
	  public Catalog() {
    	 setTitle("Drone Types");
         setSize(900, 500);
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

         // Main panel layout
         JPanel catalogPanel = new JPanel();
         catalogPanel.setLayout(new BorderLayout(10, 10));

         // Header label
         JLabel headerLabel = new JLabel("Drone Types", SwingConstants.CENTER);
         headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
         catalogPanel.add(headerLabel, BorderLayout.NORTH);

         // Table columns
         String[] columnNames = {
             "ID", "Manufacturer", "Typename", "Weight", "Max Speed", "Battery Capacity", "Control Range", "Max Carriage"
         };

         tableModel = new DefaultTableModel(columnNames, 0);
         droneTypesTable = new JTable(tableModel);

         JScrollPane scrollPane = new JScrollPane(droneTypesTable);
         catalogPanel.add(scrollPane, BorderLayout.CENTER);

         // Button panel
         JPanel buttonPanel = new JPanel();
         buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));

         JButton viewFlightDynamicsButton = new JButton("View Flight Dynamics");
         viewFlightDynamicsButton.setEnabled(false);
         viewFlightDynamicsButton.addActionListener(e -> {
             int selectedRow = droneTypesTable.getSelectedRow();
             if (selectedRow != -1) {
                 String selectedDroneId = droneTypesTable.getValueAt(selectedRow, 0).toString();
                 FlightDynamics flightDynamics = new FlightDynamics(selectedDroneId);
                 flightDynamics.setVisible(true);
                 dispose();
             }
         });

         droneTypesTable.getSelectionModel().addListSelectionListener(e -> {
             if (!e.getValueIsAdjusting()) {
                 viewFlightDynamicsButton.setEnabled(droneTypesTable.getSelectedRow() != -1);
             }
         });

         JButton backToMenuButton = new JButton("Back to Main Menu");
         backToMenuButton.addActionListener(e -> {
             MainMenu mainMenu = new MainMenu();
             mainMenu.setVisible(true);
             dispose();
         });

         buttonPanel.add(viewFlightDynamicsButton);
         buttonPanel.add(backToMenuButton);

         catalogPanel.add(buttonPanel, BorderLayout.SOUTH);

         add(catalogPanel);
         setLocationRelativeTo(null);

         // Load drone types on startup
         fetchAndDisplayDroneTypes();
     }

    
	  /**
	   * Fetches drone types from the API and populates the table.
	   * This method retrieves a list of drone types and displays them in a GUI table.
	   */
     private void fetchAndDisplayDroneTypes() {
         try {
        	 
        	 // Fetch a list of drone types from the API with pagination (limit = 10, offset = 0)
             List<DroneType> droneTypes = fetchDroneTypesFromApi(10, 0);

         	 // Clear all existing rows in the table before adding new data
             tableModel.setRowCount(0);
             
             // Loop through the list of drone types and add them to the table model
             for (DroneType droneType : droneTypes) {
                 Object[] row = {
                     droneType.getId(),
                     droneType.getManufacturer(),
                     droneType.getTypename(),
                     droneType.getWeight() + " g",
                     droneType.getMaxSpeed() + " km/h",
                     droneType.getBatteryCapacity() + " mAh",
                     droneType.getControlRange() + " m",
                     (droneType.getMaxCarriage() != null) ? droneType.getMaxCarriage() + " g" : "N/A"
                 };
                 
                 // Add the row containing drone type details to the table
                 tableModel.addRow(row);
             }
         } catch (Exception ex) {
        	 
        	 // Show an error message dialog in case of failure
             JOptionPane.showMessageDialog(this, "Failed to fetch drone types: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
         }
     }
     

     /**
	  * Fetch drones from the DroneController API.
	  * @param limit Number of results to fetch.
	  * @param offset Index to start fetching from.
	  * @return List of Drone objects.
	  */
     private List<DroneType> fetchDroneTypesFromApi(int limit, int offset) {
    	 
    	 // Construct the API URL with pagination parameters
         String apiUrl = "http://localhost:8080/api/dronetypes/?limit=" + limit + "&offset=" + offset;
         
         // Create a new RestTemplate instance to perform HTTP requests
         RestTemplate restTemplate = new RestTemplate();

         try {
        	 // Make a GET request to fetch drone types and store the response
             ResponseEntity<DroneType[]> response = restTemplate.getForEntity(apiUrl, DroneType[].class);
             
             // Check if the response status code is 2xx (success) and the response body is not null
             if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            	 
            	 // Convert the array of DroneType objects to a List and return
                 return List.of(response.getBody());
             }
         } catch (Exception ex) {
        	 
        	 // Handle any exceptions by throwing a runtime exception with details
             throw new RuntimeException("Error fetching drone types from API: " + ex.getMessage(), ex);
         }
         // Return an empty list if no data is retrieved or an error occurs
         return List.of();
     }

     public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> {
             Catalog droneTypes = new Catalog();
             droneTypes.setVisible(true);
         });
     }
}

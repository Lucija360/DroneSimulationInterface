package droneApi.Gui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DroneDynamics extends JFrame {

	private JTable dynamicsTable;
    private DefaultTableModel tableModel;

    public DroneDynamics() {
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
     * @param showMessage Whether to show success or error messages.
     */
    private void fetchAndDisplayDroneDynamics(boolean showMessage) {
        try {
            List<droneApi.Entities.DroneDynamics> dynamics = fetchDroneDynamicsFromApi(10, 0);

            // Clear existing rows
            tableModel.setRowCount(0);

            // Populate the table with fetched data
            for (droneApi.Entities.DroneDynamics drone : dynamics) {
                Object[] row = {
                    drone.getDrone(),
                    drone.getTimestamp(),
                    drone.getSpeed() + " km/h",
                    drone.getAlignRoll(),
                    drone.getAlignPitch(),
                    drone.getAlignYaw(),
                    drone.getLongitude(),
                    drone.getLatitude(),
                    drone.getBatteryStatus() + " mAh",  // Displaying battery in mAh
                    drone.getLastSeen(),
                    drone.getStatus()
                };
                tableModel.addRow(row);
            }

            if (showMessage) {
                JOptionPane.showMessageDialog(this, "Drone dynamics data refreshed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to fetch drone dynamics: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Fetch drone dynamics from the DroneController API.
     * @param limit Number of results to fetch.
     * @param offset Index to start fetching from.
     * @return List of DroneDynamics objects.
     */
    private List<droneApi.Entities.DroneDynamics> fetchDroneDynamicsFromApi(int limit, int offset) {
        List<droneApi.Entities.DroneDynamics> dynamics = new ArrayList<>();
        String apiUrl = "http://localhost:8080/api/dronedynamics/?limit=" + limit + "&offset=" + offset;
        
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<droneApi.Entities.DroneDynamics[]> response = restTemplate.getForEntity(apiUrl, droneApi.Entities.DroneDynamics[].class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                for (droneApi.Entities.DroneDynamics drone : response.getBody()) {
                    dynamics.add(drone);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error fetching drone dynamics from API: " + ex.getMessage(), ex);
        }

        return dynamics;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DroneDynamics dynamics = new DroneDynamics();
            dynamics.setVisible(true);
        });
    }
}
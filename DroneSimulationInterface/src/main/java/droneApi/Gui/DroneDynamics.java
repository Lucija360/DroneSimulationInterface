package droneApi.Gui;

import javax.swing.*;
import java.awt.*;

public class DroneDynamics extends JFrame {

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
        String[] columnNames = {"ID", "Timestamp", "Drone", "Speed", "Alignment Roll", "Control Range", "Alignment Yaw", "Longitude", "Latitude", "Battery Status", "Last Seen", "Status"};
        Object[][] data = {
            {"1", "2025-01-01T12:00:00", "Drone A", "50 km/h", "0.0", "2 km", "0.0", "50.1109", "8.6821", "80%", "2025-01-01T12:00:00", "Active"},
            {"2", "2025-01-01T12:05:00", "Drone B", "60 km/h", "0.5", "3 km", "0.3", "50.1200", "8.6900", "75%", "2025-01-01T12:05:00", "Active"},
            {"3", "2025-01-01T12:10:00", "Drone C", "45 km/h", "-0.2", "1.5 km", "0.1", "50.1300", "8.7000", "85%", "2025-01-01T12:10:00", "Inactive"}
        };
        JTable dynamicsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(dynamicsTable);

        // Adding the table to the center of the panel
        dynamicsPanel.add(scrollPane, BorderLayout.CENTER);

        // Back to main menu button
        JButton backToMenuButton = new JButton("Back to Main Menu");
        backToMenuButton.addActionListener(e -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
            dispose(); // Close the Drone Dynamics window
        });

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 1, 10, 10));
        buttonPanel.add(backToMenuButton);

        dynamicsPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(dynamicsPanel);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DroneDynamics dynamics = new DroneDynamics();
            dynamics.setVisible(true);
        });
    }
}

package droneApi.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Catalog extends JFrame {

    public Catalog() {
        setTitle("Drone Catalog");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Catalog Panel
        JPanel catalogPanel = new JPanel();
        catalogPanel.setLayout(new BorderLayout(10, 10));

        // Catalog Title
        JLabel headerLabel = new JLabel("Drone Catalog", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        catalogPanel.add(headerLabel, BorderLayout.NORTH);

        // Catalog Data
        String[] columnNames = {"ID", "Manufacturer", "Typename", "Weight", "Maximum Speed", "Battery Capacity", "Control Range", "Maximum Carriage"};
        Object[][] data = {
            {"1", "Company A", "Drone X", "2.5 kg", "50 km/h", "3000 mAh", "2 km", "1.5 kg"},
            {"2", "Company B", "Drone Y", "3.0 kg", "60 km/h", "3500 mAh", "3 km", "2.0 kg"},
            {"3", "Company C", "Drone Z", "2.0 kg", "45 km/h", "2800 mAh", "1.5 km", "1.2 kg"}
        };
        JTable catalogTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(catalogTable);
        catalogPanel.add(scrollPane, BorderLayout.CENTER);

        // Flight Dynamics Button
        JButton viewFlightDynamicsButton = new JButton("View Flight Dynamics");
        viewFlightDynamicsButton.setEnabled(false); // Initially disabled
        viewFlightDynamicsButton.addActionListener(e -> {
            int selectedRow = catalogTable.getSelectedRow();
            if (selectedRow != -1) {
                String selectedDrone = (String) catalogTable.getValueAt(selectedRow, 0);
                FlightDynamics flightDynamics = new FlightDynamics(selectedDrone);
                flightDynamics.setVisible(true);
                dispose(); // Closes the catalog screen
            }
        });

        // Selection Listener
        catalogTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                viewFlightDynamicsButton.setEnabled(catalogTable.getSelectedRow() != -1);
            }
        });

        // Back to Main Menu Button
        JButton backToMenuButton = new JButton("Back to Main Menu");
        backToMenuButton.addActionListener(e -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(viewFlightDynamicsButton);
        buttonPanel.add(backToMenuButton);

        catalogPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(catalogPanel);
        setLocationRelativeTo(null);
    }
}

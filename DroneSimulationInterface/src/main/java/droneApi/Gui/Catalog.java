package droneApi.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Catalog extends JFrame {

    public Catalog() {
        setTitle("Drone Catalog");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Catalog Panel
        JPanel catalogPanel = new JPanel();
        catalogPanel.setLayout(new BorderLayout(10, 10));

        // Catalog Title
        JLabel headerLabel = new JLabel("Drone Catalog", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        catalogPanel.add(headerLabel, BorderLayout.NORTH);

        // Catalog Data (Placeholder)
        String[] columnNames = {"Model", "Max Speed", "Manufacturer"};
        Object[][] data = {
            {"Drone X", "50 km/h", "Company A"},
            {"Drone Y", "60 km/h", "Company B"},
            {"Drone Z", "45 km/h", "Company C"}
        };
        JTable catalogTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(catalogTable);

        catalogPanel.add(scrollPane, BorderLayout.CENTER);

        // Flight Dynamics Button
        JButton viewFlightDynamicsButton = new JButton("View Flight Dynamics");
        viewFlightDynamicsButton.setEnabled(false); // Initially disabled
        viewFlightDynamicsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = catalogTable.getSelectedRow();
                if (selectedRow != -1) {
                    String selectedDrone = (String) catalogTable.getValueAt(selectedRow, 0);
                    FlightDynamics flightDynamics = new FlightDynamics(selectedDrone);
                    flightDynamics.setVisible(true);
                    dispose(); // Closes the catalog screen
                }
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

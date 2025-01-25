package droneApi.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FlightDynamics extends JFrame {

    public FlightDynamics(String droneName) {
        setTitle("Flight Dynamics - " + droneName);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Flight Dynamics Panel
        JPanel dynamicsPanel = new JPanel();
        dynamicsPanel.setLayout(new BorderLayout(10, 10));

        // Title
        JLabel headerLabel = new JLabel("Flight Dynamics for " + droneName, SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        dynamicsPanel.add(headerLabel, BorderLayout.NORTH);

        // Placeholder for Flight Data
        JTextArea flightDataArea = new JTextArea("Real-time flight data for " + droneName + " will be displayed here.");
        flightDataArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(flightDataArea);

        dynamicsPanel.add(scrollPane, BorderLayout.CENTER);

        // Refresh Button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Refreshing flight dynamics (placeholder)
                flightDataArea.setText("Fetching updated data for " + droneName + "...\n[Sample Data]");
                JOptionPane.showMessageDialog(null, "Flight data refreshed!");
                // Code to fetch data from the API can be added here.
            }
        });

        // Back Buttons
        JButton backToCatalogButton = new JButton("Back to Catalog");
        backToCatalogButton.addActionListener(e -> {
            Catalog catalog = new Catalog();
            catalog.setVisible(true);
            dispose();
        });

        JButton backToMenuButton = new JButton("Back to Main Menu");
        backToMenuButton.addActionListener(e -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
            dispose();
        });

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10)); // GridLayout for three buttons
        buttonPanel.add(refreshButton);
        buttonPanel.add(backToCatalogButton);
        buttonPanel.add(backToMenuButton);

        dynamicsPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(dynamicsPanel);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlightDynamics flightDynamics = new FlightDynamics("Example Drone");
            flightDynamics.setVisible(true);
        });
    }
}

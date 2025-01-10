package droneApi.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Drones extends JFrame {

    public Drones() {
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
        JTable dronesTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(dronesTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Back to Main Menu button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu mainMenu = new MainMenu();
                mainMenu.setVisible(true);
                dispose(); // Close the Drones window
            }
        });
        mainPanel.add(backButton, BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Drones drones = new Drones();
            drones.setVisible(true);
        });
    }
}

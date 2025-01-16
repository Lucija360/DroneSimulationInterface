package droneApi.Gui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Drone Simulation - Main Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1, 10, 10));

        // Drone Catalog Button
        JButton catalogButton = new JButton("Drone Catalog");
        catalogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Transition to the Catalog screen
                Catalog catalog = new Catalog();
                catalog.setVisible(true);
                dispose(); // Closes the main menu
            }
        });

        // Drone Dynamics Button
        JButton dynamicsButton = new JButton("Drone Dynamics");
        dynamicsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Transition to the Drone Dynamics screen
                DroneDynamics dynamics = new DroneDynamics();
                dynamics.setVisible(true);
                dispose(); // Closes the main menu
            }
        });

        // Drones Button
        JButton dronesButton = new JButton("Drones");
        dronesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Transition to the Drones screen
                Drones drones = new Drones();
                drones.setVisible(true);
                dispose(); // Closes the main menu
            }
        });
        
     // Drone Dashboard Button
        JButton dashboardButton = new JButton("Drone Dashboard");
        dashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Transition to the Drone Dashboard screen
                DroneDashboard dashboard = new DroneDashboard();
                dashboard.setVisible(true);
                dispose(); // Closes the main menu
            }
        });

        
        // Add buttons to the main panel
        mainPanel.add(catalogButton);
        mainPanel.add(dynamicsButton);
        mainPanel.add(dronesButton);
        mainPanel.add(dashboardButton);
        add(mainPanel);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
        });
    }
}

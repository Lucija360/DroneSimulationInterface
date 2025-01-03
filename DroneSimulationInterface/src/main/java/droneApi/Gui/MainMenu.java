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
                dispose(); //Closes the main menu
            }
        });

        // Drone Dashboard Button
        JButton dashboardButton = new JButton("Drone Dashboard");
        dashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Transition to the Dashboard screen
                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);
                dispose(); // Closes the main menu
            }
        });

        // Exit Button
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        // Add buttons to the main panel
        mainPanel.add(catalogButton);
        mainPanel.add(dashboardButton);
        mainPanel.add(exitButton);

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

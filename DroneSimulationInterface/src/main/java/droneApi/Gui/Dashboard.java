package droneApi.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {

    public Dashboard() {
        setTitle("Drone Simulation - Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Dashboard Layout
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BorderLayout(10, 10));

        // Dashboard Header
        JLabel headerLabel = new JLabel("Drone Dashboard", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        dashboardPanel.add(headerLabel, BorderLayout.NORTH);

        // Refresh Button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Refresh (placeholder)
                JOptionPane.showMessageDialog(null, "Refreshing data...");
            }
        });

        // Back to main menu button
        JButton backToMenuButton = new JButton("Back to Main Menu");
        backToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Back to main menu
                MainMenu mainMenu = new MainMenu();
                mainMenu.setVisible(true);
                dispose(); // Dashboard windows will close 
            }
        });

        // Button Panel (Refresh and back to main menu buttons)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(refreshButton);
        buttonPanel.add(backToMenuButton);

        // Main Content Placeholder
        JTextArea contentArea = new JTextArea("Real-time drone data will be displayed here...");
        contentArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(contentArea);

        dashboardPanel.add(scrollPane, BorderLayout.CENTER);
        dashboardPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(dashboardPanel);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Dashboard dashboard = new Dashboard();
            dashboard.setVisible(true);
        });
    }
}

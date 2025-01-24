package droneApi.Gui;

import java.awt.GridLayout;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;

import droneApi.Entities.Drone;

class DroneDetailsWindow extends JFrame {
    public DroneDetailsWindow(Drone drone) {
        setTitle("Drone Details");
        setSize(400, 300);
        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel("Drone ID:"));
        add(new JLabel(String.valueOf(drone.getId())));

        add(new JLabel("Dronetype:"));
        add(new JLabel(drone.getDronetype()));

        add(new JLabel("Created:"));
        add(new JLabel(new SimpleDateFormat("MMM. dd, yyyy, h:mm a").format(drone.getCreated())));

        add(new JLabel("Serial Number:"));
        add(new JLabel(drone.getSerialNumber()));

        add(new JLabel("Carriage Weight:"));
        add(new JLabel(drone.getCarriageWeight() + " g"));

        add(new JLabel("Carriage Type:"));
        add(new JLabel(drone.getCarriageType()));

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

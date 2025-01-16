package droneApi.Gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DroneDashboard extends JFrame {

    public DroneDashboard() {
        setTitle("Drone Dashboard");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // main layout with BorderLayout
        setLayout(new BorderLayout(10, 10));

        // HEADER (NORTH)
        JLabel headerLabel = new JLabel("Drone Dashboard", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(headerLabel, BorderLayout.NORTH);	// add Header

        // Center Panel (Panels in a 2x2 Grid)
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // distance between the Panels

        // Table Panel
        
        // calculations for the table
        // total traveled distance(in km) = speed(in km/h) * time difference inbetween timestamps(in hours)
        // average speed over time(in km/h) = sum of all speeds(in km/h)/number of all timestamp speed measures
        // battery consumption per km = battery status(in %)/total traveled distance(in km)
        // payload utilization(in %) = carriage weight/maximum carriage * 100
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] columnNames = {"ID", "Dronetype", "Created", "Serialnumber", "Carriage Weight", "Carriage Type", 
                                "Average speed over time", "Total distance traveled for each drone", 
                                "Battery consumption per km", "Payload utilization"};
        Object[][] data = {
                {1, "Quadcopter", "2025-01-01", "SN12345", 15.5, "Package", 20, 200, 40, 70},
                {2, "Hexacopter", "2025-01-02", "SN67890", 20.0, "Medical", 10, 450, 50, 70},
                {3, "Octocopter", "2025-01-03", "SN54321", 25.0, "Survey", 20, 300, 30, 90}
        };
        JTable dynamicsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(dynamicsTable);
        tablePanel.add(new JLabel("Drone Data Table", SwingConstants.CENTER), BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(tablePanel);
        
        // Pie Chart Panel
        JPanel pieChartPanel = new JPanel(new BorderLayout());
        pieChartPanel.add(new JLabel("Manufacturer", SwingConstants.CENTER), BorderLayout.NORTH);
        pieChartPanel.add(new PieChartPanel(), BorderLayout.CENTER);
        centerPanel.add(pieChartPanel);
        
        // Bar Chart Panel
        int[] values = {20, 15, 30, 45};
        String[] labels = {"Drone A", "Drone B", "Drone C", "Drone D"}; 
        Color[] colors = {Color.RED, Color.GRAY, Color.WHITE, Color.BLUE};

        BarChartPanel barChartPanel = new BarChartPanel(values, labels, colors);
        barChartPanel.add(new JLabel("Average speed", SwingConstants.CENTER), BorderLayout.NORTH);
        barChartPanel.setPreferredSize(new Dimension(400, 300));
        centerPanel.add(barChartPanel);

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(new JLabel("Info Panel", SwingConstants.CENTER));
        infoPanel.add(new JLabel("Battery Status: 85%"));
        infoPanel.add(new JLabel("Current Speed: 15 km/h"));
        infoPanel.add(new JLabel("Payload Utilization: 70%"));
        centerPanel.add(infoPanel);

        add(centerPanel, BorderLayout.CENTER);	// add Panels to Center Panel
        
        centerPanel.setBackground(Color.LIGHT_GRAY);	// set Background color

        // FOOTER (SOUTH)
        JPanel footerPanel = new JPanel();
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> JOptionPane.showMessageDialog(null, "Data refreshed!"));
        footerPanel.add(refreshButton); // Refresh-Button

        JButton backToMenuButton = new JButton("Back to Main Menu");
        footerPanel.add(backToMenuButton); // Back-Button
        backToMenuButton.addActionListener(e -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
            dispose();
        });
        add(footerPanel, BorderLayout.SOUTH); // add Buttons to Footer Panel        
        
        setLocationRelativeTo(null); // center frame
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DroneDashboard dashboard = new DroneDashboard();
            dashboard.setVisible(true);
        });
    }
}

class PieChartPanel extends JPanel {
    private Slice[] slices = {
            new Slice(25, Color.red, "Company A"),
            new Slice(20, Color.blue, "Company B"),
            new Slice(23, Color.white, "Company C"),
            new Slice(32, Color.gray, "Company D")
    };

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(getWidth(), getHeight()) - 20;
        Rectangle area = new Rectangle((getWidth() - size) / 2, (getHeight() - size) / 2, size, size);
        drawPie(g2d, area, slices);
    }

    private void drawPie(Graphics2D g, Rectangle area, Slice[] slices) {
        double total = 0.0D;
        for (Slice slice : slices) {
            total += slice.value;
        }

        double curValue = 0.0D;
        int startAngle;

        for (Slice slice : slices) {
            startAngle = (int) (curValue * 360 / total);
            int arcAngle = (int) (slice.value * 360 / total);

            if (curValue + slice.value == total) {
                arcAngle = 360 - startAngle;
            }

            g.setColor(slice.color);
            g.fillArc(area.x, area.y, area.width, area.height, startAngle, arcAngle);

            double angle = Math.toRadians(startAngle + arcAngle / 2.0);
            double labelRadius = area.width / 3.0;

            int labelX = (int) (area.getCenterX() + Math.cos(angle) * labelRadius);
            int labelY = (int) (area.getCenterY() - Math.sin(angle) * labelRadius);

            FontMetrics metrics = g.getFontMetrics();
            int textWidth = metrics.stringWidth(slice.description);
            int textHeight = metrics.getAscent();

            g.setColor(Color.black);
            g.drawString(slice.description, labelX - textWidth / 2, labelY + textHeight / 2);

            curValue += slice.value;
        }
    }
}

class Slice {
    double value;
    Color color;
    String description;

    public Slice(double value, Color color, String description) {
        this.value = value;
        this.color = color;
        this.description = description;
    }
}

class BarChartPanel extends JPanel {
    private int[] values; 
    private String[] labels; 
    private Color[] colors; 

    public BarChartPanel(int[] values, String[] labels, Color[] colors) {
        this.values = values;
        this.labels = labels;
        this.colors = colors;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (values == null || values.length == 0) {
            g.drawString("No data available", getWidth() / 2 - 50, getHeight() / 2);
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 50; 	// More space for Y-axis labeling
        int barWidth = (width - 2 * padding) / values.length;
        int maxBarHeight = height - 2 * padding;

        // Calculate Maximum Value to Determine Scale 
        int maxValue = 0;
        for (int value : values) {
            if (value > maxValue) maxValue = value;
        }

        // Draw Y-axis labeling
        int ySteps = 5; // Number of steps on the Y-axis
        int stepValue = maxValue / ySteps;
        for (int i = 0; i <= ySteps; i++) {
            int y = height - padding - (i * maxBarHeight / ySteps);

            // Line and labeling
            g2d.setColor(Color.GRAY);
            g2d.drawLine(padding - 5, y, width - padding, y); // Horizontal line

            g2d.setColor(Color.BLACK);
            String label = Integer.toString(i * stepValue);
            g2d.drawString(label, padding - 35, y + 5); // Y-lableing (left)
        }

        // Draw bars
        for (int i = 0; i < values.length; i++) {
            int barHeight = (int) ((double) values[i] / maxValue * maxBarHeight);
            int x = padding + i * barWidth;
            int y = height - padding - barHeight;

            g2d.setColor(colors[i % colors.length]);
            g2d.fillRect(x, y, barWidth - 10, barHeight);

            // Description of the bars
            g2d.setColor(Color.BLACK);
            g2d.drawString(labels[i], x + (barWidth - 10) / 2 - g2d.getFontMetrics().stringWidth(labels[i]) / 2, height - padding + 15);
        }

        // Draw axis
        g2d.setColor(Color.BLACK);
        g2d.drawLine(padding, height - padding, width - padding, height - padding); // X-Achse
        g2d.drawLine(padding, padding, padding, height - padding); // Y-Achse
    }
} 
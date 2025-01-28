package droneApi.Gui;

import droneApi.Entities.Drone;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DroneDashboard extends JFrame {

	 private JTable dynamicsTable;
	 private DefaultTableModel tableModel;

	    public DroneDashboard() {
	        setTitle("Drone Dashboard");
	        setSize(900, 700);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        // Main layout with BorderLayout
	        setLayout(new BorderLayout(10, 10));

	        // HEADER (NORTH)
	        JLabel headerLabel = new JLabel("Drone Dashboard", SwingConstants.CENTER);
	        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
	        add(headerLabel, BorderLayout.NORTH);

	        // Center Panel (Panels in a 2x2 Grid)
	        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
	        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Ensure spacing

	        // Table Panel
	        
	        // calculations for the table
	        // total traveled distance(in km) = speed(in km/h) * time difference inbetween timestamps(in hours)
	        // average speed over time(in km/h) = sum of all speeds(in km/h)/number of all timestamp speed measures
	        // battery consumption per km = battery status(in %)/total traveled distance(in km)
	        // payload utilization(in %) = carriage weight/maximum carriage * 100
	        
	        // Table Panel with BorderLayout
	        JPanel tablePanel = new JPanel(new BorderLayout());
	        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Maintain gray spacing

	        // Column names and table initialization
	        String[] columnNames = {"ID", "Dronetype", "Created", "Serialnumber", "Carriage Weight",
	                "Carriage Type", "Average speed over time", "Total distance traveled",
	                "Battery consumption per km", "Payload utilization"};

	        tableModel = new DefaultTableModel(columnNames, 0);
	        dynamicsTable = new JTable(tableModel);
	        
	     // Add MouseListener to the table to handle row clicks
	        dynamicsTable.addMouseListener(new java.awt.event.MouseAdapter() {
	            @Override
	            public void mouseClicked(java.awt.event.MouseEvent evt) {
	                int row = dynamicsTable.rowAtPoint(evt.getPoint());
	                if (row >= 0) {
	                    int droneId = (int) tableModel.getValueAt(row, 0);
	                    showDroneDetails(droneId);
	                }
	            }
	        });

	        // Scroll pane for the table with fixed preferred size
	        JScrollPane scrollPane = new JScrollPane(dynamicsTable);
	        scrollPane.setPreferredSize(new Dimension(600, 150));  // Fixed size to prevent overflow

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

	        add(centerPanel, BorderLayout.CENTER);
	        
	        centerPanel.setBackground(Color.LIGHT_GRAY);

	        // FOOTER (SOUTH)
	        JPanel footerPanel = new JPanel();
	        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

	        JButton refreshButton = new JButton("Refresh Data");
	        refreshButton.addActionListener(e -> {
	            fetchAndDisplayDrones();
	            JOptionPane.showMessageDialog(this, "Data refreshed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
	        });
	        footerPanel.add(refreshButton);

	        JButton exportButton = new JButton("Export to CSV");
	        exportButton.addActionListener(e -> exportDronesToCSV());
	        footerPanel.add(exportButton);

	        JButton backToMenuButton = new JButton("Back to Main Menu");
	        backToMenuButton.addActionListener(e -> {
	            MainMenu mainMenu = new MainMenu();
	            mainMenu.setVisible(true);
	            dispose();
	        });
	        footerPanel.add(backToMenuButton);

	        add(footerPanel, BorderLayout.SOUTH);

	        setLocationRelativeTo(null);

	        // Load drone data automatically on page load without success message
	        fetchAndDisplayDrones();
	    }

	    /**
	     * Fetch drone data from the API and display it in the table.
	     */
	    private void fetchAndDisplayDrones() {
	        try {
	        	
	            List<Drone> drones = fetchDronesFromApi(10, 0);

	            // Clear existing rows in the table before populating new data
	            tableModel.setRowCount(0);

	            // Add fetched drones to the table model
	            for (Drone drone : drones) {
	                String formattedDate = new SimpleDateFormat("MMM. dd, yyyy, h:mm a").format(drone.getCreated());
	                Object[] row = {
	                        drone.getId(),
	                        drone.getDronetype(),
	                        formattedDate,
	                        drone.getSerialNumber(),
	                        drone.getCarriageWeight() + " g",
	                        drone.getCarriageType(),
	                        "20 km/h",  // Hardcoded values
	                        "200 km",   // Hardcoded values
	                        "40 mAh/km", // Hardcoded values
	                        "70%"      // Hardcoded values
	                };
	                tableModel.addRow(row);
	            }
	        } catch (Exception ex) {
	        	
	        	// Show an error message dialog in case of failure
	            JOptionPane.showMessageDialog(this, "Failed to fetch drone data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }

	    /**
	     * Fetch drones from the DroneController API.
	     * @param limit Number of results to fetch.
	     * @param offset Index to start fetching from.
	     * @return List of Drone objects.
	     */
	    private List<Drone> fetchDronesFromApi(int limit, int offset) {
	    	
	    	// Create an empty list to store the retrieved drones
	        List<Drone> drones = new ArrayList<>();
	        
	        // Construct the API URL with query parameters for pagination
	        String apiUrl = "http://localhost:8080/api/drones/?limit=" + limit + "&offset=" + offset;

	        try {
	        	// Initialize a RestTemplate instance to handle HTTP requests
	            RestTemplate restTemplate = new RestTemplate();
	            
	            // Make a GET request to the API and receive a response containing an array of Drone objects
	            ResponseEntity<Drone[]> response = restTemplate.getForEntity(apiUrl, Drone[].class);
	            
	            // Check if the response status is successful (HTTP 2xx codes) and response body is not null
	            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
	            	
	            	// Iterate through the response body (array of Drone objects) and add each to the list
	                for (Drone drone : response.getBody()) {
	                    drones.add(drone);
	                }
	            }
	        } catch (Exception ex) {
	        	// Handle exceptions that may occur during the API call
	            throw new RuntimeException("Error fetching drones from API: " + ex.getMessage(), ex);
	        }
	        
	        // Return the list of fetched drones
	        return drones;
	    }
	    
	    /**
	     * Fetch drones by id from the DroneController API.
	     * @param droneId The unique ID of the drone to fetch details for.
	     * @return Drone object.
	     */
	    private void showDroneDetails(int droneId) {
	        try {
	        	
	        	// Construct the API URL to fetch drone details using the provided drone ID
	            String apiUrl = "http://localhost:8080/api/drones/" + droneId;
	            
	            // Initialize a RestTemplate instance to handle HTTP requests
	            RestTemplate restTemplate = new RestTemplate();
	            
	            // Make a GET request to fetch a single Drone object by its ID
	            ResponseEntity<Drone> response = restTemplate.getForEntity(apiUrl, Drone.class);

	            // Check if the response status is successful (HTTP 2xx codes) and response body is not null
	            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
	            	// Retrieve the drone object from the response
	                Drone drone = response.getBody();
	                
	                // Open a new window to display the drone details
	                new DroneDetailsWindow(drone);
	            } else {
	            	
	            	// Show an error message if the drone was not found (HTTP 404 or empty response)
	                JOptionPane.showMessageDialog(this, "Drone not found.", "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        } catch (Exception ex) {
	        	
	        	// Handle exceptions that may occur during the API call and show an error dialog
	            JOptionPane.showMessageDialog(this, "Error fetching drone details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }
	    
	    
	    /**
	     *  Exports the drone data displayed in the table to a CSV file.
	     */
	    private void exportDronesToCSV() {
	        JFileChooser fileChooser = new JFileChooser();
	        fileChooser.setDialogTitle("Save Drone Data to CSV");
	        int userSelection = fileChooser.showSaveDialog(this);

	        if (userSelection == JFileChooser.APPROVE_OPTION) {
	            String filePath = fileChooser.getSelectedFile().getAbsolutePath() + ".csv";

	            try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
	                // Improved header row with better readability
	                String[] header = {
	                    "Drone ID", "Drone Type", "Date Created", "Serial Number", 
	                    "Carriage Weight (g)", "Carriage Type", 
	                    "Average Speed (km/h)", "Total Distance (km)", 
	                    "Battery Consumption (mAh/km)", "Payload Utilization (%)"
	                };
	                writer.writeNext(header);

	                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm a");

	                // Writing formatted data rows
	                for (int i = 0; i < tableModel.getRowCount(); i++) {
	                    String[] row = new String[tableModel.getColumnCount()];

	                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
	                        Object value = tableModel.getValueAt(i, j);
	                        if (value instanceof Date) {
	                            row[j] = dateFormat.format((Date) value);  // Format date for readability
	                        } else if (value instanceof Number) {
	                            row[j] = String.format("%.2f", ((Number) value).doubleValue());  // Format numbers
	                        } else {
	                            row[j] = value.toString();
	                        }
	                    }

	                    writer.writeNext(row);
	                }

	                JOptionPane.showMessageDialog(this, "Data exported successfully to:\n" + filePath, "Success", JOptionPane.INFORMATION_MESSAGE);
	            } catch (IOException ex) {
	                JOptionPane.showMessageDialog(this, "Error exporting data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        }
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
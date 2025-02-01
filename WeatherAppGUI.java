import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class WeatherAppGUI extends JFrame {
    // GUI components
    private JTextField cityField;
    private JButton fetchButton;
    private JTextArea resultArea;
    
    // API details
    private static final String API_KEY = "50a7aa80fa492fa92e874d23ad061374"; // Replace with your OpenWeatherMap API key
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=";

    // Constructor to set up the GUI
    public WeatherAppGUI() {
        setTitle("Weather App"); // Set the title of the window
        setSize(400, 300); // Set the window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Creating GUI Components
        JLabel cityLabel = new JLabel("Enter City:");
        cityField = new JTextField(15); // Input field for city name
        fetchButton = new JButton("Get Weather"); // Button to fetch weather
        resultArea = new JTextArea(10, 30); // Text area to display results
        resultArea.setEditable(false); // Make the result area non-editable

        // Panel for input
        JPanel inputPanel = new JPanel();
        inputPanel.add(cityLabel);
        inputPanel.add(cityField);
        inputPanel.add(fetchButton);

        // Adding action listener to button
        fetchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fetchWeatherData(); // Call the method to fetch data when button is clicked
            }
        });

        // Layout setup
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER); // Scrollable text area
    }

    // Method to fetch weather data from OpenWeatherMap API
    private void fetchWeatherData() {
        String city = cityField.getText().trim(); // Get user input from text field
        if (city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a city name!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            // Construct the API URL
            String urlString = API_URL + city + "&appid=" + API_KEY + "&units=metric";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read the response from API
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            parseAndDisplayWeather(response.toString()); // Parse and display weather data
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to fetch weather data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to parse JSON response and display weather information
    private void parseAndDisplayWeather(String jsonResponse) {
        JSONObject obj = new JSONObject(jsonResponse);
        String city = obj.getString("name"); // Extract city name
        JSONObject main = obj.getJSONObject("main");
        double temperature = main.getDouble("temp"); // Extract temperature
        int humidity = main.getInt("humidity"); // Extract humidity level
        String weatherDescription = obj.getJSONArray("weather").getJSONObject(0).getString("description"); // Extract weather description
        
        // Display the weather information in the text area
        resultArea.setText("Weather in " + city + "\n" +
                           "Temperature: " + temperature + "°C\n" +
                           "Humidity: " + humidity + "%\n" +
                           "Description: " + weatherDescription);
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WeatherAppGUI().setVisible(true); // Create and display the GUI
        });
    }
}
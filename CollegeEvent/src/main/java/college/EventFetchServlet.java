package college;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/fetchEvents")
public class EventFetchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // JDBC connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "root";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // Set response type to JSON
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        // List to store event details
        List<Event> events = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL JDBC driver
        } catch (ClassNotFoundException e) {
            log("MySQL JDBC Driver not found.", e);
            out.write(gson.toJson(new Response(false, "Internal server error")));
            return;
        }

        // Database connection and event retrieval
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT name, description, date, location FROM events")) {

            // Execute query and get the result
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    String date = resultSet.getString("date");
                    String location = resultSet.getString("location");

                    events.add(new Event(name, description, date, location));
                }
            }

            // Write the events as JSON response
            out.write(gson.toJson(events));

        } catch (SQLException e) {
            // Handle SQL errors
            e.printStackTrace();
            out.write(gson.toJson(new Response(false, "Database error")));
        }
    }

    // Event class for structured event data
    private static class Event {
        private final String name;
        private final String description;
        private final String date;
        private final String location;

        public Event(String name, String description, String date, String location) {
            this.name = name;
            this.description = description;
            this.date = date;
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getDate() {
            return date;
        }

        public String getLocation() {
            return location;
        }
    }

    // Response class for structured JSON output
    private static class Response {
        private final boolean success;
        private final String message;

        public Response(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}

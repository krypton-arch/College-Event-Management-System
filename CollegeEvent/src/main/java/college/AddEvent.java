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
import java.sql.SQLException;

@WebServlet("/addEvent")
public class AddEvent extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // JDBC connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "root";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // Set response type to JSON
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        // Parse JSON input from the request
        Event event;
        try {
            event = gson.fromJson(request.getReader(), Event.class);
        } catch (Exception e) {
            out.write(gson.toJson(new Response(false, "Invalid input data")));
            return;
        }

        // Validate input
        if (event.getName() == null || event.getName().trim().isEmpty() ||
            event.getDescription() == null || event.getDescription().trim().isEmpty() ||
            event.getDate() == null || event.getDate().trim().isEmpty() ||
            event.getLocation() == null || event.getLocation().trim().isEmpty()) {
            out.write(gson.toJson(new Response(false, "All fields are required")));
            return;
        }

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log("MySQL JDBC Driver not found", e);
            out.write(gson.toJson(new Response(false, "Internal server error")));
            return;
        }

        // Insert event into the database
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO events (name, description, date, location) VALUES (?, ?, ?, ?)")) {

            // Set parameters for the SQL statement
            statement.setString(1, event.getName());
            statement.setString(2, event.getDescription());
            statement.setString(3, event.getDate());
            statement.setString(4, event.getLocation());

            // Execute the update
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                out.write(gson.toJson(new Response(true, "Event added successfully")));
            } else {
                out.write(gson.toJson(new Response(false, "Failed to add the event")));
            }

        } catch (SQLException e) {
            log("Database error", e);
            out.write(gson.toJson(new Response(false, "Database error")));
        }
    }

    // Event class to map the input JSON
    private static class Event {
        private String name;
        private String description;
        private String date;
        private String location;

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

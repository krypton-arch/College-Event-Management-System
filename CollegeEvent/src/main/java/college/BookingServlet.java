package college;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/bookEvent")
public class BookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // JDBC connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "root";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        String eventName = request.getParameter("eventName");

        if (eventName == null || eventName.trim().isEmpty()) {
            out.write(gson.toJson(new Response(false, "Event name cannot be empty")));
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            out.write(gson.toJson(new Response(false, "Session does not exist")));
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            out.write(gson.toJson(new Response(false, "User ID not found in session")));
            return;
        }

        String userEmail = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log("MySQL JDBC Driver not found", e);
            out.write(gson.toJson(new Response(false, "Internal server error")));
            return;
        }

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS)) {

            // Verify that the event exists in the events table
            try (PreparedStatement eventCheck = connection.prepareStatement(
                    "SELECT COUNT(*) FROM events WHERE name = ?")) {
                eventCheck.setString(1, eventName);
                try (ResultSet resultSet = eventCheck.executeQuery()) {
                    if (!resultSet.next() || resultSet.getInt(1) == 0) {
                        out.write(gson.toJson(new Response(false, "Event does not exist")));
                        return;
                    }
                }
            }

            // Retrieve the user's email
            try (PreparedStatement emailStatement = connection.prepareStatement(
                    "SELECT email FROM users WHERE id = ?")) {
                emailStatement.setInt(1, userId);
                try (ResultSet resultSet = emailStatement.executeQuery()) {
                    if (resultSet.next()) {
                        userEmail = resultSet.getString("email");
                    } else {
                        out.write(gson.toJson(new Response(false, "User email not found")));
                        return;
                    }
                }
            }

            // Insert the booking
            try (PreparedStatement insertBooking = connection.prepareStatement(
                    "INSERT INTO bookings (event_name, user_email, booking_date) VALUES (?, ?, ?)")) {
                insertBooking.setString(1, eventName);
                insertBooking.setString(2, userEmail);
                insertBooking.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

                int rowsAffected = insertBooking.executeUpdate();
                if (rowsAffected > 0) {
                    out.write(gson.toJson(new Response(true, "Booking successful")));
                } else {
                    out.write(gson.toJson(new Response(false, "Failed to book the event")));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            out.write(gson.toJson(new Response(false, "Database error")));
        }
    }

    private static class Response {
        private final boolean success;
        private final String message;

        public Response(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}

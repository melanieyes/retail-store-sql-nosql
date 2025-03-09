package database;

import model.User;

import java.awt.*;
import java.sql.*;

public class DataAdapter {
    private static final String URL = "jdbc:mysql://localhost:3306/store";
    private static final String USER = "root";
    private static final String PASSWORD = "Wabisabi^^";

    public static final Color PRIMARY_COLOR = Color.decode("#191E29");
    public static final Color SECONDARY_COLOR = Color.decode("#132D46");
    public static final Color TEXT_COLOR = Color.decode("#01C38D");

    private static Connection connection = null;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    public static User validateLogin(String username, String password) throws SQLException {
        String query = "SELECT * FROM Users WHERE Username = ? AND Password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("UserId"));
                    user.setUsername(rs.getString("Username"));
                    user.setPassword(rs.getString("Password"));
                    user.setRole(rs.getString("Role"));
                    return user;
                }
            }
        }
        return null;
    }
}

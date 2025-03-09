package control;

import database.DataAdapter;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO Users (Username, Password, Role) VALUES (?, ?, ?)";
        try (Connection conn = DataAdapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.executeUpdate();
        }
    }

    public void deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM Users WHERE UserId = ?";
        try (Connection conn = DataAdapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM Users";
        try (Connection conn = DataAdapter.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("UserId"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setRole(rs.getString("Role"));
                users.add(user);
            }
        }
        return users;
    }
}

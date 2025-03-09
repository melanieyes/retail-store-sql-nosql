package view;

import control.UserController;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ManageUsersView extends JPanel {
    private final DefaultTableModel tableModel;
    private final UserController userController;

    public ManageUsersView() {
        userController = new UserController();
        setLayout(new BorderLayout());
        setBackground(new Color(0, 139, 139)); // Dark cyan

        // Title label
        JLabel titleLabel = new JLabel("User Management", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"User ID", "Username", "Role"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable userTable = new JTable(tableModel);
        userTable.setBackground(Color.DARK_GRAY);
        userTable.setForeground(Color.WHITE);
        userTable.getTableHeader().setBackground(new Color(0, 104, 104));
        userTable.getTableHeader().setForeground(Color.WHITE);
        add(new JScrollPane(userTable), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 139, 139));
        buttonPanel.setFont(new Font("Dialog", Font.BOLD, 14));
        JButton addButton = createStyledButton("Add User");
        JButton deleteButton = createStyledButton("Delete User");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load user data
        loadUsers();

        // Add User functionality
        addButton.addActionListener(e -> addUser());

        // Delete User functionality
        deleteButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow >= 0) {
                int userId = (int) tableModel.getValueAt(selectedRow, 0);
                deleteUser(userId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a user to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(244, 187, 68)); // Amber
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Dialog", Font.BOLD, 14));
        button.setFocusPainted(false);
        return button;
    }

    private void loadUsers() {
        try {
            tableModel.setRowCount(0); // Clear table
            List<User> users = userController.getAllUsers();
            for (User user : users) {
                tableModel.addRow(new Object[]{user.getUserId(), user.getUsername(), user.getRole()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addUser() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField roleField = new JTextField();

        Object[] fields = {
                "Username:", usernameField,
                "Password:", passwordField,
                "Role:", roleField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                User user = new User();
                user.setUsername(usernameField.getText());
                user.setPassword(new String(passwordField.getPassword()));
                user.setRole(roleField.getText());
                userController.addUser(user);
                loadUsers(); // Refresh table
                JOptionPane.showMessageDialog(this, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteUser(int userId) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                userController.deleteUser(userId);
                loadUsers(); // Refresh table
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

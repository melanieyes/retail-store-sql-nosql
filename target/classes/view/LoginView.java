package view;

import database.DataAdapter;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginView extends JFrame {

    public LoginView() {
        setTitle("Login");
        setSize(520, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(DataAdapter.PRIMARY_COLOR); // Apply background color
        addGuiComponents();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private void addGuiComponents() {
        // Title label
        JLabel loginLabel = new JLabel("Login");
        loginLabel.setBounds(0, 25, 520, 100);
        loginLabel.setForeground(DataAdapter.TEXT_COLOR);
        loginLabel.setFont(new Font("Dialog", Font.BOLD, 40));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(loginLabel);

        // Username field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(30, 150, 400, 25);
        usernameLabel.setForeground(DataAdapter.TEXT_COLOR);
        usernameLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
        JTextField usernameField = new JTextField();
        usernameField.setBounds(30, 185, 450, 55);
        usernameField.setBackground(DataAdapter.SECONDARY_COLOR);
        usernameField.setForeground(DataAdapter.TEXT_COLOR);
        usernameField.setFont(new Font("Dialog", Font.PLAIN, 24));
        usernameField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(usernameLabel);
        add(usernameField);

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 335, 400, 25);
        passwordLabel.setForeground(DataAdapter.TEXT_COLOR);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(30, 365, 450, 55);
        passwordField.setBackground(DataAdapter.SECONDARY_COLOR);
        passwordField.setForeground(DataAdapter.TEXT_COLOR);
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 24));
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(passwordLabel);
        add(passwordField);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(125, 520, 250, 50);
        loginButton.setFont(new Font("Dialog", Font.BOLD, 18));
        loginButton.setBackground(DataAdapter.TEXT_COLOR);
        loginButton.setForeground(Color.BLACK);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                // Validate credentials and get the user
                User user = DataAdapter.validateLogin(username, password);
                if (user != null) {
                    JOptionPane.showMessageDialog(this, "Login Successful!");

                    SwingUtilities.invokeLater(() -> {
                        new MainApplication(user.getRole());
                        this.dispose(); // Close the login screen
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error during login: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        add(loginButton);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginView::new);
    }
}

package view;

import control.RecentCustomerController;
import model.RecentCustomer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RecentCustomerView extends JPanel {
    private final DefaultTableModel tableModel;
    private final RecentCustomerController customerController;
    private JTable customerTable;

    public RecentCustomerView() {
        customerController = new RecentCustomerController();

        setLayout(new BorderLayout());
        setBackground(new Color(0, 139, 139));

        // Title label
        JLabel titleLabel = new JLabel("Recent Customers", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"First Name", "Last Name", "Email", "Phone", "Address", "Join Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        customerTable = new JTable(tableModel);
        customerTable.setBackground(Color.DARK_GRAY);
        customerTable.setForeground(Color.WHITE);
        customerTable.setFont(new Font("Monospaced", Font.PLAIN, 14));
        customerTable.getTableHeader().setBackground(new Color(0, 104, 104));
        customerTable.getTableHeader().setForeground(Color.WHITE);
        add(new JScrollPane(customerTable), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 139, 139));
        JButton addButton = createStyledButton("Add Customer");
        JButton deleteButton = createStyledButton("Delete Customer");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data
        loadCustomers();

        // Add button functionality
        addButton.addActionListener(e -> addCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(244, 187, 68));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Dialog", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        button.setOpaque(true);
        return button;
    }

    private void loadCustomers() {
        tableModel.setRowCount(0); // Clear table
        List<RecentCustomer> customers = customerController.getAllCustomers();

        for (RecentCustomer customer : customers) {
            Object[] rowData = {
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getAddress(),
                    customer.getJoinDate()
            };
            tableModel.addRow(rowData);
        }
    }

    private void addCustomer() {
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField joinDateField = new JTextField();

        Object[] fields = {
                "First Name:", firstNameField,
                "Last Name:", lastNameField,
                "Email:", emailField,
                "Phone:", phoneField,
                "Address:", addressField,
                "Join Date:", joinDateField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add Customer", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                RecentCustomer customer = new RecentCustomer(
                        firstNameField.getText(),
                        lastNameField.getText(),
                        emailField.getText(),
                        phoneField.getText(),
                        addressField.getText(),
                        joinDateField.getText()
                );
                customerController.addCustomer(customer);
                loadCustomers();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error adding customer: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow >= 0) {
            String firstName = (String) tableModel.getValueAt(selectedRow, 0);
            String lastName = (String) tableModel.getValueAt(selectedRow, 1);
            String key = "recent_customer:" + firstName.toLowerCase() + "_" + lastName.toLowerCase();

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this customer?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                customerController.deleteCustomer(key);
                loadCustomers();
                JOptionPane.showMessageDialog(this, "Customer deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}

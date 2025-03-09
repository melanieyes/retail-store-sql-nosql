package view;

import control.CustomerController;
import model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomerView extends JPanel {
    private final DefaultTableModel tableModel;
    private final CustomerController customerController;
    private JTable customerTable;

    public CustomerView() {
        customerController = new CustomerController();

        // Panel layout and background color
        setLayout(new BorderLayout());
        setBackground(new Color(0, 139, 139));

        // Title label
        JLabel titleLabel = new JLabel("Customer Management", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Customer ID", "First Name", "Last Name", "Email", "Phone", "Address", "Join Date"};
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

        // Add customer functionality
        addButton.addActionListener(e -> addCustomer());

        // Delete customer functionality
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
        List<Customer> customers = customerController.getAllCustomers();

        for (Customer customer : customers) {
            try {
                Object[] rowData = {
                        customer.getId().toString(),
                        customer.getfName(),
                        customer.getlName(),
                        customer.getEmail(),
                        customer.getPhone(),
                        customer.getAddress(),
                        new SimpleDateFormat("yyyy-MM-dd").format(customer.getJoinDate())
                };
                tableModel.addRow(rowData);
            } catch (Exception e) {
                System.err.println("Error loading customer: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void addCustomer() {
        JTextField fNameField = new JTextField();
        JTextField lNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField joinDateField = new JTextField();

        Object[] fields = {
                "First Name:", fNameField,
                "Last Name:", lNameField,
                "Email:", emailField,
                "Phone:", phoneField,
                "Address:", addressField,
                "Join Date (YYYY-MM-DD):", joinDateField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add Customer", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Customer customer = new Customer();
                customer.setfName(fNameField.getText());
                customer.setlName(lNameField.getText());
                customer.setEmail(emailField.getText());
                customer.setPhone(phoneField.getText());
                customer.setAddress(addressField.getText());

                // Convert String to Date
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date parsedDate = formatter.parse(joinDateField.getText());
                customer.setJoinDate(parsedDate);

                customerController.addCustomer(customer);
                loadCustomers(); // Refresh table
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error adding customer: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow >= 0) {
            String customerId = (String) tableModel.getValueAt(selectedRow, 0); // Customer ID

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this customer?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    customerController.deleteCustomer(new org.bson.types.ObjectId(customerId));
                    loadCustomers(); // Refresh table
                    JOptionPane.showMessageDialog(this, "Customer deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error deleting customer: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}

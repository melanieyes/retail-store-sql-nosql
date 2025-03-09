package view;

import model.CartItem;
import model.Products;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class ShoppingCartView extends JPanel {
    private final DefaultTableModel cartTableModel;
    private final JTable cartTable;
    private final JButton addToCartButton;
    private final JButton confirmOrderButton;
    private final JButton deleteItemButton; // New button to delete items

    public ShoppingCartView() {
        setLayout(new BorderLayout());
        setBackground(new Color(0, 139, 139));

        // Title label
        JLabel titleLabel = new JLabel("Shopping Cart", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Cart Table
        String[] cartColumns = {"Product ID", "Product Name", "Price", "Quantity", "Total"};
        cartTableModel = new DefaultTableModel(cartColumns, 0);
        cartTable = new JTable(cartTableModel);
        styleTable(cartTable);

        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartScrollPane.setBorder(BorderFactory.createTitledBorder("Your Cart"));
        add(cartScrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 139, 139));

        addToCartButton = createButton("Add to Cart");
        confirmOrderButton = createButton("Confirm Order");
        deleteItemButton = createButton("Delete from Cart"); // New button for deleting items

        buttonPanel.add(addToCartButton);
        buttonPanel.add(confirmOrderButton);
        buttonPanel.add(deleteItemButton); // Add the delete button to the panel
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void styleTable(JTable table) {
        table.setBackground(Color.DARK_GRAY);
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Monospaced", Font.PLAIN, 14));
        table.getTableHeader().setBackground(new Color(0, 104, 104));
        table.getTableHeader().setForeground(Color.WHITE);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(244, 187, 68));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Dialog", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        return button;
    }

    // Method to update the product table with the given products
    public void updateProductTable(List<Products> products) {
        // Clear the existing rows in the table
        cartTableModel.setRowCount(0);

        // Add each product to the table
        for (Products product : products) {
            Object[] rowData = {
                    product.getProdsID(),          // Product ID
                    product.getProdsName(),        // Product Name
                    product.getPrice()             // Product Price
            };
            cartTableModel.addRow(rowData); // Add product data as a new row in the table
        }
    }

    public void updateCartTable(List<CartItem> cartItems) {
        cartTableModel.setRowCount(0); // Clear existing data
        for (CartItem item : cartItems) {
            cartTableModel.addRow(new Object[]{
                    item.getProdsID(),
                    item.getName(),
                    item.getPrice(),
                    item.getQuantity(),
                    item.getTotalCost()
            });
        }
    }

    public void clearCartTable() {
        cartTableModel.setRowCount(0);
    }

    public int getSelectedProductRow() {
        return cartTable.getSelectedRow();
    }

    public int getSelectedProductID(int row) {
        return (int) cartTableModel.getValueAt(row, 0);
    }

    public String getSelectedProductName(int row) {
        return (String) cartTableModel.getValueAt(row, 1);
    }

    public double getSelectedProductPrice(int row) {
        return (double) cartTableModel.getValueAt(row, 2);
    }

    public void addAddToCartListener(ActionListener listener) {
        addToCartButton.addActionListener(listener);
    }

    public void addConfirmOrderListener(ActionListener listener) {
        confirmOrderButton.addActionListener(listener);
    }

    public void addDeleteItemListener(ActionListener listener) {
        deleteItemButton.addActionListener(listener);
    }

    // Method to show the dialog for entering shipping and email information
    public Object[] showOrderConfirmationDialog() {
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField phoneField = new JTextField();

        Object[] fields = {
                "First Name:", firstNameField,
                "Last Name:", lastNameField,
                "Email:", emailField,
                "Address:", addressField,
                "Phone:", phoneField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Enter Shipping Information", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            // Get the values entered by the user
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();  // Email value
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();

            // Return all the entered values
            return new Object[]{firstName, lastName, email, address, phone};
        } else {
            return null; // If user cancels, return null
        }
    }
}

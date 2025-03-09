package view;

import control.ProductController;
import model.Products;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductView extends JPanel {
    private final DefaultTableModel tableModel;
    private final ProductController productController;
    private JTable productTable;

    // Define a consistent color theme
    private static final Color PRIMARY_COLOR = new Color(0, 139, 139); // Dark cyan
    private static final Color BUTTON_COLOR = new Color(244, 187, 68); // Yellow
    private static final Color TEXT_COLOR = Color.WHITE;

    public ProductView() {
        productController = new ProductController();
        setLayout(new BorderLayout());
        setBackground(PRIMARY_COLOR);

        // Title label
        JLabel titleLabel = new JLabel("Product Management", JLabel.CENTER);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Table for displaying product data
        String[] columnNames = {"Product ID", "Product Name", "Category ID", "Price", "Stock Quantity", "Supplier ID"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        productTable.setBackground(Color.DARK_GRAY);
        productTable.setForeground(TEXT_COLOR);
        productTable.setFont(new Font("Monospaced", Font.PLAIN, 14));
        productTable.getTableHeader().setBackground(PRIMARY_COLOR);
        productTable.getTableHeader().setForeground(TEXT_COLOR);
        add(new JScrollPane(productTable), BorderLayout.CENTER);

        // Buttons for actions
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(PRIMARY_COLOR);
        JButton addButton = createStyledButton("Add Product");
        JButton deleteButton = createStyledButton("Delete Product");
        JButton updateButton = createStyledButton("Update Product");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        add(buttonPanel, BorderLayout.SOUTH);


        loadProducts();

        // Add product functionality
        addButton.addActionListener(e -> addProduct());

        // Delete product functionality
        deleteButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow >= 0) {
                int productId = (int) tableModel.getValueAt(selectedRow, 0);
                deleteProduct(productId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a product to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Update product functionality
        updateButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow >= 0) {
                int productId = (int) tableModel.getValueAt(selectedRow, 0); // Get Product ID
                updateProduct(productId, selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a product to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFont(new Font("Dialog", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        button.setOpaque(true);
        return button;
    }

    private void loadProducts() {
        try {
            tableModel.setRowCount(0); // Clear existing rows
            List<Products> products = productController.getAllProducts();

            for (Products product : products) {
                Object[] rowData = {
                        product.getProdsID(),
                        product.getProdsName(),
                        product.getProdCatID(),
                        product.getPrice(),
                        product.getQtyStock(),
                        product.getSuppID()
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addProduct() {
        JTextField nameField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField stockField = new JTextField();
        JTextField supplierField = new JTextField();

        Object[] fields = {
                "Product Name:", nameField,
                "Category ID:", categoryField,
                "Price:", priceField,
                "Stock Quantity:", stockField,
                "Supplier ID:", supplierField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add Product", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Products product = new Products();
                product.setProdsName(nameField.getText());
                product.setProdCatID(Integer.parseInt(categoryField.getText()));
                product.setPrice(Double.parseDouble(priceField.getText()));
                product.setQtyStock(Integer.parseInt(stockField.getText()));
                product.setSuppID(Integer.parseInt(supplierField.getText()));
                productController.addProduct(product);
                loadProducts(); // Refresh table
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding product: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteProduct(int productId) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                productController.deleteProduct(productId);
                loadProducts(); // Refresh table
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting product: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateProduct(int productId, int selectedRow) {
        // Pre-fill the fields with the current values
        String currentName = (String) tableModel.getValueAt(selectedRow, 1);
        String currentCategory = tableModel.getValueAt(selectedRow, 2).toString();
        String currentPrice = tableModel.getValueAt(selectedRow, 3).toString();
        String currentStock = tableModel.getValueAt(selectedRow, 4).toString();
        String currentSupplier = tableModel.getValueAt(selectedRow, 5).toString();

        JTextField nameField = new JTextField(currentName);
        JTextField categoryField = new JTextField(currentCategory);
        JTextField priceField = new JTextField(currentPrice);
        JTextField stockField = new JTextField(currentStock);
        JTextField supplierField = new JTextField(currentSupplier);

        Object[] fields = {
                "Product Name:", nameField,
                "Category ID:", categoryField,
                "Price:", priceField,
                "Stock Quantity:", stockField,
                "Supplier ID:", supplierField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Update Product", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Products product = new Products();
                product.setProdsID(productId);
                product.setProdsName(nameField.getText());
                product.setProdCatID(Integer.parseInt(categoryField.getText()));
                product.setPrice(Double.parseDouble(priceField.getText()));
                product.setQtyStock(Integer.parseInt(stockField.getText()));
                product.setSuppID(Integer.parseInt(supplierField.getText()));

                productController.updateProduct(product);
                loadProducts();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error updating product: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

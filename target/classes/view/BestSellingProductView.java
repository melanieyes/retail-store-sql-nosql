package view;

import control.BestSellingProductController;
import model.BestSellingProduct;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BestSellingProductView extends JPanel {
    private final DefaultTableModel tableModel;
    private final BestSellingProductController productController;
    private JTable productTable;

    public BestSellingProductView() {
        productController = new BestSellingProductController();

        setLayout(new BorderLayout());
        setBackground(new Color(0, 139, 139));

        // Title label
        JLabel titleLabel = new JLabel("Best-Selling Products", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Name", "Category", "Price", "Sales", "Revenue"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        productTable.setBackground(Color.DARK_GRAY);
        productTable.setForeground(Color.WHITE);
        productTable.setFont(new Font("Monospaced", Font.PLAIN, 14));
        productTable.getTableHeader().setBackground(new Color(0, 104, 104));
        productTable.getTableHeader().setForeground(Color.WHITE);
        add(new JScrollPane(productTable), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 139, 139));
        JButton addButton = createStyledButton("Add Product");
        JButton deleteButton = createStyledButton("Delete Product");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data
        loadProducts();

        // Add button functionality
        addButton.addActionListener(e -> addProduct());
        deleteButton.addActionListener(e -> deleteProduct());
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

    private void loadProducts() {
        tableModel.setRowCount(0); // Clear table
        List<BestSellingProduct> products = productController.getAllProducts();

        for (BestSellingProduct product : products) {
            Object[] rowData = {
                    product.getName(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getSales(),
                    product.getRevenue()
            };
            tableModel.addRow(rowData);
        }
    }

    private void addProduct() {
        JTextField nameField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField salesField = new JTextField();
        JTextField revenueField = new JTextField();

        Object[] fields = {
                "Name:", nameField,
                "Category:", categoryField,
                "Price:", priceField,
                "Sales:", salesField,
                "Revenue:", revenueField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add Product", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                BestSellingProduct product = new BestSellingProduct(
                        nameField.getText(),
                        categoryField.getText(),
                        Double.parseDouble(priceField.getText()),
                        Integer.parseInt(salesField.getText()),
                        Double.parseDouble(revenueField.getText())
                );
                productController.addProduct(product);
                loadProducts();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error adding product: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            String productName = (String) tableModel.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this product?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                productController.deleteProduct(productName);
                loadProducts();
                JOptionPane.showMessageDialog(this, "Product deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}

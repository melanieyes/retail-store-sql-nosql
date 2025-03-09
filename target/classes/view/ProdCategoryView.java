package view;

import control.ProdCategoryController;
import model.ProdCategory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ProdCategoryView extends JPanel {
    private final DefaultTableModel tableModel;
    private final ProdCategoryController categoryController;
    private JTable categoryTable;

    public ProdCategoryView() {
        categoryController = new ProdCategoryController();

        setLayout(new BorderLayout());
        setBackground(new Color(0, 139, 139));

        // Title label
        JLabel titleLabel = new JLabel("Product Categories Management", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Category ID", "Category Name"};
        tableModel = new DefaultTableModel(columnNames, 0);
        categoryTable = new JTable(tableModel);
        categoryTable.setBackground(Color.DARK_GRAY);
        categoryTable.setForeground(Color.WHITE);
        categoryTable.setFont(new Font("Monospaced", Font.PLAIN, 14));
        categoryTable.getTableHeader().setBackground(new Color(0, 104, 104));
        categoryTable.getTableHeader().setForeground(Color.WHITE);
        add(new JScrollPane(categoryTable), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 139, 139));
        JButton addButton = createStyledButton("Add Category");
        JButton deleteButton = createStyledButton("Delete Category");
        JButton updateButton = createStyledButton("Update Category");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data
        loadCategories();

        // Add category functionality
        addButton.addActionListener(e -> addCategory());

        // Delete category functionality
        deleteButton.addActionListener(e -> {
            int selectedRow = categoryTable.getSelectedRow();
            if (selectedRow >= 0) {
                int categoryId = (int) tableModel.getValueAt(selectedRow, 0); // Get Category ID
                deleteCategory(categoryId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a category to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Update category functionality
        updateButton.addActionListener(e -> {
            int selectedRow = categoryTable.getSelectedRow();
            if (selectedRow >= 0) {
                int categoryId = (int) tableModel.getValueAt(selectedRow, 0);
                updateCategory(categoryId, selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a category to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Dialog", Font.BOLD, 14));
        button.setBackground(new Color(244, 187, 68));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Dialog", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        button.setOpaque(true);
        return button;
    }

    private void loadCategories() {
        try {
            tableModel.setRowCount(0); // Clear table
            List<ProdCategory> categories = categoryController.getAllCategories();
            for (ProdCategory category : categories) {
                Object[] rowData = {
                        category.getProdCatID(),
                        category.getCatName()
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addCategory() {
        JTextField catNameField = new JTextField();

        Object[] fields = {
                "Category Name:", catNameField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add Category", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                ProdCategory category = new ProdCategory();
                category.setCatName(catNameField.getText());
                categoryController.addCategory(category);
                loadCategories(); // Refresh table
                JOptionPane.showMessageDialog(this, "Category added successfully.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding category: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteCategory(int categoryId) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this category?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                categoryController.deleteCategory(categoryId);
                loadCategories(); // Refresh table
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting category: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateCategory(int categoryId, int selectedRow) {
        String currentName = (String) tableModel.getValueAt(selectedRow, 1);

        JTextField catNameField = new JTextField(currentName);

        Object[] fields = {
                "Category Name:", catNameField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Update Category", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                ProdCategory category = new ProdCategory();
                category.setProdCatID(categoryId);
                category.setCatName(catNameField.getText());
                categoryController.updateCategory(category);
                loadCategories();
                JOptionPane.showMessageDialog(this, "Category updated successfully.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error updating category: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

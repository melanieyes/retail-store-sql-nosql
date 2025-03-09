package view;

import control.ReviewController;
import model.Review;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReviewView extends JPanel {
    private final DefaultTableModel tableModel;
    private final ReviewController reviewController;
    private JTable reviewTable;

    public ReviewView() {
        reviewController = new ReviewController();

        // Panel layout and background color
        setLayout(new BorderLayout());
        setBackground(new Color(0, 139, 139));

        // Title label
        JLabel titleLabel = new JLabel("Review Management", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Customer ID", "Product ID", "Rating", "Review Text", "Review Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        reviewTable = new JTable(tableModel);
        reviewTable.setBackground(Color.DARK_GRAY);
        reviewTable.setForeground(Color.WHITE);
        reviewTable.setFont(new Font("Monospaced", Font.PLAIN, 14));
        reviewTable.getTableHeader().setBackground(new Color(0, 104, 104));
        reviewTable.getTableHeader().setForeground(Color.WHITE);
        add(new JScrollPane(reviewTable), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 139, 139));
        JButton addButton = createStyledButton("Add Review");
        JButton deleteButton = createStyledButton("Delete Review");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data
        loadReviews();

        // Add review functionality
        addButton.addActionListener(e -> addReview());

        // Delete review functionality
        deleteButton.addActionListener(e -> deleteReview());
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

    private void loadReviews() {
        tableModel.setRowCount(0); // Clear table
        List<Review> reviews = reviewController.getAllReviews();

        for (Review review : reviews) {
            try {
                Object[] rowData = {
                        (review.getCustId() != null) ? review.getCustId().toString() : "N/A",
                        (review.getProdsId() != null) ? review.getProdsId().toString() : "N/A",
                        review.getRating(),
                        (review.getReviewText() != null) ? review.getReviewText() : "N/A",
                        (review.getReviewDate() != null) ? new SimpleDateFormat("yyyy-MM-dd").format(review.getReviewDate()) : "N/A"
                };
                tableModel.addRow(rowData);
            } catch (Exception e) {
                System.err.println("Error loading review: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void addReview() {
        JTextField custIDField = new JTextField();
        JTextField prodsIDField = new JTextField();
        JTextField ratingField = new JTextField();
        JTextField reviewTextField = new JTextField();
        JTextField reviewDateField = new JTextField();

        Object[] fields = {
                "Customer ID:", custIDField,
                "Product ID:", prodsIDField,
                "Rating (1-5):", ratingField,
                "Review Text:", reviewTextField,
                "Review Date (YYYY-MM-DD):", reviewDateField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add Review", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Review review = new Review();
                review.setCustId(new org.bson.types.ObjectId(custIDField.getText()));
                review.setProdsId(new org.bson.types.ObjectId(prodsIDField.getText()));
                review.setRating(Integer.parseInt(ratingField.getText()));
                review.setReviewText(reviewTextField.getText());

                // Convert String to Date
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date parsedDate = formatter.parse(reviewDateField.getText());
                review.setReviewDate(parsedDate);

                reviewController.addReview(review);
                loadReviews(); // Refresh table
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error adding review: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteReview() {
        int selectedRow = reviewTable.getSelectedRow();
        if (selectedRow >= 0) {
            String custId = (String) tableModel.getValueAt(selectedRow, 0); // Customer ID
            String prodsId = (String) tableModel.getValueAt(selectedRow, 1); // Product ID

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this review?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    reviewController.deleteReviewByCustomerAndProductId(custId, prodsId); // Custom deletion logic
                    loadReviews(); // Refresh table
                    JOptionPane.showMessageDialog(this, "Review deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error deleting review: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a review to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}

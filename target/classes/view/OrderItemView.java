package view;

import control.OrderItemController;
import model.OrderItem;
import org.bson.types.ObjectId;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrderItemView extends JPanel {
    private final DefaultTableModel tableModel;
    private final OrderItemController orderItemController;
    private JTable orderItemTable;
    private ObjectId orderId;

    // Constructor that accepts an ObjectId
    public OrderItemView(ObjectId orderId) {
        this.orderId = orderId;

        orderItemController = new OrderItemController();

        setLayout(new BorderLayout());
        setBackground(new Color(0, 139, 139));

        // Title label
        JLabel titleLabel = new JLabel("Order Item Management", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Item ID", "Order ID", "Product Name", "Quantity", "Unit Price", "Total Price"};
        tableModel = new DefaultTableModel(columnNames, 0);
        orderItemTable = new JTable(tableModel);
        orderItemTable.setBackground(Color.DARK_GRAY);
        orderItemTable.setForeground(Color.WHITE);
        orderItemTable.setFont(new Font("Monospaced", Font.PLAIN, 14));
        orderItemTable.getTableHeader().setBackground(new Color(0, 104, 104));
        orderItemTable.getTableHeader().setForeground(Color.WHITE);
        add(new JScrollPane(orderItemTable), BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 139, 139));
        JButton addButton = createStyledButton("Add Item");
        JButton deleteButton = createStyledButton("Delete Item");
        JButton purchaseButton = createStyledButton("Purchase");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(purchaseButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data
        loadOrderItems();

        // Add button functionality
        addButton.addActionListener(e -> addOrderItem());
        deleteButton.addActionListener(e -> deleteOrderItem());
        purchaseButton.addActionListener(e -> purchaseOrderItems());
    }

    // Default constructor (for standalone use)
    public OrderItemView() {
        this(null);  // Calling the constructor with no ObjectId (i.e., no specific order)
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

    private void loadOrderItems() {
        tableModel.setRowCount(0); // Clear table
        List<OrderItem> items;

        if (orderId != null) {
            items = orderItemController.getOrderItemsByOrderId(orderId);  // Fetch order items by order ID
        } else {
            items = orderItemController.getAllOrderItems();  // Fetch all order items (standalone view)
        }

        for (OrderItem item : items) {
            Object[] rowData = {
                    item.getId().toString(),
                    item.getOrderId().toString(),
                    item.getProdsName(),
                    item.getQty(),
                    item.getUnitPrice(),
                    item.getTotalPrice()
            };
            tableModel.addRow(rowData);
        }
    }

    private void addOrderItem() {
        JTextField orderIdField = new JTextField();
        JTextField productNameField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField unitPriceField = new JTextField();

        Object[] fields = {
                "Order ID:", orderIdField,
                "Product Name:", productNameField,
                "Quantity:", quantityField,
                "Unit Price:", unitPriceField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add Order Item", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                OrderItem item = new OrderItem();
                item.setOrderId(new org.bson.types.ObjectId(orderIdField.getText()));
                item.setProdsName(productNameField.getText());
                item.setQty(Integer.parseInt(quantityField.getText()));
                item.setUnitPrice(Double.parseDouble(unitPriceField.getText()));
                item.setTotalPrice(item.getQty() * item.getUnitPrice());

                orderItemController.addOrderItem(item);
                loadOrderItems();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error adding item: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteOrderItem() {
        int selectedRow = orderItemTable.getSelectedRow();
        if (selectedRow >= 0) {
            String itemId = (String) tableModel.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this item?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                orderItemController.deleteOrderItemById(new org.bson.types.ObjectId(itemId));
                loadOrderItems();
                JOptionPane.showMessageDialog(this, "Item deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void purchaseOrderItems() {
        int selectedRow = orderItemTable.getSelectedRow();
        if (selectedRow >= 0) {
            String itemId = (String) tableModel.getValueAt(selectedRow, 0);
            String orderId = (String) tableModel.getValueAt(selectedRow, 1);
            String[] paymentMethods = {"Apple Pay", "QR", "Cash"};

            String paymentMethod = (String) JOptionPane.showInputDialog(
                    this,
                    "Select a payment method:",
                    "Purchase Order",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    paymentMethods,
                    paymentMethods[0]
            );

            if (paymentMethod != null) {
                orderItemController.deleteOrderItemById(new org.bson.types.ObjectId(itemId));
                loadOrderItems();
                JOptionPane.showMessageDialog(this, "Order " + orderId + " purchased using " + paymentMethod + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to purchase.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}

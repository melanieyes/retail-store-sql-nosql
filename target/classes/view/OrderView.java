package view;

import control.OrderController;
import model.Order;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Consumer;

public class OrderView extends JPanel {
    private final DefaultTableModel tableModel;
    private final OrderController orderController;
    private JTable orderTable;
    private Consumer<String> onOrderSelected;

    public OrderView() {
        orderController = new OrderController();

        setLayout(new BorderLayout());
        setBackground(new Color(0, 139, 139));

        // Title label
        JLabel titleLabel = new JLabel("Order Management", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Order ID", "Customer ID", "Order Date", "Status", "Total Amount"};
        tableModel = new DefaultTableModel(columnNames, 0);
        orderTable = new JTable(tableModel);
        orderTable.setBackground(Color.DARK_GRAY);
        orderTable.setForeground(Color.WHITE);
        orderTable.setFont(new Font("Monospaced", Font.PLAIN, 14));
        orderTable.getTableHeader().setBackground(new Color(0, 104, 104));
        orderTable.getTableHeader().setForeground(Color.WHITE);
        add(new JScrollPane(orderTable), BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 139, 139));
        JButton addButton = createStyledButton("Add Order");
        JButton deleteButton = createStyledButton("Delete Order");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data
        loadOrders();

        // Add button functionality
        addButton.addActionListener(e -> addOrder());
        deleteButton.addActionListener(e -> deleteOrder());
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

    private void loadOrders() {
        tableModel.setRowCount(0);
        List<Order> orders = orderController.getAllOrders();

        for (Order order : orders) {
            Object[] rowData = {
                    order.getId().toString(),
                    order.getCustId().toString(),
                    new SimpleDateFormat("yyyy-MM-dd").format(order.getOrdDate()),
                    order.getStatus(),
                    order.getTotAmt()
            };
            tableModel.addRow(rowData);
        }
    }

    private void addOrder() {
        JTextField custIDField = new JTextField();
        JTextField ordDateField = new JTextField();
        JTextField statusField = new JTextField();
        JTextField totAmtField = new JTextField();

        Object[] fields = {
                "Customer ID:", custIDField,
                "Order Date (YYYY-MM-DD):", ordDateField,
                "Status:", statusField,
                "Total Amount:", totAmtField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add Order", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Order order = new Order();
                order.setCustId(new org.bson.types.ObjectId(custIDField.getText()));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                order.setOrdDate(formatter.parse(ordDateField.getText()));
                order.setStatus(statusField.getText());
                order.setTotAmt(Double.parseDouble(totAmtField.getText()));

                orderController.addOrder(order);
                loadOrders();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error adding order: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow >= 0) {
            String orderId = (String) tableModel.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this order?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                orderController.deleteOrderById(orderId);
                loadOrders();
                JOptionPane.showMessageDialog(this, "Order deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void setOnOrderSelected(Consumer<String> onOrderSelected) {
        this.onOrderSelected = onOrderSelected;
    }
}

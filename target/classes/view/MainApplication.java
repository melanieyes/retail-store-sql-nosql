package view;

import control.ShoppingCartController;
import model.ShoppingCart;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.awt.*;

public class MainApplication extends JFrame {

    private final String userRole;
    private JPanel contentPanel;

    public MainApplication(String userRole) {
        this.userRole = userRole;

        setTitle("Retail Store System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Setup role-based dashboard
        switch (userRole.toLowerCase()) {
            case "admin":
                setupAdminDashboard();
                break;
            case "manager":
                setupManagerDashboard();
                break;
            case "customer":
                setupCustomerDashboard();
                break;
            default:
                JOptionPane.showMessageDialog(this, "Invalid Role. Closing application.", "Error", JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
        }

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Customer Dashboard setup
    private void setupCustomerDashboard() {
        contentPanel = new JPanel(new CardLayout());
        add(createCustomerSidebar(), BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Create shopping cart
        ShoppingCart cart = new ShoppingCart();
        ShoppingCartView shoppingCartView = new ShoppingCartView();
        new ShoppingCartController(shoppingCartView, cart);

        // Add views to CardLayout
        contentPanel.add(shoppingCartView, "Shopping Cart");

        // Show default view
        showView("Shopping Cart");
    }

    private JPanel createCustomerSidebar() {
        JPanel sidebar = new JPanel(new GridLayout(2, 1, 10, 10));
        sidebar.setBackground(new Color(0, 139, 139));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addSidebarButton(sidebar, "Shopping Cart", () -> showView("Shopping Cart"));
        addSidebarButton(sidebar, "Back to Login", this::backToLogin);

        return sidebar;
    }

    // Admin Dashboard setup
    private void setupAdminDashboard() {
        add(createAdminSidebar(), BorderLayout.WEST);
        contentPanel = new JPanel(new CardLayout());
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createAdminSidebar() {
        JPanel sidebar = new JPanel(new GridLayout(9, 1, 5, 5));
        sidebar.setBackground(new Color(0, 139, 139));

        addSidebarButton(sidebar, "Manage Users", this::showManageUserView);
        addSidebarButton(sidebar, "Product Management", this::showProductView);
        addSidebarButton(sidebar, "Product Categories", this::showProductCategoryView);
        addSidebarButton(sidebar, "Supplier Management", this::showSupplierView);
        addSidebarButton(sidebar, "Customer Management", this::showCustomerView);
        addSidebarButton(sidebar, "Order Management", this::showOrderView);
        addSidebarButton(sidebar, "Order Item Management", this::showStandaloneOrderItemView);
        addSidebarButton(sidebar, "Review Management", this::showReviewView);
        addSidebarButton(sidebar, "Back to Login", this::backToLogin);

        return sidebar;
    }

    // Manager Dashboard setup
    private void setupManagerDashboard() {
        contentPanel = new JPanel(new CardLayout());
        add(createManagerSidebar(), BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        RecentCustomerView recentCustomerView = new RecentCustomerView();
        BestSellingProductView bestSellingProductView = new BestSellingProductView();

        contentPanel.add(recentCustomerView, "Recent Customers");
        contentPanel.add(bestSellingProductView, "Best-Selling Products");

        showView("Recent Customers");
    }

    private JPanel createManagerSidebar() {
        JPanel sidebar = new JPanel(new GridLayout(3, 1, 10, 10));
        sidebar.setBackground(new Color(0, 139, 139));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addSidebarButton(sidebar, "Recent Customers", () -> showView("Recent Customers"));
        addSidebarButton(sidebar, "Best-Selling Products", () -> showView("Best-Selling Products"));
        addSidebarButton(sidebar, "Back to Login", this::backToLogin);

        return sidebar;
    }

    private void addSidebarButton(JPanel sidebar, String text, Runnable onClickAction) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 104, 104));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Dialog", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 139, 139)));
        button.addActionListener(e -> onClickAction.run());
        sidebar.add(button);
    }

    // Show specific view in CardLayout
    private void showView(String viewName) {
        CardLayout layout = (CardLayout) contentPanel.getLayout();
        layout.show(contentPanel, viewName);
    }

    private void backToLogin() {
        dispose();
        SwingUtilities.invokeLater(LoginView::new);
    }

    private void showManageUserView() {
        openView(new ManageUsersView(), "Manage Users");
    }

    private void showCustomerView() {
        openView(new CustomerView(), "Customer Management");
    }

    private void showProductView() {
        openView(new ProductView(), "Product Management");
    }

    private void showOrderView() {
        OrderView orderView = new OrderView();
        orderView.setOnOrderSelected(orderId -> {
            if (orderId != null) {
                ObjectId orderObjId = new ObjectId(orderId);
                openView(new OrderItemView(orderObjId), "Order Item Management"); // Pass ObjectId to OrderItemView constructor
            } else {
                JOptionPane.showMessageDialog(this, "No order selected. Please select an order to view items.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        openView(orderView, "Order Management");
    }

    private void showStandaloneOrderItemView() {
        openView(new OrderItemView(), "Order Item Management"); // No ObjectId for standalone
    }

    private void showProductCategoryView() {
        openView(new ProdCategoryView(), "Product Categories");
    }

    private void showSupplierView() {
        openView(new SupplierView(), "Supplier Management");
    }

    private void showReviewView() {
        openView(new ReviewView(), "Review Management");
    }

    private void openView(JPanel panel, String title) {
        if (contentPanel != null) {
            contentPanel.removeAll();
            contentPanel.add(panel, title);
            contentPanel.revalidate();
            contentPanel.repaint();
        } else {
            JFrame frame = new JFrame(title);
            frame.setContentPane(panel);
            frame.setSize(600, 400);
            frame.setVisible(true);
        }
    }
}

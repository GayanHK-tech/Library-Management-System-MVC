package View;

import Controller.BookController;
import Controller.MemberController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

public class MainMenuView extends JFrame {
    private JButton manageMembers;
    private JButton manageBooks;

    public MainMenuView() {
        setTitle("Library Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JLabel headerLabel = new JLabel("Library Management System");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create buttons with icons (you can add your own icons)
        manageMembers = createMenuButton("Manage Members", "Users management section");
        manageBooks = createMenuButton("Manage Books", "Books management section");

        // Add buttons to panel
        gbc.gridx = 0; gbc.gridy = 0;
        buttonsPanel.add(manageMembers, gbc);
        
        gbc.gridy = 1;
        buttonsPanel.add(manageBooks, gbc);

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFocusPainted(false);
        footerPanel.add(logoutButton);

        // Add components to main panel
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Add logout functionality
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (choice == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginView().setVisible(true);
            }
        });

        // Add button listeners
        manageBooks.addActionListener(e -> {
            BookView bookView = new BookView();
            new BookController(bookView);
            bookView.setVisible(true);
        });
        
        // Add button listeners
        manageMembers.addActionListener(e -> {
            MemberView memberView = new MemberView();
            new MemberController(memberView);
            memberView.setVisible(true);
        });
    }

    private JButton createMenuButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 60));
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setToolTipText(tooltip);
        button.setFocusPainted(false);
        return button;
    }

    public JButton getManageMembersButton() {
        return manageMembers;
    }

    public JButton getManageBooksButton() {
        return manageBooks;
    }
}
package Controller;

import View.LoginView;
import View.MainMenuView;
import model.DatabaseConnection;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {
    private LoginView loginView;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        
        // Add action listener for the login button
        this.loginView.getLoginButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticate();
            }
        });

        // Add action listener for password field to allow Enter key
        this.loginView.getPasswordField().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticate();
            }
        });
    }

    private void authenticate() {
        String username = loginView.getUsernameField().getText().trim();
        String password = new String(loginView.getPasswordField().getPassword());

        // Input validation
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginView,
                "Username and password cannot be empty",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Disable login button while processing
        loginView.getLoginButton().setEnabled(false);
        loginView.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection == null) {
                throw new Exception("Unable to establish database connection");
            }

            System.out.println("Attempting login for user: " + username);
            String query = "SELECT * FROM admin_users WHERE username = ? AND password = ?";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        System.out.println("Login successful for user: " + username);
                        loginView.dispose();
                        SwingUtilities.invokeLater(() -> {
                            MainMenuView mainMenuView = new MainMenuView();
                            mainMenuView.setVisible(true);
                        });
                    } else {
                        System.out.println("Login failed for user: " + username);
                        loginView.showError("Invalid username or password");
                        loginView.getPasswordField().setText("");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(loginView,
                "An error occurred during login: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } finally {
            // Re-enable login button and restore cursor
            loginView.getLoginButton().setEnabled(true);
            loginView.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
}
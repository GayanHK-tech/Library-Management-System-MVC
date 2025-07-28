package Controller;

import View.MemberView;
import Model.Member;
import Model.MembershipCard;
import model.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberController {
    private MemberView memberView;

    public MemberController(MemberView memberView) {
        this.memberView = memberView;
        
        // Initialize listeners
        this.memberView.getAddButton().addActionListener(e -> addMember());
        this.memberView.getUpdateButton().addActionListener(e -> updateMember());
        this.memberView.getDeleteButton().addActionListener(e -> deleteMember());
        
        // Load initial data
        loadMembers();
    }

    private void loadMembers() {
        DefaultTableModel model = memberView.getTableModel();
        model.setRowCount(0); // Clear existing rows

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM members";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Object[] row = {
                    resultSet.getString("memberID"),
                    resultSet.getString("name"),
                    resultSet.getString("contactInfo"),
                    resultSet.getString("cardNumber"),
                    resultSet.getString("expirationDate")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(memberView, "Error loading members.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addMember() {
        try {
            String memberID = memberView.getMemberIDField().getText();
            String name = memberView.getNameField().getText();
            String contactInfo = memberView.getContactInfoField().getText();
            String cardNumber = memberView.getCardNumberField().getText();
            String expirationDate = memberView.getExpirationDateField().getText();

            // Validate input
            if (memberID.isEmpty() || name.isEmpty() || contactInfo.isEmpty() || 
                cardNumber.isEmpty() || expirationDate.isEmpty()) {
                JOptionPane.showMessageDialog(memberView, "All fields must be filled.", 
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO members (memberID, name, contactInfo, cardNumber, expirationDate) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, memberID);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, contactInfo);
                preparedStatement.setString(4, cardNumber);
                preparedStatement.setString(5, expirationDate);
                preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(memberView, "Member added successfully.", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadMembers(); // Refresh table
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(memberView, "Error adding member.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateMember() {
        String memberID = memberView.getMemberIDField().getText();
        if (memberID.isEmpty()) {
            JOptionPane.showMessageDialog(memberView, "Please select a member to update.", 
                "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String name = memberView.getNameField().getText();
            String contactInfo = memberView.getContactInfoField().getText();
            String cardNumber = memberView.getCardNumberField().getText();
            String expirationDate = memberView.getExpirationDateField().getText();

            // Validate input
            if (name.isEmpty() || contactInfo.isEmpty() || 
                cardNumber.isEmpty() || expirationDate.isEmpty()) {
                JOptionPane.showMessageDialog(memberView, "All fields must be filled.", 
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                // First check if member exists
                String checkQuery = "SELECT * FROM members WHERE memberID = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                checkStatement.setString(1, memberID);
                ResultSet resultSet = checkStatement.executeQuery();

                if (!resultSet.next()) {
                    JOptionPane.showMessageDialog(memberView, "Member ID not found.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Proceed with update
                String query = "UPDATE members SET name = ?, contactInfo = ?, cardNumber = ?, expirationDate = ? WHERE memberID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, contactInfo);
                preparedStatement.setString(3, cardNumber);
                preparedStatement.setString(4, expirationDate);
                preparedStatement.setString(5, memberID);
                preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(memberView, "Member updated successfully.", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadMembers(); // Refresh table
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(memberView, "Error updating member.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMember() {
        String memberID = memberView.getMemberIDField().getText();

        if (memberID.isEmpty()) {
            JOptionPane.showMessageDialog(memberView, "Please select a member to delete.", 
                "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(memberView, 
            "Are you sure you want to delete this member?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "DELETE FROM members WHERE memberID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, memberID);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(memberView, "Member deleted successfully.", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    loadMembers(); // Refresh table
                } else {
                    JOptionPane.showMessageDialog(memberView, "Member not found.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(memberView, "Error deleting member.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearFields() {
        memberView.getMemberIDField().setText("");
        memberView.getNameField().setText("");
        memberView.getContactInfoField().setText("");
        memberView.getCardNumberField().setText("");
        memberView.getExpirationDateField().setText("");
        memberView.getMemberTable().clearSelection();
    }
}
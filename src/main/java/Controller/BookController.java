package Controller;

import View.BookView;
import Model.Book;
import model.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookController {
    private BookView bookView;

    public BookController(BookView bookView) {
        this.bookView = bookView;
        
        // Initialize listeners
        this.bookView.getAddButton().addActionListener(e -> addBook());
        this.bookView.getUpdateButton().addActionListener(e -> updateBook());
        this.bookView.getDeleteButton().addActionListener(e -> deleteBook());
        
        // Load initial data
        loadBooks();
    }

    private void loadBooks() {
        DefaultTableModel model = bookView.getTableModel();
        model.setRowCount(0); // Clear existing rows

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM books";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Object[] row = {
                    resultSet.getString("bookID"),
                    resultSet.getString("title"),
                    resultSet.getString("author"),
                    resultSet.getInt("yearPublished")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(bookView, "Error loading books.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addBook() {
        try {
            String bookID = bookView.getBookIDField().getText();
            String title = bookView.getTitleField().getText();
            String author = bookView.getAuthorField().getText();
            int yearPublished = Integer.parseInt(bookView.getYearPublishedField().getText());

            // Validate input
            if (bookID.isEmpty() || title.isEmpty() || author.isEmpty()) {
                JOptionPane.showMessageDialog(bookView, "All fields must be filled.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO books (bookID, title, author, yearPublished) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, bookID);
                preparedStatement.setString(2, title);
                preparedStatement.setString(3, author);
                preparedStatement.setInt(4, yearPublished);
                preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(bookView, "Book added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadBooks(); // Refresh table
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(bookView, "Year must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(bookView, "Error adding book.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBook() {
        try {
            String bookID = bookView.getBookIDField().getText();
            String title = bookView.getTitleField().getText();
            String author = bookView.getAuthorField().getText();
            int yearPublished = Integer.parseInt(bookView.getYearPublishedField().getText());

            // Validate input
            if (bookID.isEmpty() || title.isEmpty() || author.isEmpty()) {
                JOptionPane.showMessageDialog(bookView, "All fields must be filled.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                // First check if book exists
                String checkQuery = "SELECT * FROM books WHERE bookID = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                checkStatement.setString(1, bookID);
                ResultSet resultSet = checkStatement.executeQuery();

                if (!resultSet.next()) {
                    JOptionPane.showMessageDialog(bookView, "Book ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Proceed with update
                String query = "UPDATE books SET title = ?, author = ?, yearPublished = ? WHERE bookID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, author);
                preparedStatement.setInt(3, yearPublished);
                preparedStatement.setString(4, bookID);
                preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(bookView, "Book updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadBooks(); // Refresh table
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(bookView, "Year must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(bookView, "Error updating book.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBook() {
        String bookID = bookView.getBookIDField().getText();

        if (bookID.isEmpty()) {
            JOptionPane.showMessageDialog(bookView, "Please select a book to delete.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(bookView, 
            "Are you sure you want to delete this book?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "DELETE FROM books WHERE bookID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, bookID);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(bookView, "Book deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    loadBooks(); // Refresh table
                } else {
                    JOptionPane.showMessageDialog(bookView, "Book not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(bookView, "Error deleting book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearFields() {
        bookView.getBookIDField().setText("");
        bookView.getTitleField().setText("");
        bookView.getAuthorField().setText("");
        bookView.getYearPublishedField().setText("");
        bookView.getBookTable().clearSelection();
    }
}
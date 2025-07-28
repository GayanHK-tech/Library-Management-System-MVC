package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class BookView extends JFrame {
    private JTextField bookIDField;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField yearPublishedField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JTable bookTable;
    private DefaultTableModel tableModel;

    public BookView() {
        setTitle("Manage Books");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Book Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add form components
        addFormField(formPanel, "Book ID:", bookIDField = new JTextField(20), gbc, 0);
        addFormField(formPanel, "Title:", titleField = new JTextField(20), gbc, 1);
        addFormField(formPanel, "Author:", authorField = new JTextField(20), gbc, 2);
        addFormField(formPanel, "Year Published:", yearPublishedField = new JTextField(20), gbc, 3);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Add Book");
        updateButton = new JButton("Update Book");
        deleteButton = new JButton("Delete Book");
        clearButton = new JButton("Clear Fields");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Table
        String[] columns = {"Book ID", "Title", "Author", "Year Published"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);

        // Add components to main panel
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Add clear functionality
        clearButton.addActionListener(e -> clearFields());

        // Add table selection listener
        bookTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow != -1) {
                    bookIDField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    titleField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    authorField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    yearPublishedField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                }
            }
        });
    }

    private void addFormField(JPanel panel, String label, JTextField field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void clearFields() {
        bookIDField.setText("");
        titleField.setText("");
        authorField.setText("");
        yearPublishedField.setText("");
        bookTable.clearSelection();
    }

    // Getters
    public JTextField getBookIDField() { return bookIDField; }
    public JTextField getTitleField() { return titleField; }
    public JTextField getAuthorField() { return authorField; }
    public JTextField getYearPublishedField() { return yearPublishedField; }
    public JButton getAddButton() { return addButton; }
    public JButton getUpdateButton() { return updateButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTable getBookTable() { return bookTable; }
}
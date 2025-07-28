package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class MemberView extends JFrame {
    private JTextField memberIDField;
    private JTextField nameField;
    private JTextField contactInfoField;
    private JTextField cardNumberField;
    private JTextField expirationDateField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JTable memberTable;
    private DefaultTableModel tableModel;

    public MemberView() {
        setTitle("Member Management");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Member Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add form components
        addFormField(formPanel, "Member ID:", memberIDField = new JTextField(20), gbc, 0);
        addFormField(formPanel, "Name:", nameField = new JTextField(20), gbc, 1);
        addFormField(formPanel, "Contact Info:", contactInfoField = new JTextField(20), gbc, 2);
        addFormField(formPanel, "Card Number:", cardNumberField = new JTextField(20), gbc, 3);
        addFormField(formPanel, "Expiration Date:", expirationDateField = new JTextField(20), gbc, 4);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Add Member");
        updateButton = new JButton("Update Member");
        deleteButton = new JButton("Delete Member");
        clearButton = new JButton("Clear Fields");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Table
        String[] columns = {"Member ID", "Name", "Contact Info", "Card Number", "Expiration Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        memberTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(memberTable);

        // Add components to main panel
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Add clear functionality
        clearButton.addActionListener(e -> clearFields());

        // Add table selection listener
        memberTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = memberTable.getSelectedRow();
                if (selectedRow != -1) {
                    memberIDField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    contactInfoField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    cardNumberField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    expirationDateField.setText(tableModel.getValueAt(selectedRow, 4).toString());
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
        memberIDField.setText("");
        nameField.setText("");
        contactInfoField.setText("");
        cardNumberField.setText("");
        expirationDateField.setText("");
        memberTable.clearSelection();
    }

    // Getters
    public JTextField getMemberIDField() { return memberIDField; }
    public JTextField getNameField() { return nameField; }
    public JTextField getContactInfoField() { return contactInfoField; }
    public JTextField getCardNumberField() { return cardNumberField; }
    public JTextField getExpirationDateField() { return expirationDateField; }
    public JButton getAddButton() { return addButton; }
    public JButton getUpdateButton() { return updateButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTable getMemberTable() { return memberTable; }
}
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;

public class ContactGUI extends JFrame {
    private ContactDAO contactDAO;

    private JTable contactTable;
    private DefaultTableModel tableModel;

    public ContactGUI(ContactDAO contactDAO) {
        this.contactDAO = contactDAO;

        setTitle("Contact Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create table to display contacts
        createTable();

        // Add buttons for adding, editing, and deleting contacts
        addButtons();

        setVisible(true);
    }

    private void createTable() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("Street");
        tableModel.addColumn("City");
        tableModel.addColumn("Postal Code");
        tableModel.addColumn("Phone Number");

        contactTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(contactTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load existing contacts into the table
        loadContacts();
    }

    private void loadContacts() {
        try {
            // Retrieve contacts from the database using your ContactDAO
            Contact[] contacts = contactDAO.getAllContacts();

            // Clear existing table data
            tableModel.setRowCount(0);

            // Add each contact to the table
            for (Contact contact : contacts) {
                addContactToTable(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading contacts from database.");
        }
    }

    private void addContactToTable(Contact contact) {
        Vector<String> row = new Vector<>();
        row.add(contact.getFirstName());
        row.add(contact.getLastName());
        row.add(contact.getStreet());
        row.add(contact.getCity());
        row.add(contact.getPostalCode());
        row.add(contact.getPhoneNumber());
        tableModel.addRow(row);
    }

    private void addButtons() {
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Contact");
        JButton editButton = new JButton("Edit Contact");
        JButton deleteButton = new JButton("Delete Contact");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a dialog for adding a new contact
                new AddContactDialog(ContactGUI.this, contactDAO);
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get selected row from table
                // Open a dialog for editing the selected contact
                // Implement this method
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get selected row from table
                // Delete the contact associated with the selected row
                // Implement this method
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}

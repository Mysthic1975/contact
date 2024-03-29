import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContactGUI extends JFrame {
    private final ContactDAO contactDAO;
    private static final Logger LOGGER = Logger.getLogger(ContactGUI.class.getName());

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
            LOGGER.log(Level.SEVERE, "Error loading contacts from database.", e);
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

        addButton.addActionListener(_ -> new AddContactDialog(ContactGUI.this, contactDAO));

        editButton.addActionListener(_ -> {
            int selectedRow = contactTable.getSelectedRow();
            if (selectedRow >= 0) {
                String firstName = (String) tableModel.getValueAt(selectedRow, 0);
                String lastName = (String) tableModel.getValueAt(selectedRow, 1);
                try {
                    Contact selectedContact = contactDAO.getContact(firstName, lastName);
                    if (selectedContact != null) {
                        new EditContactDialog(ContactGUI.this, contactDAO, selectedContact);
                        loadContacts(); // Reload contacts after editing
                    } else {
                        JOptionPane.showMessageDialog(ContactGUI.this, "Error: Selected contact not found.");
                    }
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error retrieving contact.", ex);
                    JOptionPane.showMessageDialog(ContactGUI.this, "Error retrieving contact.");
                }
            } else {
                JOptionPane.showMessageDialog(ContactGUI.this, "Please select a contact to edit.");
            }
        });

        deleteButton.addActionListener(_ -> {
            int selectedRow = contactTable.getSelectedRow();
            if (selectedRow >= 0) {
                String firstName = (String) tableModel.getValueAt(selectedRow, 0);
                String lastName = (String) tableModel.getValueAt(selectedRow, 1);
                try {
                    Contact selectedContact = contactDAO.getContact(firstName, lastName);
                    if (selectedContact != null) {
                        contactDAO.deleteContact(selectedContact);
                        tableModel.removeRow(selectedRow);
                    } else {
                        JOptionPane.showMessageDialog(ContactGUI.this, "Error: Selected contact not found.");
                    }
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error deleting contact.", ex);
                    JOptionPane.showMessageDialog(ContactGUI.this, "Error deleting contact.");
                }
            } else {
                JOptionPane.showMessageDialog(ContactGUI.this, "Please select a contact to delete.");
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}

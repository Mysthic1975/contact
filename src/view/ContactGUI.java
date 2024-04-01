package view;

import controller.ContactController;
import model.Contact;
import utils.ValidationUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContactGUI extends JFrame {
    private final ContactController contactController;
    private static final Logger LOGGER = Logger.getLogger(ContactGUI.class.getName());

    private JTable contactTable;
    private DefaultTableModel tableModel;
    private JTextField filterTextField;
    private JComboBox<String> filterComboBox;

    public ContactGUI(ContactController contactController) {
        this.contactController = contactController;

        setTitle("Kontaktverwaltung");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create table to display contacts
        createTable();

        // Add buttons for adding, editing, and deleting contacts
        addButtons();

        // Add filter components
        addFilterComponents();

        setVisible(true);
    }

    private void addFilterComponents() {
        JPanel filterPanel = new JPanel();
        filterTextField = new JTextField(20);
        filterComboBox = new JComboBox<>(new String[]{"Vorname", "Nachname", "Stadt", "Postleitzahl", "Telefonnummer"});
        JButton filterButton = new JButton("Filtern");
        JButton showAllButton = new JButton("Alle anzeigen"); // Neuer Button

        filterButton.addActionListener(_ -> {
            String filterField = (String) filterComboBox.getSelectedItem();
            String filterValue = filterTextField.getText();

            try {
                List<Contact> filteredContacts = contactController.getFilteredContacts(filterField, filterValue);
                tableModel.setRowCount(0); // Clear existing table data
                for (Contact contact : filteredContacts) {
                    addContactToTable(contact);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error filtering contacts.", e);
                JOptionPane.showMessageDialog(this, "Error filtering contacts.");
            }
        });

        // Fügt einen ActionListener zum "Alle anzeigen" Button hinzu
        showAllButton.addActionListener(_ -> loadContacts());

        filterPanel.add(filterComboBox);
        filterPanel.add(filterTextField);
        filterPanel.add(filterButton);
        filterPanel.add(showAllButton); // Fügt den "Alle anzeigen" Button zum Panel hinzu
        add(filterPanel, BorderLayout.NORTH);
    }

    private void createTable() {
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // This causes all cells to be not editable
                return false;
            }
        };
        tableModel.addColumn("Vornamen");
        tableModel.addColumn("Nachnamen");
        tableModel.addColumn("Straße");
        tableModel.addColumn("Ort");
        tableModel.addColumn("Postleitzahl");
        tableModel.addColumn("Telefonnummer");

        contactTable = new JTable(tableModel);
        contactTable.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(contactTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load existing contacts into the table
        loadContacts();
    }

    private void loadContacts() {
        try {
            // Retrieve contacts from the database using your ContactController
            Contact[] contacts = contactController.getAllContacts();

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
        JButton addButton = new JButton("Kontakt hinzufügen");
        JButton editButton = new JButton("Kontakt bearbeiten");
        JButton deleteButton = new JButton("Kontakt löschen");

        addButton.addActionListener(_ -> {
            new AddContactDialog(ContactGUI.this, contactController);
            loadContacts(); // Reload contacts after adding
        });

        editButton.addActionListener(_ -> {
            int selectedRow = contactTable.getSelectedRow();
            if (ValidationUtils.validateSelectedRow(selectedRow, ContactGUI.this)) {
                selectedRow = contactTable.convertRowIndexToModel(selectedRow);
                String firstName = (String) tableModel.getValueAt(selectedRow, 0);
                String lastName = (String) tableModel.getValueAt(selectedRow, 1);
                try {
                    Contact selectedContact = contactController.getContact(firstName, lastName);
                    if (selectedContact != null) {
                        new EditContactDialog(ContactGUI.this, contactController, selectedContact);
                        loadContacts(); // Reload contacts after editing
                    } else {
                        JOptionPane.showMessageDialog(ContactGUI.this, "Error: Selected contact not found.");
                    }
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error retrieving contact.", ex);
                    JOptionPane.showMessageDialog(ContactGUI.this, "Error retrieving contact.");
                }
            }
        });

        deleteButton.addActionListener(_ -> {
            int selectedRow = contactTable.getSelectedRow();
            if (ValidationUtils.validateSelectedRow(selectedRow, ContactGUI.this)) {
                selectedRow = contactTable.convertRowIndexToModel(selectedRow);
                String firstName = (String) tableModel.getValueAt(selectedRow, 0);
                String lastName = (String) tableModel.getValueAt(selectedRow, 1);
                try {
                    Contact selectedContact = contactController.getContact(firstName, lastName);
                    if (selectedContact != null) {
                        // Show confirmation dialog before deleting
                        int response = JOptionPane.showConfirmDialog(
                                ContactGUI.this,
                                STR."Möchten Sie \{firstName} \{lastName} wirklich löschen?",
                                "Bestätigen",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE
                        );
                        if (response == JOptionPane.YES_OPTION) {
                            contactController.deleteContact(selectedContact);
                            tableModel.removeRow(selectedRow);
                        }
                    } else {
                        JOptionPane.showMessageDialog(ContactGUI.this, "Error: Selected contact not found.");
                    }
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error deleting contact.", ex);
                    JOptionPane.showMessageDialog(ContactGUI.this, "Error deleting contact.");
                }
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
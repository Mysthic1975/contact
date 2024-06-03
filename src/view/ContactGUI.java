package view;

import controller.ContactController;
import model.Contact;
import utils.ValidationUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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

    // Konstruktor für die Kontaktverwaltung GUI
    public ContactGUI(ContactController contactController) {
        this.contactController = contactController;

        setTitle("Kontaktverwaltung");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Erstellen Sie eine Tabelle zur Anzeige von Kontakten
        createTable();

        // Fügen Sie Schaltflächen zum Hinzufügen, Bearbeiten und Löschen von Kontakten hinzu
        addButtons();

        // Fügen Sie Filterkomponenten hinzu
        addFilterComponents();

        setVisible(true);
    }

    // Methode zum Hinzufügen von Filterkomponenten
    private void addFilterComponents() {
        JPanel filterPanel = new JPanel();
        filterTextField = new JTextField(20);
        filterComboBox = new JComboBox<>(new String[]{"Vorname", "Nachname", "Stadt", "Postleitzahl", "Telefonnummer"});
        JButton filterButton = new JButton("Filtern");
        JButton showAllButton = new JButton("Alle anzeigen"); // Neuer Button

        // ActionListener für den Filter-Button
        filterButton.addActionListener(_ -> {
            String filterField = (String) filterComboBox.getSelectedItem();
            String filterValue = filterTextField.getText();

            try {
                List<Contact> filteredContacts = contactController.getFilteredContacts(filterField, filterValue);
                tableModel.setRowCount(0); // Löschen Sie vorhandene Tabellendaten
                for (Contact contact : filteredContacts) {
                    addContactToTable(contact);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Fehler beim Filtern von Kontakten.", e);
                JOptionPane.showMessageDialog(this, "Fehler beim Filtern von Kontakten.");
            }
        });

        // ActionListener für den "Alle anzeigen" Button
        showAllButton.addActionListener(_ -> loadContacts());

        filterPanel.add(filterComboBox);
        filterPanel.add(filterTextField);
        filterPanel.add(filterButton);
        filterPanel.add(showAllButton); // Fügen Sie den "Alle anzeigen" Button zum Panel hinzu
        add(filterPanel, BorderLayout.NORTH);
    }

    // Methode zum Erstellen der Tabelle
    private void createTable() {
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Alle Zellen sind nicht editierbar
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

        // Setzen Sie die Hintergrundfarbe der Tabelle
        contactTable.setBackground(Color.BLUE);

        // Setzen Sie die Hintergrundfarbe der Zellen
        contactTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(contactTable);
        scrollPane.getViewport().setBackground(Color.darkGray);

        // Erstellen Sie ein JPanel als Container
        JPanel tableContainer = new JPanel(new BorderLayout());

        // Fügen Sie etwas Padding zum Container hinzu
        tableContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Fügen Sie die scrollPane zum Container hinzu
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        // Fügen Sie den Container anstelle der scrollPane zum Frame hinzu
        add(tableContainer, BorderLayout.CENTER);

        // Laden Sie vorhandene Kontakte in die Tabelle
        loadContacts();
    }

    // Methode zum Laden von Kontakten
    private void loadContacts() {
        try {
            Contact[] contacts = contactController.getAllContacts();
            tableModel.setRowCount(0); // Löschen Sie vorhandene Tabellendaten
            for (Contact contact : contacts) {
                addContactToTable(contact);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Fehler beim Laden von Kontakten.", e);
            JOptionPane.showMessageDialog(this, "Fehler beim Laden von Kontakten.");
        }
    }

    // Methode zum Hinzufügen eines Kontakts zur Tabelle
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

    // Methode zum Hinzufügen von Schaltflächen
    private void addButtons() {
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Kontakt hinzufügen");
        JButton editButton = new JButton("Kontakt bearbeiten");
        JButton deleteButton = new JButton("Kontakt löschen");

        addButton.addActionListener(_ -> {
            new AddContactDialog(ContactGUI.this, contactController);
            loadContacts(); // Kontakte neu laden nach dem Hinzufügen
        });

        editButton.addActionListener(_ -> {
            int selectedRow = contactTable.getSelectedRow();
            if (ValidationUtils.validateSelectedRow(selectedRow, ContactGUI.this)) {
                selectedRow = contactTable.convertRowIndexToModel(selectedRow);
                String firstName = (String) tableModel.getValueAt(selectedRow, 0);
                String lastName = (String) tableModel.getValueAt(selectedRow, 1);
                try {
                    Contact contact = contactController.getContact(firstName, lastName);
                    new EditContactDialog(ContactGUI.this, contactController, contact);
                    loadContacts(); // Kontakte neu laden nach dem Bearbeiten
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Fehler beim Abrufen des Kontakts.", ex);
                    JOptionPane.showMessageDialog(ContactGUI.this, "Fehler beim Abrufen des Kontakts.");
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
                    Contact contact = contactController.getContact(firstName, lastName);
                    contactController.deleteContact(contact);
                    loadContacts(); // Kontakte neu laden nach dem Löschen
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Fehler beim Löschen des Kontakts.", ex);
                    JOptionPane.showMessageDialog(ContactGUI.this, "Fehler beim Löschen des Kontakts.");
                }
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
package view;

import controller.ContactController;
import model.Contact;
import utils.ValidationUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AddContactDialog extends JDialog {
    private static final int TEXT_FIELD_SIZE = 20;

    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField streetField;
    private final JTextField cityField;
    private final JTextField postalCodeField;
    private final JTextField phoneNumberField;

    // Konstruktor für den Dialog zum Hinzufügen von Kontakten
    public AddContactDialog(JFrame parent, ContactController contactController) {
        super(parent, "Kontakt hinzufügen", true);

        // Erstellen und Konfigurieren der Eingabefelder
        firstNameField = new JTextField(TEXT_FIELD_SIZE);
        lastNameField = new JTextField(TEXT_FIELD_SIZE);
        streetField = new JTextField(TEXT_FIELD_SIZE);
        cityField = new JTextField(TEXT_FIELD_SIZE);
        postalCodeField = new JTextField(TEXT_FIELD_SIZE);
        phoneNumberField = new JTextField(TEXT_FIELD_SIZE);

        // Erstellen und Konfigurieren der Labels für die Eingabefelder
        JLabel firstNameLabel = new JLabel("Vorname:");
        JLabel lastNameLabel = new JLabel("Nachname:");
        JLabel streetLabel = new JLabel("Straße:");
        JLabel cityLabel = new JLabel("Stadt:");
        JLabel postalCodeLabel = new JLabel("Postleitzahl:");
        JLabel phoneNumberLabel = new JLabel("Telefonnummer:");

        // Erstellen der OK- und Abbrechen-Buttons
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        // Hinzufügen von ActionListeners zu den Buttons
        okButton.addActionListener(_ -> {
            // Eingabewerte abrufen
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String street = streetField.getText();
            String city = cityField.getText();
            String postalCode = postalCodeField.getText();
            String phoneNumber = phoneNumberField.getText();

            // Eingabe validieren
            if (ValidationUtils.validateInputFields(firstName, lastName, phoneNumber, postalCode, this)) {
                return;
            }

            // Neuen Kontakt erstellen
            Contact newContact = new Contact(firstName, lastName, street, city, postalCode, phoneNumber);

            // Kontakt zur Datenbank hinzufügen
            try {
                contactController.addContact(newContact);
                dispose(); // Dialog schließen nach dem Hinzufügen des Kontakts
            } catch (SQLException ex) {
                String errorMessage = String.format("Error adding contact: %s", ex.getMessage());
                JOptionPane.showMessageDialog(AddContactDialog.this, errorMessage);
            }
        });

        cancelButton.addActionListener(_ -> {
            dispose(); // Dialog schließen ohne Hinzufügen des Kontakts
        });

        // Panel erstellen, um Eingabefelder und Labels zu halten
        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        inputPanel.add(firstNameLabel);
        inputPanel.add(firstNameField);
        inputPanel.add(lastNameLabel);
        inputPanel.add(lastNameField);
        inputPanel.add(streetLabel);
        inputPanel.add(streetField);
        inputPanel.add(cityLabel);
        inputPanel.add(cityField);
        inputPanel.add(postalCodeLabel);
        inputPanel.add(postalCodeField);
        inputPanel.add(phoneNumberLabel);
        inputPanel.add(phoneNumberField);

        // Panel erstellen, um die Buttons zu halten
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Eingabe-Panel und Button-Panel zum Dialog hinzufügen
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Dialogeigenschaften setzen
        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
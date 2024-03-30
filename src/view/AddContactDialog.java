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

    public AddContactDialog(JFrame parent, ContactController contactController) {
        super(parent, "Kontakt hinzufügen", true);

        // Create and configure input fields
        firstNameField = new JTextField(TEXT_FIELD_SIZE);
        lastNameField = new JTextField(TEXT_FIELD_SIZE);
        streetField = new JTextField(TEXT_FIELD_SIZE);
        cityField = new JTextField(TEXT_FIELD_SIZE);
        postalCodeField = new JTextField(TEXT_FIELD_SIZE);
        phoneNumberField = new JTextField(TEXT_FIELD_SIZE);

        // Create and configure labels for input fields
        JLabel firstNameLabel = new JLabel("Vorname:");
        JLabel lastNameLabel = new JLabel("Nachname:");
        JLabel streetLabel = new JLabel("Straße:");
        JLabel cityLabel = new JLabel("Stadt:");
        JLabel postalCodeLabel = new JLabel("Postleitzahl:");
        JLabel phoneNumberLabel = new JLabel("Telefonnummer:");

        // Create OK and Cancel buttons
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        // Add action listeners to buttons
        okButton.addActionListener(_ -> {
            // Get input values
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String street = streetField.getText();
            String city = cityField.getText();
            String postalCode = postalCodeField.getText();
            String phoneNumber = phoneNumberField.getText();

            // Validate input
            if (ValidationUtils.validateInputFields(firstName, lastName, phoneNumber, postalCode, this)) {
                return;
            }

            // Create new contact
            Contact newContact = new Contact(firstName, lastName, street, city, postalCode, phoneNumber);

            // Add contact to database
            try {
                contactController.addContact(newContact);
                dispose(); // Close dialog after adding contact
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(AddContactDialog.this, STR."Error adding contact: \{ex.getMessage()}");
            }
        });

        cancelButton.addActionListener(_ -> {
            dispose(); // Close dialog without adding contact
        });

        // Create panel to hold input fields and labels
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

        // Create panel to hold buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Add input panel and button panel to dialog
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set dialog properties
        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
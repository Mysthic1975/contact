import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class EditContactDialog extends JDialog {
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField streetField;
    private final JTextField cityField;
    private final JTextField postalCodeField;
    private final JTextField phoneNumberField;

    public EditContactDialog(JFrame parent, ContactDAO contactDAO, Contact contact) {
        super(parent, "Kontakt bearbeiten", true);

        // Create and configure input fields
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        streetField = new JTextField(20);
        cityField = new JTextField(20);
        postalCodeField = new JTextField(10);
        phoneNumberField = new JTextField(15);

        // Load contact data into fields
        firstNameField.setText(contact.getFirstName());
        lastNameField.setText(contact.getLastName());
        streetField.setText(contact.getStreet());
        cityField.setText(contact.getCity());
        postalCodeField.setText(contact.getPostalCode());
        phoneNumberField.setText(contact.getPhoneNumber());

        // Create and configure labels for input fields
        JLabel firstNameLabel = new JLabel("Vorname:");
        JLabel lastNameLabel = new JLabel("Nachname:");
        JLabel streetLabel = new JLabel("Straße:");
        JLabel cityLabel = new JLabel("Stadt:");
        JLabel postalCodeLabel = new JLabel("Postleitzahl:");
        JLabel phoneNumberLabel = new JLabel("Telefonnummer:");

        // Create OK and Cancel buttons
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Abbrechen");

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
            if (firstName.isEmpty() || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(EditContactDialog.this, "Bitte geben Sie einen Vornamen und Nachnamen ein.");
                return;
            }

            if (!phoneNumber.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Die Telefonnummer sollte nur Ziffern enthalten.");
                return;
            }

            if (!firstName.matches("[a-zA-ZäöüÄÖÜß]+") || !lastName.matches("[a-zA-ZäöüÄÖÜß]+")) {
                JOptionPane.showMessageDialog(this, "Vorname und Nachname sollten nur Buchstaben enthalten.");
                return;
            }

            if (!postalCode.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Die Postleitzahl sollte nur Ziffern enthalten.");
                return;
            }

            // Update contact
            contact.setFirstName(firstName);
            contact.setLastName(lastName);
            contact.setStreet(street);
            contact.setCity(city);
            contact.setPostalCode(postalCode);
            contact.setPhoneNumber(phoneNumber);

            // Update contact in database
            try {
                contactDAO.updateContact(contact); // Use the updated contact with the id
                dispose(); // Close dialog after updating contact
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(EditContactDialog.this, STR."Fehler beim Aktualisieren des Kontakts: \{ex.getMessage()}");
            }
        });

        cancelButton.addActionListener(_ -> {
            dispose(); // Close dialog without updating contact
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

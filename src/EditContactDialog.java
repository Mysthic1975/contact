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
        super(parent, "Edit Contact", true);

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
            if (firstName.isEmpty() || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(EditContactDialog.this, "Please enter a first name and last name.");
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
                JOptionPane.showMessageDialog(EditContactDialog.this, STR."Error updating contact: \{ex.getMessage()}");
            }
        });

        cancelButton.addActionListener(_ -> {
            dispose(); // Close dialog without updating contact
        });

        // Create panel to hold input fields and labels
        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        inputPanel.add(new JLabel("First Name:"));
        inputPanel.add(firstNameField);
        inputPanel.add(new JLabel("Last Name:"));
        inputPanel.add(lastNameField);
        inputPanel.add(new JLabel("Street:"));
        inputPanel.add(streetField);
        inputPanel.add(new JLabel("City:"));
        inputPanel.add(cityField);
        inputPanel.add(new JLabel("Postal Code:"));
        inputPanel.add(postalCodeField);
        inputPanel.add(new JLabel("Phone Number:"));
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

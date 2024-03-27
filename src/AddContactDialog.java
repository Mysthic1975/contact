import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AddContactDialog extends JDialog {
    private JTextField firstNameField, lastNameField, streetField, cityField, postalCodeField, phoneNumberField;
    private ContactDAO contactDAO;

    public AddContactDialog(JFrame parent, ContactDAO contactDAO) {
        super(parent, "Add Contact", true);
        this.contactDAO = contactDAO;

        // Create and configure input fields
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        streetField = new JTextField(20);
        cityField = new JTextField(20);
        postalCodeField = new JTextField(10);
        phoneNumberField = new JTextField(15);

        // Create and configure labels for input fields
        JLabel firstNameLabel = new JLabel("First Name:");
        JLabel lastNameLabel = new JLabel("Last Name:");
        JLabel streetLabel = new JLabel("Street:");
        JLabel cityLabel = new JLabel("City:");
        JLabel postalCodeLabel = new JLabel("Postal Code:");
        JLabel phoneNumberLabel = new JLabel("Phone Number:");

        // Create OK and Cancel buttons
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        // Add action listeners to buttons
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get input values
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String street = streetField.getText();
                String city = cityField.getText();
                String postalCode = postalCodeField.getText();
                String phoneNumber = phoneNumberField.getText();

                // Validate input
                if (firstName.isEmpty() || lastName.isEmpty()) {
                    JOptionPane.showMessageDialog(AddContactDialog.this, "Please enter a first name and last name.");
                    return;
                }

                // Create new contact
                Contact newContact = new Contact(firstName, lastName, street, city, postalCode, phoneNumber);

                // Add contact to database
                try {
                    contactDAO.addContact(newContact);
                    dispose(); // Close dialog after adding contact
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(AddContactDialog.this, "Error adding contact: " + ex.getMessage());
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close dialog without adding contact
            }
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


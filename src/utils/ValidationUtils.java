package utils;

import javax.swing.*;
import java.awt.*;

public class ValidationUtils {

    public static boolean validateInputFields(String firstName, String lastName, String phoneNumber, String postalCode, Component parentComponent) {
        if (firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(parentComponent, "Bitte geben Sie einen Vornamen und Nachnamen ein.");
            return true;
        }

        if (!phoneNumber.matches("\\d+")) {
            JOptionPane.showMessageDialog(parentComponent, "Die Telefonnummer sollte nur Ziffern enthalten.");
            return true;
        }

        if (!firstName.matches("[a-zA-ZäöüÄÖÜß]+") || !lastName.matches("[a-zA-ZäöüÄÖÜß]+")) {
            JOptionPane.showMessageDialog(parentComponent, "Vorname und Nachname sollten nur Buchstaben enthalten.");
            return true;
        }

        if (!postalCode.matches("\\d+")) {
            JOptionPane.showMessageDialog(parentComponent, "Die Postleitzahl sollte nur Ziffern enthalten.");
            return true;
        }

        return false;
    }
}

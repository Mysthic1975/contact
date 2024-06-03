package utils;

import javax.swing.*;
import java.awt.*;

public class ValidationUtils {

    // Methode zur Validierung der Eingabefelder
    // überprüft, ob der Vorname und der Nachname nicht leer sind
    // überprüft, ob die Telefonnummer und die Postleitzahl nur Ziffern enthalten
    // überprüft, ob der Vorname und der Nachname nur Buchstaben enthalten
    // gibt true zurück, wenn ein Fehler gefunden wurde, sonst false
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

    // Methode zur Validierung der ausgewählten Zeile in der Tabelle
    // überprüft, ob eine Zeile ausgewählt wurde
    // gibt false zurück, wenn keine Zeile ausgewählt wurde, sonst true
    public static boolean validateSelectedRow(int selectedRow, Component parentComponent) {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(parentComponent, "Bitte wählen Sie einen Kontakt aus.");
            return false;
        }
        return true;
    }
}

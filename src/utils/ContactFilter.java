package utils;

import model.Contact;
import java.util.List;
import java.util.stream.Collectors;

public class ContactFilter {

    // Methode zum Filtern von Kontakten basierend auf einem bestimmten Feld und Wert
    public List<Contact> filterContacts(List<Contact> contacts, String filterField, String filterValue) {
        // Verwenden Sie den Java 8 Stream, um durch die Kontakte zu iterieren und die filterContact-Methode auf jeden Kontakt anzuwenden
        // Sammeln Sie die gefilterten Kontakte in einer neuen Liste
        return contacts.stream()
                .filter(contact -> filterContact(contact, filterField, filterValue))
                .collect(Collectors.toList());
    }

    // Hilfsmethode zum Filtern eines einzelnen Kontakts
    private boolean filterContact(Contact contact, String filterField, String filterValue) {
        // Verwenden Sie eine switch-Anweisung, um das Feld zu bestimmen, nach dem gefiltert werden soll
        // Vergleichen Sie das Feld mit dem Filterwert (ignorieren Sie die Groß-/Kleinschreibung)
        // Geben Sie true zurück, wenn das Feld dem Filterwert entspricht, sonst false
        return switch (filterField) {
            case "Vorname" -> contact.getFirstName().equalsIgnoreCase(filterValue);
            case "Nachname" -> contact.getLastName().equalsIgnoreCase(filterValue);
            case "Stadt" -> contact.getCity().equalsIgnoreCase(filterValue);
            case "Postleitzahl" -> contact.getPostalCode().equalsIgnoreCase(filterValue);
            case "Telefonnummer" -> contact.getPhoneNumber().equalsIgnoreCase(filterValue);
            default -> false;
        };
    }
}

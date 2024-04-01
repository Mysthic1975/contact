package utils;

import model.Contact;
import java.util.List;
import java.util.stream.Collectors;

public class ContactFilter {

    public List<Contact> filterContacts(List<Contact> contacts, String filterField, String filterValue) {
        return contacts.stream()
                .filter(contact -> filterContact(contact, filterField, filterValue))
                .collect(Collectors.toList());
    }

    private boolean filterContact(Contact contact, String filterField, String filterValue) {
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

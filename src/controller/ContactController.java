package controller;

import dao.ContactDAO;
import model.Contact;
import utils.ContactFilter;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ContactController {
    // DAO-Objekt für den Zugriff auf die Datenbank
    private final ContactDAO contactDAO;
    // Filter-Objekt für die Filterung von Kontakten
    private final ContactFilter contactFilter;

    // Konstruktor, der das DAO- und Filter-Objekt initialisiert
    public ContactController(ContactDAO contactDAO) {
        this.contactDAO = contactDAO;
        this.contactFilter = new ContactFilter();
    }

    // Methode zum Hinzufügen eines Kontakts zur Datenbank
    public void addContact(Contact contact) throws SQLException {
        contactDAO.addContact(contact);
    }

    // Methode zum Aktualisieren eines Kontakts in der Datenbank
    public void updateContact(Contact contact) throws SQLException {
        contactDAO.updateContact(contact);
    }

    // Methode zum Löschen eines Kontakts aus der Datenbank
    public void deleteContact(Contact contact) throws SQLException {
        contactDAO.deleteContact(contact);
    }

    // Methode zum Abrufen eines Kontakts aus der Datenbank anhand des Vornamens und Nachnamens
    public Contact getContact(String firstName, String lastName) throws SQLException {
        return contactDAO.getContact(firstName, lastName);
    }

    // Methode zum Abrufen aller Kontakte aus der Datenbank
    public Contact[] getAllContacts() throws SQLException {
        return contactDAO.getAllContacts();
    }

    // Methode zum Abrufen von gefilterten Kontakten aus der Datenbank
    public List<Contact> getFilteredContacts(String filterField, String filterValue) throws SQLException {
        Contact[] contacts = contactDAO.getAllContacts();
        return contactFilter.filterContacts(Arrays.asList(contacts), filterField, filterValue);
    }
}
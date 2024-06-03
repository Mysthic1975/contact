package dao;

import model.Contact;

import java.sql.SQLException;

// Interface für den Datenzugriff auf Kontakte
public interface ContactDAO {
    // Methode zum Hinzufügen eines Kontakts zur Datenbank
    void addContact(Contact contact) throws SQLException;

    // Methode zum Aktualisieren eines Kontakts in der Datenbank
    void updateContact(Contact contact) throws SQLException;

    // Methode zum Löschen eines Kontakts aus der Datenbank
    void deleteContact(Contact contact) throws SQLException;

    // Methode zum Abrufen eines Kontakts aus der Datenbank anhand des Vornamens und Nachnamens
    Contact getContact(String firstName, String lastName) throws SQLException;

    // Methode zum Abrufen aller Kontakte aus der Datenbank
    Contact[] getAllContacts() throws SQLException;
}

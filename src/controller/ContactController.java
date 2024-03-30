package controller;

import dao.ContactDAO;
import model.Contact;

import java.sql.SQLException;

public class ContactController {
    private final ContactDAO contactDAO;

    public ContactController(ContactDAO contactDAO) {
        this.contactDAO = contactDAO;
    }

    public void addContact(Contact contact) throws SQLException {
        contactDAO.addContact(contact);
    }

    public void updateContact(Contact contact) throws SQLException {
        contactDAO.updateContact(contact);
    }

    public void deleteContact(Contact contact) throws SQLException {
        contactDAO.deleteContact(contact);
    }

    public Contact getContact(String firstName, String lastName) throws SQLException {
        return contactDAO.getContact(firstName, lastName);
    }

    public Contact[] getAllContacts() throws SQLException {
        return contactDAO.getAllContacts();
    }
}

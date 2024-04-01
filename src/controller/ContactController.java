package controller;

import dao.ContactDAO;
import model.Contact;
import utils.ContactFilter;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ContactController {
    private final ContactDAO contactDAO;
    private final ContactFilter contactFilter;

    public ContactController(ContactDAO contactDAO) {
        this.contactDAO = contactDAO;
        this.contactFilter = new ContactFilter();
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

    public List<Contact> getFilteredContacts(String filterField, String filterValue) throws SQLException {
        Contact[] contacts = contactDAO.getAllContacts();
        return contactFilter.filterContacts(Arrays.asList(contacts), filterField, filterValue);
    }
}

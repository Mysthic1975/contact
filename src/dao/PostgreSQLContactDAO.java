package dao;

import model.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PostgreSQLContactDAO implements ContactDAO {
    private Connection connection;
    private static final Logger LOGGER = Logger.getLogger(PostgreSQLContactDAO.class.getName());

    public PostgreSQLContactDAO() {
        try {
            // Set up connection to the database
            String url = "jdbc:postgresql://localhost:5432/contact";
            String username = "postgres";
            String password = "admin";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            LOGGER.severe(STR."Error connecting to database: \{e.getMessage()}");
        }
    }

    @Override
    public void addContact(Contact contact) throws SQLException {
        String query = "INSERT INTO contacts (first_name, last_name, street, city, postal_code, phone_number) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            executeQueryAndProcessResults(contact, statement);
            statement.executeUpdate();
        }
    }

    @Override
    public void updateContact(Contact contact) throws SQLException {
        String query = "UPDATE contacts SET first_name=?, last_name=?, street=?, city=?, postal_code=?, phone_number=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            executeQueryAndProcessResults(contact, statement);
            statement.setInt(7, contact.getId()); // Use the id to identify the contact
            int updatedRows = statement.executeUpdate();

            if (updatedRows == 0) {
                throw new SQLException("Updating contact failed, no rows affected.");
            }
        }
    }

    private void executeQueryAndProcessResults(Contact contact, PreparedStatement statement) throws SQLException {
        statement.setString(1, contact.getFirstName());
        statement.setString(2, contact.getLastName());
        statement.setString(3, contact.getStreet());
        statement.setString(4, contact.getCity());
        statement.setString(5, contact.getPostalCode());
        statement.setString(6, contact.getPhoneNumber());
    }

    @Override
    public void deleteContact(Contact contact) throws SQLException {
        String query = "DELETE FROM contacts WHERE first_name=? AND last_name=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, contact.getFirstName());
            statement.setString(2, contact.getLastName());
            statement.executeUpdate();
        }
    }

    @Override
    public Contact getContact(String firstName, String lastName) throws SQLException {
        String query = "SELECT * FROM contacts WHERE first_name=? AND last_name=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Contact(
                            resultSet.getInt("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("street"),
                            resultSet.getString("city"),
                            resultSet.getString("postal_code"),
                            resultSet.getString("phone_number")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public Contact[] getAllContacts() throws SQLException {
        List<Contact> contactList = new ArrayList<>();
        String query = "SELECT * FROM contacts";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Contact contact = new Contact(
                            resultSet.getInt("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("street"),
                            resultSet.getString("city"),
                            resultSet.getString("postal_code"),
                            resultSet.getString("phone_number")
                    );
                    contactList.add(contact);
                }
            }
        }
        return contactList.toArray(new Contact[0]);
    }
}
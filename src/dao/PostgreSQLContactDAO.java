package dao;

import model.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLContactDAO implements ContactDAO {
    private final Connection connection1;
    private final Connection connection2;

    public PostgreSQLContactDAO() throws SQLException {
        // Set up connection to the first database
        connection1 = createConnection("jdbc:postgresql://localhost:5432/postgres");

        // Set up connection to the second database (backup)
        connection2 = createConnection("jdbc:postgresql://localhost:5433/postgres");

        // Ensure the contacts table exists in both databases
        ensureTableExists(connection1);
        ensureTableExists(connection2);
    }

    private void ensureTableExists(Connection connection) throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS contacts (" +
                "id SERIAL PRIMARY KEY," +
                "first_name VARCHAR(50)," +
                "last_name VARCHAR(50)," +
                "street VARCHAR(100)," +
                "city VARCHAR(50)," +
                "postal_code VARCHAR(10)," +
                "phone_number VARCHAR(15)" +
                ")";
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
    }

    private Connection createConnection(String url) throws SQLException {
        return DriverManager.getConnection(url, "postgres", "admin");
    }

    @Override
    public void addContact(Contact contact) throws SQLException {
        String query = "INSERT INTO contacts (first_name, last_name, street, city, postal_code, phone_number) VALUES (?, ?, ?, ?, ?, ?)";
        executeQueryOnBothDatabases(query, contact);
    }

    @Override
    public void updateContact(Contact contact) throws SQLException {
        String query = "UPDATE contacts SET first_name=?, last_name=?, street=?, city=?, postal_code=?, phone_number=? WHERE id=?";
        executeQueryOnBothDatabases(query, contact);
    }

    private void executeQueryOnBothDatabases(String query, Contact contact) throws SQLException {
        try (PreparedStatement statement1 = connection1.prepareStatement(query);
             PreparedStatement statement2 = connection2.prepareStatement(query)) {
            executeQueryAndProcessResults(contact, statement1);
            executeQueryAndProcessResults(contact, statement2);
            statement1.executeUpdate();
            statement2.executeUpdate();
        }
    }

    private void executeQueryAndProcessResults(Contact contact, PreparedStatement statement) throws SQLException {
        statement.setString(1, contact.getFirstName());
        statement.setString(2, contact.getLastName());
        statement.setString(3, contact.getStreet());
        statement.setString(4, contact.getCity());
        statement.setString(5, contact.getPostalCode());
        statement.setString(6, contact.getPhoneNumber());
        if (contact.getId() != 0) {
            statement.setInt(7, contact.getId()); // Use the id to identify the contact
        }
    }

    @Override
    public void deleteContact(Contact contact) throws SQLException {
        String query = "DELETE FROM contacts WHERE first_name=? AND last_name=?";
        try (PreparedStatement statement1 = connection1.prepareStatement(query);
             PreparedStatement statement2 = connection2.prepareStatement(query)) {
            executeDeleteQuery(contact, statement1);
            executeDeleteQuery(contact, statement2);
        }
    }

    private void executeDeleteQuery(Contact contact, PreparedStatement statement) throws SQLException {
        statement.setString(1, contact.getFirstName());
        statement.setString(2, contact.getLastName());
        statement.executeUpdate();
    }

    @Override
    public Contact getContact(String firstName, String lastName) throws SQLException {
        String query = "SELECT * FROM contacts WHERE first_name=? AND last_name=?";
        try (PreparedStatement statement = connection1.prepareStatement(query)) {
            Contact resultSet = getContact(firstName, lastName, statement);
            if (resultSet != null) return resultSet;
        } catch (SQLException e) {
            // If an error occurs with the first database, try with the second one
            try (PreparedStatement statement = connection2.prepareStatement(query)) {
                Contact resultSet = getContact(firstName, lastName, statement);
                if (resultSet != null) return resultSet;
            }
        }
        return null;
    }

    private Contact getContact(String firstName, String lastName, PreparedStatement statement) throws SQLException {
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
        return null;
    }

    @Override
    public Contact[] getAllContacts() throws SQLException {
        List<Contact> contactList = new ArrayList<>();
        String query = "SELECT * FROM contacts";
        try (PreparedStatement statement = connection1.prepareStatement(query)) {
            getAllContacts(contactList, statement);
        } catch (SQLException e) {
            // If an error occurs with the first database, try with the second one
            try (PreparedStatement statement = connection2.prepareStatement(query)) {
                getAllContacts(contactList, statement);
            }
        }
        return contactList.toArray(new Contact[0]);
    }

    private void getAllContacts(List<Contact> contactList, PreparedStatement statement) throws SQLException {
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
}
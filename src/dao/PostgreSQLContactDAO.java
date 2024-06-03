package dao;

import model.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLContactDAO implements ContactDAO {
    // Verbindungen zu den beiden Datenbanken
    private final Connection connection1;
    private final Connection connection2;

    public PostgreSQLContactDAO() throws SQLException {
        // Verbindung zur ersten Datenbank herstellen
        connection1 = createConnection("jdbc:postgresql://localhost:5433/postgres");

        // Verbindung zur zweiten Datenbank (Backup) herstellen
        connection2 = createConnection("jdbc:postgresql://localhost:5434/postgres");

        // Stellen Sie sicher, dass die Kontakte-Tabelle in beiden Datenbanken existiert
        ensureTableExists(connection1);
        ensureTableExists(connection2);
    }

    // Methode zum Erstellen der Kontakte-Tabelle, falls sie nicht existiert
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

    // Methode zum Herstellen einer Verbindung zur Datenbank
    private Connection createConnection(String url) throws SQLException {
        return DriverManager.getConnection(url, "postgres", "admin");
    }

    // Implementierung der addContact-Methode aus dem ContactDAO-Interface
    @Override
    public void addContact(Contact contact) throws SQLException {
        String query = "INSERT INTO contacts (first_name, last_name, street, city, postal_code, phone_number) VALUES (?, ?, ?, ?, ?, ?)";
        executeQueryOnBothDatabases(query, contact);
    }

    // Implementierung der updateContact-Methode aus dem ContactDAO-Interface
    @Override
    public void updateContact(Contact contact) throws SQLException {
        String query = "UPDATE contacts SET first_name=?, last_name=?, street=?, city=?, postal_code=?, phone_number=? WHERE id=?";
        executeQueryOnBothDatabases(query, contact);
    }

    // Methode zum Ausführen einer Abfrage auf beiden Datenbanken
    private void executeQueryOnBothDatabases(String query, Contact contact) throws SQLException {
        try (PreparedStatement statement1 = connection1.prepareStatement(query);
             PreparedStatement statement2 = connection2.prepareStatement(query)) {
            executeQueryAndProcessResults(contact, statement1);
            executeQueryAndProcessResults(contact, statement2);
            statement1.executeUpdate();
            statement2.executeUpdate();
        }
    }

    // Methode zum Ausführen einer Abfrage und Verarbeiten der Ergebnisse
    private void executeQueryAndProcessResults(Contact contact, PreparedStatement statement) throws SQLException {
        statement.setString(1, contact.getFirstName());
        statement.setString(2, contact.getLastName());
        statement.setString(3, contact.getStreet());
        statement.setString(4, contact.getCity());
        statement.setString(5, contact.getPostalCode());
        statement.setString(6, contact.getPhoneNumber());
        if (contact.getId() != 0) {
            statement.setInt(7, contact.getId()); // Verwenden Sie die ID, um den Kontakt zu identifizieren
        }
    }

    // Implementierung der deleteContact-Methode aus dem ContactDAO-Interface
    @Override
    public void deleteContact(Contact contact) throws SQLException {
        String query = "DELETE FROM contacts WHERE first_name=? AND last_name=?";
        try (PreparedStatement statement1 = connection1.prepareStatement(query);
             PreparedStatement statement2 = connection2.prepareStatement(query)) {
            executeDeleteQuery(contact, statement1);
            executeDeleteQuery(contact, statement2);
        }
    }

    // Methode zum Ausführen einer Löschabfrage
    private void executeDeleteQuery(Contact contact, PreparedStatement statement) throws SQLException {
        statement.setString(1, contact.getFirstName());
        statement.setString(2, contact.getLastName());
        statement.executeUpdate();
    }

    // Implementierung der getContact-Methode aus dem ContactDAO-Interface
    @Override
    public Contact getContact(String firstName, String lastName) throws SQLException {
        String query = "SELECT * FROM contacts WHERE first_name=? AND last_name=?";
        try (PreparedStatement statement = connection1.prepareStatement(query)) {
            Contact resultSet = getContact(firstName, lastName, statement);
            if (resultSet != null) return resultSet;
        } catch (SQLException e) {
            // Wenn ein Fehler mit der ersten Datenbank auftritt, versuchen Sie es mit der zweiten
            try (PreparedStatement statement = connection2.prepareStatement(query)) {
                Contact resultSet = getContact(firstName, lastName, statement);
                if (resultSet != null) return resultSet;
            }
        }
        return null;
    }

    // Methode zum Abrufen eines Kontakts
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

    // Implementierung der getAllContacts-Methode aus dem ContactDAO-Interface
    @Override
    public Contact[] getAllContacts() throws SQLException {
        List<Contact> contactList = new ArrayList<>();
        String query = "SELECT * FROM contacts";
        try (PreparedStatement statement = connection1.prepareStatement(query)) {
            getAllContacts(contactList, statement);
        } catch (SQLException e) {
            // Wenn ein Fehler mit der ersten Datenbank auftritt, versuchen Sie es mit der zweiten
            try (PreparedStatement statement = connection2.prepareStatement(query)) {
                getAllContacts(contactList, statement);
            }
        }
        return contactList.toArray(new Contact[0]);
    }

    // Methode zum Abrufen aller Kontakte
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
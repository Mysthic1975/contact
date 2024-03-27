import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class PostgreSQLContactDAO implements ContactDAO {
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
            statement.setString(1, contact.firstName);
            statement.setString(2, contact.lastName);
            statement.setString(3, contact.street);
            statement.setString(4, contact.city);
            statement.setString(5, contact.postalCode);
            statement.setString(6, contact.phoneNumber);
            statement.executeUpdate();
        }
    }

    @Override
    public void updateContact(Contact contact) throws SQLException {
        String query = "UPDATE contacts SET street=?, city=?, postal_code=?, phone_number=? WHERE first_name=? AND last_name=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, contact.street);
            statement.setString(2, contact.city);
            statement.setString(3, contact.postalCode);
            statement.setString(4, contact.phoneNumber);
            statement.setString(5, contact.firstName);
            statement.setString(6, contact.lastName);
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteContact(Contact contact) throws SQLException {
        String query = "DELETE FROM contacts WHERE first_name=? AND last_name=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, contact.firstName);
            statement.setString(2, contact.lastName);
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
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("street"),
                            resultSet.getString("city"),
                            resultSet.getString("postal_code"),
                            resultSet.getString("phone_number")
                    ) {
                    };
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
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("street"),
                            resultSet.getString("city"),
                            resultSet.getString("postal_code"),
                            resultSet.getString("phone_number")
                    ) {
                    };
                    contactList.add(contact);
                }
            }
        }
        return contactList.toArray(new Contact[0]);
    }
}


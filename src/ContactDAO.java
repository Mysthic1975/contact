import java.sql.SQLException;

public interface ContactDAO {
    void addContact(Contact contact) throws SQLException;
    void updateContact(Contact contact) throws SQLException;
    void deleteContact(Contact contact) throws SQLException;
    Contact getContact(String firstName, String lastName) throws SQLException;

    Contact[] getAllContacts() throws SQLException;
}

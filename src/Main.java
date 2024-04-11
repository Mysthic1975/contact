import controller.ContactController;
import dao.ContactDAO;
import dao.PostgreSQLContactDAO;
import view.ContactGUI;

import javax.swing.*;
import java.sql.SQLException;

public static void main() {
    try {
        ContactDAO contactDAO = new PostgreSQLContactDAO();
        ContactController contactController = new ContactController(contactDAO);
        SwingUtilities.invokeLater(() -> new ContactGUI(contactController));
    } catch (SQLException e) {
        System.err.printf("Error connecting to database: %s%n", e.getMessage());
        System.exit(1);
    }
}


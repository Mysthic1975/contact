import controller.ContactController;
import dao.ContactDAO;
import dao.PostgreSQLContactDAO;
import view.ContactGUI;

import javax.swing.*;
import java.sql.SQLException;

public static void main() {
    try {
        // Erstellen eines neuen PostgreSQLContactDAO-Objekts, das die ContactDAO-Schnittstelle implementiert
        ContactDAO contactDAO = new PostgreSQLContactDAO();

        // Erstellen eines neuen ContactController-Objekts, das das ContactDAO-Objekt verwendet
        ContactController contactController = new ContactController(contactDAO);

        // Erstellen eines neuen ContactGUI-Objekts auf dem Event-Dispatching-Thread
        // dies stellt sicher, dass alle Benutzeroberflächenaktionen auf dem Event-Dispatching-Thread ausgeführt werden
        SwingUtilities.invokeLater(() -> new ContactGUI(contactController));
    } catch (SQLException e) {
        // Fehlerbehandlung für den Fall, dass eine SQLException auftritt
        System.err.printf("Error connecting to database: %s%n", e.getMessage());
        System.exit(1);
    }
}


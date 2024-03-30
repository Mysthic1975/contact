import controller.ContactController;
import dao.ContactDAO;
import dao.PostgreSQLContactDAO;
import view.ContactGUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        ContactDAO contactDAO = new PostgreSQLContactDAO();
        ContactController contactController = new ContactController(contactDAO);
        SwingUtilities.invokeLater(() -> new ContactGUI(contactController));
    }
}


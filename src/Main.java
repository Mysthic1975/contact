import dao.ContactDAO;
import dao.PostgreSQLContactDAO;
import view.ContactGUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        ContactDAO contactDAO = new PostgreSQLContactDAO();
        SwingUtilities.invokeLater(() -> new ContactGUI(contactDAO));
    }
}


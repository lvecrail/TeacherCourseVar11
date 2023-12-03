package VSUET.teachcours;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdministrationDatabase.createTables();
            SchoolSystemGUI systemGUI = new SchoolSystemGUI();
            systemGUI.showMainWindow();
        });
    }
}

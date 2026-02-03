package main;

import javax.swing.SwingUtilities;
import gui.FoodDeliveryApp;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FoodDeliveryApp().setVisible(true));
    }
}
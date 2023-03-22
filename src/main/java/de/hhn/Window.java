package de.hhn;

import javax.swing.*;

public class Window extends JFrame {
    public Window() {
        super("Schach");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setLocationRelativeTo(null);

        setLayout(null);
        setResizable(false);

    }
}

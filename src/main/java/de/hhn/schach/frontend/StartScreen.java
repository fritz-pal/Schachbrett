package de.hhn.schach.frontend;

import de.hhn.schach.Game;

import javax.swing.*;
import java.awt.*;

public class StartScreen extends JFrame {
    private int checkboxNum = 0;

    public StartScreen() {
        super();
        this.setTitle("Schach");
        this.setLayout(null);
        this.getContentPane().setBackground(new Color(0x312e2b));
        this.setSize(500, 650);
        this.setPreferredSize(new Dimension(500, 650));
        this.pack();
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon(ImagePath.getPath("icon.png")).getImage());

        JLabel titleLabel = new JLabel("Schach");
        titleLabel.setBounds(200, 10, 100, 50);
        titleLabel.setFont(new Font("Arial Black", Font.PLAIN, 24));
        titleLabel.setForeground(Color.WHITE);
        this.getContentPane().add(titleLabel, 0);

        JCheckBox rotatedPieces = makeCheckbox("Rotated pieces");
        JCheckBox rotatedBoard = makeCheckbox("Rotated board");

        JTextField fenInput = new JTextField("Custom Position (Fen)");

        JButton startButton = new JButton("Start Game");
        startButton.setBounds(250, 525, 200, 50);
        startButton.setFont(new Font("Arial Black", Font.PLAIN, 14));
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(new Color(0x514e4b));
        startButton.setFocusable(false);
        startButton.addActionListener(e -> new Game(rotatedPieces.isSelected(), rotatedBoard.isSelected(), fenInput.getText()));
        this.getContentPane().add(startButton, 0);

        this.setVisible(true);
    }

    public JCheckBox makeCheckbox(String name) {
        JCheckBox checkbox = new JCheckBox(name);
        checkbox.setBounds(50, 300 + checkboxNum * 50, 200, 50);
        checkbox.setFont(new Font("Arial Black", Font.PLAIN, 14));
        checkbox.setForeground(Color.WHITE);
        checkbox.setFocusable(false);
        checkbox.setBackground(new Color(0x312e2b));
        this.getContentPane().add(checkbox);
        checkboxNum++;
        return checkbox;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(new ImageIcon(ImagePath.getPath("thumbnail.png")).getImage(), 100, 100, 300, 225, null);
    }
}

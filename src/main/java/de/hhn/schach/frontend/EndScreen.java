package de.hhn.schach.frontend;

import de.hhn.schach.Game;
import de.hhn.schach.utils.Result;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EndScreen extends JFrame {

    public EndScreen(Result result, String pgn, String fen, Game game) {
        super();
        this.setTitle(result.toString());
        this.setLayout(null);
        this.getContentPane().setBackground(new Color(0x312e2b));
        this.setSize(416, 589);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(game.getWindow());
        this.setIconImage(new ImageIcon(ImagePath.getResource("icon.png")).getImage());

        JLabel titleLabel = new JLabel((result.toString()));
        titleLabel.setBounds(0, 0, 400, 100);
        titleLabel.setFont(new Font("Arial Black", Font.PLAIN, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        this.add(titleLabel);

        JLabel fenLabel = new JLabel("FEN");
        fenLabel.setBounds(50, 100, 50, 25);
        fenLabel.setFont(new Font("Arial Black", Font.PLAIN, 14));
        fenLabel.setForeground(Color.WHITE);
        this.getContentPane().add(fenLabel, 0);

        JTextField fenField = new JTextField(fen);
        fenField.setBounds(50, 125, 300, 25);
        fenField.setBackground(new Color(0x514e4b));
        fenField.setForeground(Color.WHITE);
        fenField.setFont(new Font("Arial", Font.PLAIN, 14));
        selectAllListener(fenField);
        this.add(fenField);

        JLabel pgnLabel = new JLabel("PGN");
        pgnLabel.setBounds(50, 175, 50, 25);
        pgnLabel.setFont(new Font("Arial Black", Font.PLAIN, 14));
        pgnLabel.setForeground(Color.WHITE);
        this.getContentPane().add(pgnLabel, 0);

        JTextArea pgnField = new JTextArea(pgn);
        pgnField.setBounds(50, 200, 300, 300);
        pgnField.setBackground(new Color(0x514e4b));
        pgnField.setForeground(Color.WHITE);
        pgnField.setFont(new Font("Arial", Font.PLAIN, 14));
        pgnField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        pgnField.setLineWrap(true);
        pgnField.setWrapStyleWord(true);
        selectAllListener(pgnField);
        this.add(pgnField);

        this.setVisible(true);
    }

    private void selectAllListener(JTextComponent component) {
        component.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                component.selectAll();
            }
        });
    }
}
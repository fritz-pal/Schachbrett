package de.hhn.schach.frontend;

import de.hhn.schach.Result;

import javax.swing.*;
import java.awt.*;

public class EndScreen extends JFrame {

    public EndScreen(Result result, String pgn, String fen, Window window) {
        super();
        this.setTitle(result.toString());
        this.setLayout(null);
        this.getContentPane().setBackground(new Color(0x312e2b));
        this.setSize(400, 300);
        this.setPreferredSize(new Dimension(400, 600));
        this.pack();
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(window);
        this.setIconImage(new ImageIcon(ImagePath.getResource("icon.png")).getImage());

        JLabel titleLabel = new JLabel((result.toString()));
        titleLabel.setBounds(0, 0, 400, 100);
        titleLabel.setFont(new Font("Arial Black", Font.PLAIN, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        this.add(titleLabel);

        JTextField fenField = new JTextField(fen);
        fenField.setBounds(50, 150, 300, 25);
        fenField.setBackground(new Color(0x514e4b));
        fenField.setForeground(Color.WHITE);
        fenField.setFont(new Font("Arial", Font.PLAIN, 14));
        this.add(fenField);

        JTextArea pgnField = new JTextArea(pgn);
        pgnField.setBounds(50, 200, 300, 300);
        pgnField.setBackground(new Color(0x514e4b));
        pgnField.setForeground(Color.WHITE);
        pgnField.setFont(new Font("Arial", Font.PLAIN, 14));
        pgnField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        pgnField.setLineWrap(true);
        this.add(pgnField);


        this.setVisible(true);
    }
}

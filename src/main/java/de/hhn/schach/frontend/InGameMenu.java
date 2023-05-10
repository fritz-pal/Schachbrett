package de.hhn.schach.frontend;

import de.hhn.schach.Board;
import de.hhn.schach.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InGameMenu extends JFrame {

    public InGameMenu(Game game) {
        this.setTitle("Menu");
        this.setSize(400, 400);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(null);
        this.setLocationRelativeTo(game.getWindow().getContentPane());
        this.setResizable(false);
        this.setIconImage(new ImageIcon(ImagePath.getResource("icon.png")).getImage());
        this.getContentPane().setBackground(new Color(0x312e2b));
        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                InGameMenu.this.dispose();
            }
        });

        Board board = game.getMainBoard();

        JButton resignButton = new JButton("Resign");
        resignButton.setBounds(50, 40, 300, 50);
        resignButton.setFont(new Font("Arial Black", Font.PLAIN, 24));
        resignButton.setBackground(new Color(0x514e4b));
        resignButton.setForeground(Color.WHITE);
        resignButton.setFocusable(false);
        resignButton.addActionListener(e -> board.resign());
        this.add(resignButton);

        JButton drawButton = new JButton((board.hasOfferedDraw(!board.isWhiteTurn()) ? "Accept" : "Offer") + " Draw");
        if (board.hasOfferedDraw(board.isWhiteTurn())) drawButton.setText("Draw Offered");
        drawButton.setBounds(50, 130, 300, 50);
        drawButton.setFont(new Font("Arial Black", Font.PLAIN, 24));
        drawButton.setBackground(new Color(0x514e4b));
        drawButton.setForeground(Color.WHITE);
        drawButton.setFocusable(false);
        drawButton.addActionListener(e -> {
            board.offerDraw();
            if (drawButton.getText().equals("Offer Draw")) drawButton.setText("Draw Offered");
        });
        this.add(drawButton);

        JButton infoButton = new JButton("Info");
        infoButton.setBounds(50, 220, 300, 50);
        infoButton.setFont(new Font("Arial Black", Font.PLAIN, 24));
        infoButton.setBackground(new Color(0x514e4b));
        infoButton.setForeground(Color.WHITE);
        infoButton.setFocusable(false);
        infoButton.addActionListener(e -> game.endScreen());
        this.add(infoButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(50, 310, 300, 50);
        exitButton.setFont(new Font("Arial Black", Font.PLAIN, 24));
        exitButton.setBackground(new Color(0x514e4b));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusable(false);
        exitButton.addActionListener(e -> {
            game.stop();
            this.dispose();
        });
        this.add(exitButton);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 32) {
                    InGameMenu.this.dispose();
                }
            }
        });

        this.setVisible(true);
    }
}

package de.hhn;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    public Window() {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setSize(816, 839);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setTitle("Schach");
        this.setResizable(false);
        this.setIconImage(new ImageIcon("pawn_white+.svg").getImage());
        this.getContentPane().setBackground(new Color(0xedd6b0));


        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Tile tile = new Tile(x*100, y*100, 100, (x + y) % 2 == 0);
                this.add(tile);
                System.out.println(this.getWidth() + " " + this.getHeight());
            }
        }
    }
}

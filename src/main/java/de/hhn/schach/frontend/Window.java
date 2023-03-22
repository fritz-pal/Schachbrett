package de.hhn.schach.frontend;

import de.hhn.schach.Board;
import de.hhn.schach.Vec2;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    public Window() {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(816, 839);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setTitle("Schach");
        this.setResizable(false);
        this.setIconImage(new ImageIcon(ImagePath.getPath("icon.png")).getImage());
        this.getContentPane().setBackground(new Color(0xedd6b0));

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Vec2 pos = new Vec2(x, y);
                Tile button = new Tile(pos,this);
            }
        }

    }

    public void update(Board board) {

        this.repaint();
    }
}

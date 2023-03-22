package de.hhn.schach.frontend;

import de.hhn.schach.Board;
import de.hhn.schach.Vec2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Window extends JFrame {
    List<Tile> tiles = new ArrayList<>();

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

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Vec2 pos = new Vec2(x, y);
                Tile tile = new Tile(pos, this);
                this.tiles.add(tile);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (Tile tile : tiles) {
            tile.paint(g);
        }
    }

    public void update(Board board) {
        for (Tile tile : tiles) {
            Vec2 pos = tile.getPos();
            tile.setPiece(board.getPiece(pos));
            System.out.println("Updated " + pos + " to " + board.getPiece(pos));
        }
        this.repaint();
    }
}

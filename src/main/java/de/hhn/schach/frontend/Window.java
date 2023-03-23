package de.hhn.schach.frontend;

import de.hhn.schach.Board;
import de.hhn.schach.Game;
import de.hhn.schach.Vec2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Window extends JFrame {
    public final Game game;
    private final boolean rotatedPieces;
    private final boolean rotatedBoard;
    List<Tile> tiles = new ArrayList<>();

    public Window(Game game, boolean rotatedPieces, boolean rotatedBoard) {
        super();
        this.game = game;
        this.rotatedBoard = rotatedBoard;
        this.rotatedPieces = rotatedPieces;

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(800, 800);
        this.getContentPane().setPreferredSize(new Dimension(800, 800));
        this.pack();
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setMinimumSize(new Dimension(416, 439));
        this.setTitle("Schach");
        this.setIconImage(new ImageIcon(ImagePath.getPath("icon.png")).getImage());
        this.getContentPane().setBackground(new Color(0x312e2b));
        this.setResizable(true);

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
        System.out.println("Width: " + this.getContentPane().getWidth() + ", Height: " + this.getContentPane().getHeight());
    }

    public void update(Board board) {
        for (Tile tile : tiles) {
            Vec2 pos = tile.getPos();
            tile.setPiece(board.getPiece(pos));

            System.out.println("Updated " + pos + " to " + board.getPiece(pos));
        }
        this.repaint();
    }

    public boolean isRotatedPieces() {
        return rotatedPieces;
    }

    public boolean isRotatedBoard() {
        return rotatedBoard;
    }
}

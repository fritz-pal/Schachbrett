package de.hhn.schach.frontend;

import de.hhn.schach.Board;
import de.hhn.schach.Game;
import de.hhn.schach.utils.Vec2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class Window extends JFrame {
    private final Game game;
    private final List<Tile> tiles = new ArrayList<>();
    JPanel boardPanel = new JPanel();


    public Window(Game game) {
        super();
        this.game = game;
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setSize(816, 839);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setMinimumSize(new Dimension(416, 439));
        this.setTitle("Schach");
        this.setIconImage(new ImageIcon(ImagePath.getResource("icon.png")).getImage());
        this.getContentPane().setBackground(new Color(0x312e2b));
        this.setResizable(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                game.stop();
            }
        });

        boardPanel.setBounds(0, 0, 800, 800);
        boardPanel.setLayout(new GridLayout(8, 8));
        this.getContentPane().add(boardPanel);

        for (int x = 7; x >= 0; x--) {
            for (int y = 0; y < 8; y++) {
                Vec2 pos = new Vec2(x, y);
                Tile tile = new Tile(game, pos, this);
                this.tiles.add(tile);
                boardPanel.add(tile);
            }
        }
    }

    public void update(Board board) {
        List<Vec2> legalMoves = board.getAllLegalMoves(game.getSelectedTile());

        Vec2 checkPos = null;
        if (board.isInCheck(true)) checkPos = board.getKingPos(true);
        else if (board.isInCheck(false)) checkPos = board.getKingPos(false);
        boolean checkmate = board.isCheckmate();

        for (Tile tile : tiles) {
            Vec2 pos = tile.getPos();
            tile.update(board.getPiece(pos), legalMoves.contains(pos), pos.equals(checkPos), checkmate && pos.equals(checkPos));
        }
    }

    public Tile getTile(Vec2 pos) {
        for (Tile tile : tiles) {
            if (tile.getPos().equals(pos)) return tile;
        }
        throw new IllegalArgumentException("Tile not found");
    }
}
package de.hhn.schach.frontend;

import de.hhn.schach.Board;
import de.hhn.schach.Game;
import de.hhn.schach.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Window extends JFrame {
    private final Game game;
    private final List<Tile> tiles = new ArrayList<>();
    private final boolean maximized;
    private final JPanel boardPanel = new JPanel();
    private int goBack = 0;
    private long lastKeyPress = 0;

    public Window(Game game, boolean maximized) {
        super();
        this.game = game;
        this.maximized = maximized;
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setSize(800, 800);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.getContentPane().setMinimumSize(new Dimension(400, 400));
        this.setTitle("Chess");
        this.setIconImage(new ImageIcon(ImagePath.getResource("icon.png")).getImage());
        this.getContentPane().setBackground(new Color(0x312e2b));
        this.setResizable(true);
        this.addWindowListener(windowListener());
        this.addWindowStateListener(windowStateListener());
        this.setUndecorated(maximized);
        this.setExtendedState(maximized ? JFrame.MAXIMIZED_BOTH : JFrame.NORMAL);
        this.getContentPane().addKeyListener(keyListener());
        this.getContentPane().setFocusable(true);
        this.getContentPane().requestFocusInWindow();
        this.getContentPane().setPreferredSize(new Dimension(800, 800));
        this.pack();

        for (int x = 7; x >= 0; x--) {
            for (int y = 0; y < 8; y++) {
                Vec2 pos = new Vec2(x, y);
                Tile tile = new Tile(game, pos, this);
                this.tiles.add(tile);
                boardPanel.add(tile);
            }
        }

        if (!maximized)
            boardPanel.setBounds((this.getContentPane().getWidth() - getBoardSize()) / 2, (this.getContentPane().getHeight() - getBoardSize()) / 2, getBoardSize(), getBoardSize());
        else
            boardPanel.setBounds((Toolkit.getDefaultToolkit().getScreenSize().width - getBoardSize()) / 2, 0, getBoardSize(), getBoardSize());

        boardPanel.setLayout(new GridLayout(8, 8));
        this.getContentPane().add(boardPanel);

        this.setVisible(true);
    }

    public void update() {
        goBack = 0;
        Board board = game.getMainBoard();
        List<Vec2> legalMoves = board.getAllLegalMoves(game.getSelectedTile());

        Vec2 checkPos = null;
        if (board.isWhiteTurn() && board.isInCheck(true)) checkPos = board.getKingPos(true);
        else if (!board.isWhiteTurn() && board.isInCheck(false)) checkPos = board.getKingPos(false);

        Vec2 whiteKingPos = null;
        Vec2 blackKingPos = null;
        TileIcon whiteIcon = null;
        TileIcon blackIcon = null;
        if (board.getResult() != Result.NOTFINISHED) {
            whiteKingPos = board.getKingPos(true);
            blackKingPos = board.getKingPos(false);
            whiteIcon = board.getResult().whiteWon() ? TileIcon.WIN : board.getResult().isDraw() ? TileIcon.DRAW : TileIcon.CHECKMATE;
            blackIcon = board.getResult().blackWon() ? TileIcon.WIN : board.getResult().isDraw() ? TileIcon.DRAW : TileIcon.CHECKMATE;
        }

        for (Tile tile : tiles) {
            Vec2 pos = tile.getPos();
            if (pos.equals(whiteKingPos))
                tile.update(board.getPiece(pos), legalMoves.contains(pos), pos.equals(checkPos), whiteIcon);
            else if (pos.equals(blackKingPos))
                tile.update(board.getPiece(pos), legalMoves.contains(pos), pos.equals(checkPos), blackIcon);
            else
                tile.update(board.getPiece(pos), legalMoves.contains(pos), pos.equals(checkPos), null);
        }
    }

    public void displayFen(String fen) {
        for (Tile tile : tiles) tile.update(null, false, false, null);
        String[] fenParts = fen.split(" ");
        String[] fenRows = fenParts[0].split("/");
        for (int x = 0; x < 8; x++) {
            String fenRow = fenRows[x];
            int y = 0;
            for (char c : fenRow.toCharArray()) {
                if (Character.isDigit(c)) {
                    y += Integer.parseInt(String.valueOf(c));
                } else {
                    Vec2 pos = new Vec2(x, y);
                    Tile tile = getTile(pos);
                    tile.update(new Piece(PieceType.fromNotation(c), Character.isUpperCase(c)), false, false, null);
                    y++;
                }
            }
        }
    }

    public Tile getTile(Vec2 pos) {
        for (Tile tile : tiles) {
            if (tile.getPos().equals(pos)) return tile;
        }
        throw new IllegalArgumentException("Tile not found");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (!maximized)
            boardPanel.setBounds((this.getContentPane().getWidth() - getBoardSize()) / 2, (this.getContentPane().getHeight() - getBoardSize()) / 2, getBoardSize(), getBoardSize());
        update();
    }

    private int getBoardSize() {
        return !maximized ? Math.min(this.getContentPane().getWidth(), this.getContentPane().getHeight()) : Toolkit.getDefaultToolkit().getScreenSize().height;
    }

    private KeyAdapter keyListener() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (System.currentTimeMillis() - lastKeyPress < 50) return;
                switch (e.getKeyCode()) {
                    case 32 -> {
                        new InGameMenu(game);
                    }
                    case 37 -> {
                        List<Move> moves = game.getMainBoard().getMoveHistory();
                        if (moves.size() - goBack > 0) {
                            goBack++;
                            Move move = moves.get(moves.size() - goBack);
                            displayFen(move.fen());
                            Sound.moveSound(move);
                        }
                    }
                    case 39 -> {
                        List<Move> moves = game.getMainBoard().getMoveHistory();
                        if (goBack > 1) {
                            goBack--;
                            Move move = moves.get(moves.size() - goBack);
                            displayFen(move.fen());
                            Sound.moveSound(moves.get(moves.size() - goBack - 1));
                        } else if (goBack == 1) {
                            goBack--;
                            Sound.moveSound(moves.get(moves.size() - 1));
                            update();
                        }
                    }
                    case 122 -> maximize();
                    case 38 -> {
                        if (goBack > 0) update();
                    }
                    case 40 -> {
                        List<Move> moves = game.getMainBoard().getMoveHistory();
                        goBack = moves.size();
                        if (moves.size() > 0) {
                            displayFen(game.getMainBoard().getMoveHistory().get(0).fen());
                        }
                    }
                    case 27 -> {
                        game.setSelectedTile(null);
                        update();
                    }
                }
                lastKeyPress = System.currentTimeMillis();
            }
        };
    }

    private WindowStateListener windowStateListener() {
        return windowEvent -> {
            if (windowEvent.getNewState() == JFrame.MAXIMIZED_BOTH) maximize();
        };
    }

    private WindowAdapter windowListener() {
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                game.stop();
            }
        };
    }

    private void maximize() {
        Window window = new Window(game, !maximized);
        game.setWindow(window);
        window.update();
        Window.this.dispose();
    }
}
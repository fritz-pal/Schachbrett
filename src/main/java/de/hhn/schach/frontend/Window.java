package de.hhn.schach.frontend;

import de.hhn.schach.Board;
import de.hhn.schach.Game;
import de.hhn.schach.utils.Move;
import de.hhn.schach.utils.Piece;
import de.hhn.schach.utils.PieceType;
import de.hhn.schach.utils.Result;
import de.hhn.schach.utils.TileIcon;
import de.hhn.schach.utils.Vec2;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame {
  private final Game game;
  private final List<Tile> tiles = new ArrayList<>();
  private final boolean maximized;
  private final JPanel boardPanel = new JPanel();
  private EvalBar evalBar = null;
  private int goBack = 0;
  private long lastKeyPress = 0;

  public Window(Game game, boolean maximized) {
    super();
    this.game = game;
    this.maximized = maximized;
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setSize(game.isWithEval() ? 920 : 800, 800);
    this.setLocationRelativeTo(null);
    this.setLayout(null);
    this.setMinimumSize(new Dimension(416, 439));
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
    this.getContentPane().setPreferredSize(new Dimension(game.isWithEval() ? 920 : 800, 800));
    this.pack();

    if (game.isRotatedBoard()) {
      for (int x = 0; x < 8; x++) {
        for (int y = 7; y >= 0; y--) {
          Vec2 pos = new Vec2(x, y);
          Tile tile = new Tile(game, pos, this);
          this.tiles.add(tile);
          boardPanel.add(tile);
        }
      }
    } else {
      for (int x = 7; x >= 0; x--) {
        for (int y = 0; y < 8; y++) {
          Vec2 pos = new Vec2(x, y);
          Tile tile = new Tile(game, pos, this);
          this.tiles.add(tile);
          boardPanel.add(tile);
        }
      }
    }
      if (game.isWithEval()) {
          evalBar = new EvalBar(game, this);
      }

    if (!maximized) {
      boardPanel.setBounds((this.getContentPane().getWidth() - getBoardSize()) / 2,
          (this.getContentPane().getHeight() - getBoardSize()) / 2, getBoardSize(), getBoardSize());
        if (game.isWithEval()) {
            evalBar.setBounds((this.getContentPane().getWidth() - getBoardSize()) / 2 - 50,
                (this.getContentPane().getHeight() - getBoardSize()) / 2, 40, getBoardSize());
        }
    } else {
      boardPanel.setBounds((Toolkit.getDefaultToolkit().getScreenSize().width - getBoardSize()) / 2,
          0, getBoardSize(), getBoardSize());
        if (game.isWithEval()) {
            evalBar.setBounds(
                (Toolkit.getDefaultToolkit().getScreenSize().width - getBoardSize()) / 2 - 50, 0,
                40,
                getBoardSize());
        }
    }
    boardPanel.setLayout(new GridLayout(8, 8));
    this.getContentPane().add(boardPanel);
    if (game.isWithEval()) {
      this.getContentPane().add(evalBar);
        if (game.getUci() != null) {
            evalBar.setEval(game.getUci().getCp(), game.getUci().getMate());
        }
    }


    this.setVisible(true);
  }

  public void update() {
    goBack = 0;
    Board board = game.getMainBoard();
    List<Vec2> legalMoves = board.getAllLegalMoves(game.getSelectedTile());

    Vec2 checkPos = null;
      if (board.isWhiteTurn() && board.isInCheck(true)) {
          checkPos = board.getKingPos(true);
      } else if (!board.isWhiteTurn() && board.isInCheck(false)) {
          checkPos = board.getKingPos(false);
      }

    Vec2 whiteKingPos = null;
    Vec2 blackKingPos = null;
    TileIcon whiteIcon = null;
    TileIcon blackIcon = null;
    if (board.getResult() != Result.NOTFINISHED) {
      whiteKingPos = board.getKingPos(true);
      blackKingPos = board.getKingPos(false);
      whiteIcon = board.getResult().whiteWon() ? TileIcon.WIN :
          board.getResult().isDraw() ? TileIcon.DRAW : TileIcon.CHECKMATE;
      blackIcon = board.getResult().blackWon() ? TileIcon.WIN :
          board.getResult().isDraw() ? TileIcon.DRAW : TileIcon.CHECKMATE;
    }

    Move lastMove = board.getLastMove();

    for (Tile tile : tiles) {
      Vec2 pos = tile.getPos();
      boolean selected =
          lastMove != null && (lastMove.from().equals(pos) || lastMove.to().equals(pos)) ||
              pos.equals(game.getSelectedTile());

        if (pos.equals(whiteKingPos)) {
            tile.update(board.getPiece(pos), legalMoves.contains(pos), pos.equals(checkPos),
                whiteIcon,
                selected);
        } else if (pos.equals(blackKingPos)) {
            tile.update(board.getPiece(pos), legalMoves.contains(pos), pos.equals(checkPos),
                blackIcon,
                selected);
        } else {
            tile.update(board.getPiece(pos), legalMoves.contains(pos), pos.equals(checkPos), null,
                selected);
        }
    }
  }

  public void displayFen(String fen, Move move) {
      for (Tile tile : tiles) {
          tile.update(null, false, false, null,
              move != null &&
                  (move.to().equals(tile.getPos()) || move.from().equals(tile.getPos())));
      }
    String[] fenParts = fen.split(" ");
    String[] fenRows = fenParts[0].split("/");
    for (int x = 0; x < 8; x++) {
      String fenRow = fenRows[x];
      int y = 0;
      for (char c : fenRow.toCharArray()) {
        if (Character.isDigit(c)) {
          y += Integer.parseInt(String.valueOf(c));
        } else {
          Vec2 pos = new Vec2(7 - x, y);
          Tile tile = getTile(pos);
          tile.update(new Piece(PieceType.fromNotation(c), Character.isUpperCase(c)), false, false,
              null, move != null && (move.to().equals(pos) || move.from().equals(pos)));
          y++;
        }
      }
    }
  }

  public Tile getTile(Vec2 pos) {
    for (Tile tile : tiles) {
        if (tile.getPos().equals(pos)) {
            return tile;
        }
    }
    throw new IllegalArgumentException("Tile not found");
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    if (!maximized) {
      boardPanel.setBounds((this.getContentPane().getWidth() - getBoardSize()) / 2,
          (this.getContentPane().getHeight() - getBoardSize()) / 2, getBoardSize(), getBoardSize());
      if (game.isWithEval()) {
        evalBar.getWhiteLabel().setBounds(0, evalBar.getHeight() - 40, 40, 40);
        evalBar.setBounds((this.getContentPane().getWidth() - getBoardSize()) / 2 - 50,
            (this.getContentPane().getHeight() - getBoardSize()) / 2, 40, getBoardSize());
          if (game.getUci() != null) {
              evalBar.setEval(game.getUci().getCp(), game.getUci().getMate());
          }
      }
    }
    update();
  }

  public void receivedEval(int cp, int mate) {
      if (game.isWithEval()) {
          evalBar.setEval(cp, mate);
      }
  }

  public int getBoardSize() {
    return !maximized ?
        Math.min(this.getContentPane().getWidth(), this.getContentPane().getHeight()) :
        Toolkit.getDefaultToolkit().getScreenSize().height;
  }

  private KeyAdapter keyListener() {
    return new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
          if (System.currentTimeMillis() - lastKeyPress < 20) {
              return;
          }
        switch (e.getKeyCode()) {
          case 32 -> new InGameMenu(game);
          case 37 -> {
            List<Move> moves = game.getMainBoard().getMoveHistory();
            if (moves.size() - goBack > 0) {
              goBack++;
              Move move = moves.get(moves.size() - goBack);
                if (moves.size() - goBack > 1) {
                    displayFen(move.fen(), moves.get(moves.size() - goBack - 1));
                } else {
                    displayFen(move.fen(), null);
                }
              Sound.moveSound(move);
            }
          }
          case 39 -> {
            List<Move> moves = game.getMainBoard().getMoveHistory();
            if (goBack > 1) {
              goBack--;
              Move move = moves.get(moves.size() - goBack);

              displayFen(move.fen(), moves.get(moves.size() - goBack - 1));
              Sound.moveSound(moves.get(moves.size() - goBack - 1));
            } else if (goBack == 1) {
              goBack--;
              Sound.moveSound(game.getMainBoard().getLastMove());
              update();
            }
          }
          case 122 -> maximize();
          case 38 -> {
              if (goBack > 0) {
                  update();
              }
          }
          case 40 -> {
            List<Move> moves = game.getMainBoard().getMoveHistory();
            goBack = moves.size();
            if (!moves.isEmpty()) {
              displayFen(game.getMainBoard().getMoveHistory().get(0).fen(), null);
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
        if (windowEvent.getNewState() == JFrame.MAXIMIZED_BOTH) {
            maximize();
        }
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

  public void setEndEvaluation() {
    evalBar.setEndResult();
  }
}
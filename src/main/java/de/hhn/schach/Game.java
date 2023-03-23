package de.hhn.schach;

import de.hhn.schach.frontend.Window;

public class Game {
    private State state;

    public Game(boolean rotatedPieces, boolean rotatedBoard, String fen) {
        Board mainBoard;
        mainBoard = new Board(fen);
        Window window = new Window(rotatedPieces, rotatedBoard);
        window.setVisible(true);
//      window.update(mainBoard);
    }
}

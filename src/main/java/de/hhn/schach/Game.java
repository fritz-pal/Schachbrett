package de.hhn.schach;

import de.hhn.schach.frontend.Window;

public class Game {
    private Board mainBoard;
    private Window window;
    private State state;
    private boolean rotatedPieces;


    public Game(boolean rotatedPieces, boolean rotatedBoard, String fen) {
        //if(!Board.isValidFen(fen)) throw new IllegalArgumentException("Invalid FEN");
        this.rotatedPieces = rotatedPieces;
        mainBoard = new Board();
        window = new Window(rotatedPieces, rotatedBoard);
        window.setVisible(true);
        window.update(mainBoard);

    }
}

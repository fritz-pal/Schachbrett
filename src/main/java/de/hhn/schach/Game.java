package de.hhn.schach;

import de.hhn.schach.frontend.Window;

public class Game {
    private State state;
    private final Board mainBoard;

    public Game(boolean rotatedPieces, boolean rotatedBoard, String fen) {
        mainBoard = new Board(fen);
        Window window = new Window(this, rotatedPieces, rotatedBoard);
        window.setVisible(true);
        window.update(mainBoard);
        state = new TurnState(this);
    }

    public State getState() {
        return state;
    }

    public void changeState(State state) {
        this.state = state;
        System.out.println("Changed state to " + state.getClass().getSimpleName());
    }

    public Board getMainBoard() {
        return mainBoard;
    }
}

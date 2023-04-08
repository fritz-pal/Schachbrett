package de.hhn.schach;

import de.hhn.schach.frontend.Window;

public class Game {
    private final Board mainBoard;
    private final Window window;
    private State state;
    private Vec2 selectedTile = null;

    public Game(boolean rotatedPieces, boolean rotatedBoard, String fen) {
        mainBoard = new Board(fen);
        window = new Window(this, rotatedPieces, rotatedBoard);
        window.setVisible(true);
        window.update(mainBoard);
        state = new TurnState(this);
    }

    public State getState() {
        return state;
    }

    public void changeState(State state) {
        this.state = state;
        update();
    }

    public Vec2 getSelectedTile() {
        return selectedTile;
    }

    public void setSelectedTile(Vec2 selectedTile) {
        this.selectedTile = selectedTile;
    }

    public void update() {
        window.update(mainBoard);
    }

    public Window getWindow() {
        return window;
    }

    public Board getMainBoard() {
        return mainBoard;
    }
}

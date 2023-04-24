package de.hhn.schach;

import de.hhn.schach.frontend.EndScreen;
import de.hhn.schach.frontend.Window;
import de.hhn.schach.stateMachine.PieceSelectedState;
import de.hhn.schach.stateMachine.State;
import de.hhn.schach.stateMachine.TurnState;
import de.hhn.schach.utils.Vec2;

public class Game {
    private final Board mainBoard;
    private final Window window;
    private State state;
    private Vec2 selectedTile = null;
    private final String whiteName = "";
    private final String blackName = "";
    private final int whiteElo = -1;
    private final int blackElo = -1;
    private boolean ended = false;

    public Game(boolean rotatedPieces, boolean rotatedBoard, String fen) {
        mainBoard = new Board(fen);
        window = new Window(this, rotatedPieces, rotatedBoard);
        window.setVisible(true);
        window.update(mainBoard, false);
        state = new TurnState(this);
    }

    public State getState() {
        return state;
    }

    public void changeState(State state) {
        this.state = state;
        update(state instanceof PieceSelectedState);
        if (ended) new EndScreen(mainBoard.getResult(), mainBoard.getPGN(), mainBoard.getFen(), window);

    }

    public Vec2 getSelectedTile() {
        return selectedTile;
    }

    public void setSelectedTile(Vec2 selectedTile) {
        this.selectedTile = selectedTile;
    }

    public void update(boolean performanceMode) {
        window.update(mainBoard, performanceMode);
    }

    public Window getWindow() {
        return window;
    }

    public Board getMainBoard() {
        return mainBoard;
    }

    public void gameEnded() {
        this.ended = true;
    }
}

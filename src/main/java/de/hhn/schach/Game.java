package de.hhn.schach;

import de.hhn.schach.engine.UCIProtocol;
import de.hhn.schach.frontend.EndScreen;
import de.hhn.schach.frontend.Window;
import de.hhn.schach.stateMachine.*;
import de.hhn.schach.utils.PieceType;
import de.hhn.schach.utils.Vec2;

import java.util.Timer;
import java.util.TimerTask;

public class Game {
    private final Board mainBoard;
    private final Window window;
    private final String whiteName;
    private final String blackName;
    private final int whiteElo;
    private final int blackElo;
    private final boolean engineWhite;
    private final UCIProtocol uci;
    private final boolean againstEngine;
    private State state;
    private Vec2 selectedTile = null;
    private boolean ended = false;
    private EndScreen endScreen = null;
    private String engineName = "Stockfish";

    public Game(boolean rotatedPieces, boolean rotatedBoard, boolean againstEngine, String fen, String whiteName, String blackName, int whiteElo, int blackElo) {
        this.whiteName = whiteName;
        this.blackName = blackName;
        this.whiteElo = whiteElo;
        this.blackElo = blackElo;
        this.againstEngine = againstEngine;
        mainBoard = new Board(this, fen);
        window = new Window(this, rotatedPieces, rotatedBoard);
        window.setVisible(true);
        window.update(mainBoard, false);
        if (againstEngine) {
            if (mainBoard.isCustomFen() && (mainBoard.isWhiteTurn() && rotatedBoard || !mainBoard.isWhiteTurn() && !rotatedBoard)) {
                engineWhite = !rotatedBoard;
            } else {
                engineWhite = rotatedBoard;
            }
            uci = new UCIProtocol(this);
            state = new TurnAgainstEngineState(this);
        } else {
            engineWhite = false;
            uci = null;
            state = new TurnState(this);
        }
    }

    public State getState() {
        return state;
    }

    public void changeState(State state) {
        this.state = state;
        update(state instanceof PieceSelectedState);
        if (ended) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    endScreen = new EndScreen(mainBoard.getResult(), mainBoard.getPGN(), mainBoard.getFen(), Game.this);
                }
            }, 1000);
        }
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

    public void endGame() {
        this.ended = true;
    }

    public String getEngineName() {
        return engineName;
    }

    public void setEngineName(String engineName) {
        this.engineName = engineName;
    }

    public String getName(boolean white) {
        return white ? whiteName : blackName;
    }

    public int getElo(boolean white) {
        return white ? whiteElo : blackElo;
    }

    public boolean isAgainstEngine() {
        return againstEngine;
    }

    public boolean isEngineWhite() {
        return engineWhite;
    }

    public void foundMove(String notation) {
        uci.printInfo();
        Vec2 from = new Vec2(notation.substring(0, 2));
        Vec2 to = new Vec2(notation.substring(2, 4));
        mainBoard.move(from, to, true, notation.length() > 4 ? PieceType.fromNotation(notation.charAt(4)) : null);
        if (mainBoard.isCheckmate() || mainBoard.isStalemate()) endGame();
        changeState(new TurnAgainstEngineState(this));
    }

    public void startEngine() {
        selectedTile = null;
        changeState(new EnginePonderState());
        uci.startSearching();
    }

    public void stop() {
        if (uci != null) uci.quit();
        if (window != null) window.dispose();
        if (endScreen != null) endScreen.dispose();
    }
}

package de.hhn.schach;

import de.hhn.schach.engine.UCIProtocol;
import de.hhn.schach.frontend.EndScreen;
import de.hhn.schach.frontend.Sound;
import de.hhn.schach.frontend.Window;
import de.hhn.schach.stateMachine.*;
import de.hhn.schach.utils.PieceType;
import de.hhn.schach.utils.Result;
import de.hhn.schach.utils.Vec2;

import java.util.Timer;
import java.util.TimerTask;

public class Game {
    private final Board mainBoard;
    private final String whiteName;
    private final String blackName;
    private final int whiteElo;
    private final int blackElo;
    private final boolean engineWhite;
    private final boolean rotatedPieces, rotatedBoard;
    private final UCIProtocol uci;
    private final boolean againstEngine;
    private final int difficulty;
    private final boolean withEval;
    private Window window;
    private State state;
    private Vec2 selectedTile = null;
    private EndScreen endScreen = null;
    private String engineName = "Stockfish";

    public Game(boolean rotatedPieces, boolean rotatedBoard, boolean againstEngine, String fen, String whiteName, String blackName, int whiteElo, int blackElo, int difficulty, boolean withEval) {
        this.whiteName = whiteName;
        this.blackName = blackName;
        this.difficulty = difficulty;
        this.whiteElo = whiteElo;
        this.blackElo = blackElo;
        this.againstEngine = againstEngine;
        this.rotatedPieces = rotatedPieces;
        this.rotatedBoard = rotatedBoard;
        this.withEval = withEval;
        mainBoard = new Board(this, fen);
        window = new Window(this, false);
        window.update();
        if (againstEngine) {
            if (mainBoard.isCustomFen() && (mainBoard.isWhiteTurn() && rotatedBoard || !mainBoard.isWhiteTurn() && !rotatedBoard)) {
                engineWhite = !rotatedBoard;
            } else {
                engineWhite = rotatedBoard;
            }
            uci = new UCIProtocol(this, engineWhite);
            if (engineWhite) state = new EnginePonderState();
            else state = new TurnAgainstEngineState(this);
        } else {
            engineWhite = false;
            uci = null;
            state = new TurnState(this);
        }
    }

    public static int map(int difficulty) {
        return difficulty * 75 + 1350;
    }

    public State getState() {
        return state;
    }

    public void changeState(State state) {
        this.state = state;
        window.update();
    }

    public boolean isRotatedBoard() {
        return rotatedBoard;
    }

    public Vec2 getSelectedTile() {
        return selectedTile;
    }

    public void setSelectedTile(Vec2 selectedTile) {
        this.selectedTile = selectedTile;
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public boolean isWithEval() {
        return withEval;
    }

    public UCIProtocol getUci() {
        return uci;
    }

    public int getEngineDifficulty() {
        return difficulty;
    }

    public Board getMainBoard() {
        return mainBoard;
    }

    public void endGame() {
        Sound.endSound();
        selectedTile = null;
        this.state = new GameEndedState();
        window.update();
        if (withEval) window.setEndEvaluation();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Game.this.endScreen();
            }
        }, 1000);
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

    public boolean isRotated(boolean white) {
        return !white && rotatedPieces && !rotatedBoard || white && rotatedPieces && rotatedBoard;
    }

    public void foundMove(String notation) {
        if (notation.equals("0000")) return;
        uci.printInfo();
        Vec2 from = new Vec2(notation.substring(0, 2));
        Vec2 to = new Vec2(notation.substring(2, 4));
        mainBoard.move(from, to, true, notation.length() > 4 ? PieceType.fromNotation(notation.charAt(4)) : null);
        if (mainBoard.getResult() != Result.NOTFINISHED) {
            endGame();
        } else {
            changeState(new TurnAgainstEngineState(this));
        }
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

    public void endScreen() {
        if (endScreen != null) endScreen.dispose();
        endScreen = new EndScreen(this);
    }
}
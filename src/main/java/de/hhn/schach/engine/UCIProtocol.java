package de.hhn.schach.engine;

import de.hhn.schach.Game;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class UCIProtocol {
    private final Game game;
    private Process process;
    private String posCommand = "position startpos";

    public UCIProtocol(Game game) {
        this.game = game;
        if (!game.getMainBoard().getFromFen().isEmpty()) posCommand = "position fen " + game.getMainBoard().getFromFen();

        Stockfish stockfish = new Stockfish();
        stockfish.start();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                UCIProtocol.this.process = stockfish.process;
                sendCommand("uci");
                new CommandListener(UCIProtocol.this, process);
            }
        }, 500);
    }

    public void sendCommand(String command) {
        try {
            BufferedWriter writer = process.outputWriter();
            writer.write(command + "\n");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Could not send command to program.", e);
        }
    }

    public void receiveCommand(String command, String[] params) {
        switch (command) {
            case "uciok" -> sendCommand("isready");
            case "readyok" -> sendCommand("ucinewgame");
            case "bestmove" -> game.foundMove(params[0]);
        }
    }

    public void startSearching() {
        String command = posCommand + " moves " + game.getMainBoard().getAllMovesInEngineNotation();
        sendCommand(command);
        sendCommand("go movetime 1000");
    }

    public void quit() {
        sendCommand("quit");
    }
}

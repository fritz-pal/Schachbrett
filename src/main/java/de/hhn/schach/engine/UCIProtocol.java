package de.hhn.schach.engine;

import de.hhn.schach.Game;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class UCIProtocol {
    private final Game game;
    private int depth = 0;
    private int cp = 0;
    private int mate = 0;
    private Process process;
    private String posCommand = "position startpos";

    public UCIProtocol(Game game) {
        this.game = game;
        if (!game.getMainBoard().getFromFen().isEmpty())
            posCommand = "position fen " + game.getMainBoard().getFromFen();

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
//System.out.println(command + Arrays.toString(params));
        switch (command) {
            case "uciok" -> sendCommand("isready");
            case "readyok" -> sendCommand("ucinewgame");
            case "bestmove" -> game.foundMove(params[0]);
            case "info" -> {
                for (int i = 0; i < params.length; i++) {
                    if (params[i].equals("depth")) depth = Integer.parseInt(params[i + 1]);
                    if (params[i].equals("score") && params[i + 1].equals("cp")) {
                        mate = 0;
                        cp = Integer.parseInt(params[i + 2]);
                        if (!game.isEngineWhite()) cp *= -1;
                    }
                    if (params[i].equals("score") && params[i + 1].equals("mate")) {
                        mate = Integer.parseInt(params[i + 2]);
                        if (!game.isEngineWhite()) mate *= -1;
                    }
                }
            }
        }
    }

    public void startSearching() {
        String command = posCommand + " moves" + game.getMainBoard().getAllMovesInEngineNotation();
        sendCommand(command);
//System.out.println(command);
        sendCommand("go movetime 2000");
    }

    public void quit() {
        sendCommand("quit");
    }

    public void printInfo() {
        System.out.println("Depth: " + depth);
        if (mate == 0) System.out.println("Centipawn: " + cp);
        else System.out.println("Mate in " + mate);
    }
}

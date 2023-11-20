package de.hhn.schach.engine;

import de.hhn.schach.Game;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class UCIProtocol {
  private final Game game;
  private final boolean autoStart;
  private int depth = 0;
  private int cp = 30;
  private int mate = 0;
  private Process process;
  private String posCommand = "position startpos";
  private boolean ignoreNextBestMove = false;

  public UCIProtocol(Game game, boolean autoStart) {
    this.autoStart = autoStart;
    this.game = game;
    if (!game.getMainBoard().getFromFen().isEmpty()) {
      posCommand = "position fen " + game.getMainBoard().getFromFen();
    }

    Engine engine = new Engine();
    engine.start();
    new Timer().schedule(new TimerTask() {
      int counter = 0;

      @Override
      public void run() {
        counter++;
        if (engine.process != null) {
          UCIProtocol.this.process = engine.process;
          sendCommand("uci");
          new CommandListener(UCIProtocol.this, process);
          System.out.println("Engine started in " + counter * 10 + "ms.");
          cancel();
        }
      }
    }, 10, 10);
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
      case "uciok" -> {
        sendCommand("setoption name Skill Level value " + game.getEngineDifficulty());
        sendCommand("isready");
      }
      case "readyok" -> {
        sendCommand("ucinewgame");
        if (autoStart) {
          startSearching();
        }
      }
      case "bestmove" -> {
        if (ignoreNextBestMove) {
          ignoreNextBestMove = false;
          return;
        }
        game.foundMove(params[0]);
        ignoreNextBestMove = true;
        sendCommand(posCommand + " moves" + game.getMainBoard().getAllMovesInEngineNotation());
        sendCommand("go infinite");
      }
      case "info" -> {
        for (int i = 0; i < params.length; i++) {
          if (params[i].equals("depth")) {
            depth = Integer.parseInt(params[i + 1]);
          }
          if (params[i].equals("score") && params[i + 1].equals("cp")) {
            mate = 0;
            cp = Integer.parseInt(params[i + 2]);
            if (!game.getMainBoard().isWhiteTurn()) {
              cp *= -1;
            }
            game.getWindow().receivedEval(cp, mate);
          }
          if (params[i].equals("score") && params[i + 1].equals("mate")) {
            mate = Integer.parseInt(params[i + 2]);
            if (!game.getMainBoard().isWhiteTurn()) {
              mate *= -1;
            }
            game.getWindow().receivedEval(cp, mate);
          }
        }
      }
      case "id" -> {
        if (params[0].equals("name")) {
          game.setEngineName(String.join(" ", params).substring(5));
        }
      }
    }
  }

  public void startSearching() {
    sendCommand("stop");
    String command = posCommand + " moves" + game.getMainBoard().getAllMovesInEngineNotation();
    sendCommand(command);
    sendCommand("go movetime 2000");
  }

  public int getCp() {
    return cp;
  }

  public int getMate() {
    return mate;
  }

  public void quit() {
    sendCommand("quit");
  }

  public void printInfo() {
    System.out.println("Depth: " + depth);
    if (mate == 0) {
      System.out.println("Centipawn: " + cp);
    } else {
      System.out.println("Mate in " + mate);
    }
  }
}
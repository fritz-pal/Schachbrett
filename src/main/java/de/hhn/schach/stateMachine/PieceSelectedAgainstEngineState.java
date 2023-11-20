package de.hhn.schach.stateMachine;

import static de.hhn.schach.stateMachine.PieceSelectedState.promotion;

import de.hhn.schach.Board;
import de.hhn.schach.Game;
import de.hhn.schach.utils.Vec2;

public class PieceSelectedAgainstEngineState implements State {
  private final Game game;

  public PieceSelectedAgainstEngineState(Game game) {
    this.game = game;
  }

  @Override
  public void onTileClick(Vec2 pos) {
    Board board = game.getMainBoard();
    if (game.getSelectedTile() != null && game.getSelectedTile().equals(pos)) {
      game.setSelectedTile(null);
      game.changeState(new TurnAgainstEngineState(game));
      return;
    }
    if (board.occupied(pos) && board.getPiece(pos).isWhite() != game.isEngineWhite()) {
      game.setSelectedTile(pos);
      game.getWindow().update();
      return;
    }
    if (!board.occupied(pos) || board.getPiece(pos).isWhite() == game.isEngineWhite()) {
      if (board.isLegalMove(game.getSelectedTile(), pos)) {
        if (promotion(pos, board, game)) {
          return;
        }
        game.startEngine();
      } else {
        game.setSelectedTile(null);
        game.changeState(new TurnAgainstEngineState(game));
      }
    }
  }
}
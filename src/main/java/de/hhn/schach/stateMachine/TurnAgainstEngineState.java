package de.hhn.schach.stateMachine;

import de.hhn.schach.Board;
import de.hhn.schach.Game;
import de.hhn.schach.utils.Vec2;

public class TurnAgainstEngineState implements State{
    private final Game game;

    public TurnAgainstEngineState(Game game) {
        this.game = game;
    }
    @Override
    public void onTileClick(Vec2 pos) {
        Board board = game.getMainBoard();
        if (board.occupied(pos)) {
            if (board.getPiece(pos).isWhite() != game.isEngineWhite()) {
                game.setSelectedTile(pos);
                game.changeState(new PieceSelectedAgainstEngineState(game));
            }
        }
    }
}

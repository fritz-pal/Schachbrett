package de.hhn.schach.stateMachine;

import de.hhn.schach.Board;
import de.hhn.schach.Game;
import de.hhn.schach.utils.Vec2;

public class TurnState implements State {

    private final Game game;

    public TurnState(Game game) {
        this.game = game;
    }

    @Override
    public void onTileClick(Vec2 pos) {
        Board board = game.getMainBoard();
        if (board.occupied(pos)) {
            if (board.getPiece(pos).isWhite() == board.isWhiteTurn()) {
                game.setSelectedTile(pos);
                game.changeState(new PieceSelectedState(game));
            }
        }
    }
}

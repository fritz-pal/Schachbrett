package de.hhn.schach.stateMachine;

import de.hhn.schach.Board;
import de.hhn.schach.Game;
import de.hhn.schach.utils.Move;
import de.hhn.schach.utils.Vec2;
import de.hhn.schach.frontend.Sound;

public class PieceSelectedState implements State {

    private final Game game;

    public PieceSelectedState(Game game) {
        this.game = game;
    }

    @Override
    public void onTileClick(Vec2 pos) {
        Board board = game.getMainBoard();
        if (game.getSelectedTile() != null && game.getSelectedTile().equals(pos)) {
            game.setSelectedTile(null);
            game.changeState(new TurnState(game));
            return;
        }
        if (board.occupied(pos) && board.getPiece(pos).isWhite() == board.isWhiteTurn()) {
            game.setSelectedTile(pos);
            game.update(true);
            return;
        }
        if (!board.occupied(pos) || board.getPiece(pos).isWhite() != board.isWhiteTurn()) {
            if (board.isLegalMove(game.getSelectedTile(), pos)) {
                Move move = board.move(game.getSelectedTile(), pos, true);
                Sound.play(move);
                if (board.isCheckmate() || board.isStalemate()) game.gameEnded();
            }
            game.setSelectedTile(null);
            game.changeState(new TurnState(game));
        }
    }
}

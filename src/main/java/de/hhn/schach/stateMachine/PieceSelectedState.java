package de.hhn.schach.stateMachine;

import de.hhn.schach.Board;
import de.hhn.schach.Game;
import de.hhn.schach.frontend.PromotionWindow;
import de.hhn.schach.utils.Result;
import de.hhn.schach.utils.Vec2;

public class PieceSelectedState implements State {

    private final Game game;

    public PieceSelectedState(Game game) {
        this.game = game;
    }

    static boolean promotion(Vec2 pos, Board board, Game game) {
        if (board.isPromotingMove(game.getSelectedTile(), pos)) {
            new PromotionWindow(game, board.isWhiteTurn(), pos);
            return true;
        }
        board.move(game.getSelectedTile(), pos, true, null);
        if (board.getResult() != Result.NOTFINISHED) {
            game.endGame();
            return true;
        }
        return false;
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
            game.getWindow().update();
            return;
        }
        if (!board.occupied(pos) || board.getPiece(pos).isWhite() != board.isWhiteTurn()) {
            if (board.isLegalMove(game.getSelectedTile(), pos)) {
                if (promotion(pos, board, game)) {
                    return;
                }
            }
            game.setSelectedTile(null);
            game.changeState(new TurnState(game));
        }
    }
}
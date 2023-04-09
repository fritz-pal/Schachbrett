package de.hhn.schach;

import de.hhn.schach.frontend.EndScreen;
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
            game.update();
            return;
        }
        if (!board.occupied(pos) || board.getPiece(pos).isWhite() != board.isWhiteTurn()) {
            if (board.isLegalMove(game.getSelectedTile(), pos)) {
                Move move = board.move(game.getSelectedTile(), pos, true);
                Sound.play(move);
                if (board.isCheckmate() || board.isStalemate())
                    new EndScreen(board.getResult(), board.getPGN(), board.getFen(), game.getWindow());
            }
            game.setSelectedTile(null);
            game.changeState(new TurnState(game));
        }
    }
}

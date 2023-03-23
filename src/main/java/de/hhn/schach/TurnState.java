package de.hhn.schach;

public class TurnState implements State {

    private boolean whiteTurn;
    private Game game;

    public TurnState(Game game) {
        this.whiteTurn = game.getMainBoard().isWhiteTurn();
        this.game = game;
    }

    @Override
    public void onTileClick(Vec2 pos) {
        Board board = game.getMainBoard();
        if (board.occupied(pos)) {
            if (board.getPiece(pos).isWhite() == whiteTurn) {
                game.changeState(new PieceSelectedState(whiteTurn, pos));
            }
        }
    }
}

package de.hhn.schach;

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
            board.move(game.getSelectedTile(), pos);
            game.setSelectedTile(null);
            board.setTurn(!board.isWhiteTurn());
            game.changeState(new TurnState(game));
        }
    }
}

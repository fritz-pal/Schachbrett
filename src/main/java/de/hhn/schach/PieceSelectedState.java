package de.hhn.schach;

public class PieceSelectedState implements State {
    private boolean whiteTurn;
    private Vec2 selectedTile;
    private Game game;

    public PieceSelectedState(Game game) {
        this.whiteTurn = game.getMainBoard().isWhiteTurn();
        this.selectedTile = game.getSelectedTile();
        this.game = game;
    }

    @Override
    public void onTileClick(Vec2 pos) {
        Board board = game.getMainBoard();
        if (game.getSelectedTile() != null && game.getSelectedTile().equals(pos)) {
            game.setSelectedTile(null);
            game.changeState(new TurnState(game));
        } else {
            if (board.occupied(pos) && board.getPiece(pos).isWhite() == whiteTurn) {
                game.setSelectedTile(pos);
                game.update();
            }
        }
    }
}

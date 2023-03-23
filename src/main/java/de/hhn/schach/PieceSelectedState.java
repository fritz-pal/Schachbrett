package de.hhn.schach;

public class PieceSelectedState implements State{
    private boolean whiteTurn;
    private Vec2 pos;
    public PieceSelectedState(boolean whiteTurn, Vec2 pos) {
        this.whiteTurn = whiteTurn;
        this.pos = pos;
    }

    @Override
    public void onTileClick(Vec2 pos) {

    }
}

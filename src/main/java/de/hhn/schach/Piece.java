package de.hhn.schach;

public class Piece {
    private PieceType type;
    private boolean isWhite;
    private Vec2 pos;

    public Piece(PieceType type, boolean isWhite) {
        this.type = type;
        this.isWhite = isWhite;
        this.pos = pos;
    }

    public PieceType getType() {
        return type;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public Vec2 getPos() {
        return pos;
    }

    public void setPos(Vec2 pos) {
        this.pos = pos;
    }
}

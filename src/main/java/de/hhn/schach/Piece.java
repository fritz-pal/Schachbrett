package de.hhn.schach;

public class Piece {
    private PieceType type;
    private boolean isWhite;

    public Piece(PieceType type, boolean isWhite) {
        this.type = type;
        this.isWhite = isWhite;
    }

    public PieceType getType() {
        return type;
    }

    public boolean isWhite() {
        return isWhite;
    }

    @Override
    public String toString() {
        return (isWhite ? "White " : "Black ") + type.toString();
    }
}

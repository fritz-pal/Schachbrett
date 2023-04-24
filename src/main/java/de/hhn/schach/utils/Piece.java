package de.hhn.schach.utils;

public record Piece(PieceType type, boolean isWhite) {

    @Override
    public String toString() {
        return (isWhite ? "White " : "Black ") + type.toString();
    }
}

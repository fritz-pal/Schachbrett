package de.hhn.schach;

public class Move {
    private final Vec2 from;
    private final Vec2 to;
    private final Piece piece;
    private final String notation;

    public Move(Vec2 from, Vec2 to, Piece piece, String notation) {
        this.from = from;
        this.to = to;
        this.piece = piece;
        this.notation = notation;
    }

    public Vec2 getFrom() {
        return from;
    }

    public Vec2 getTo() {
        return to;
    }

    public String getNotation() {
        return notation;
    }

    public Piece getPiece() {
        return piece;
    }
}

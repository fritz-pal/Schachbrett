package de.hhn.schach.utils;

public enum PieceType {
    PAWN('P'),
    KNIGHT('N'),
    BISHOP('B'),
    ROOK('R'),
    QUEEN('Q'),
    KING('K');

    private final char notation;

    PieceType(char notation) {
        this.notation = notation;
    }

    public static PieceType fromNotation(char notation) {
        for (PieceType pieceType : values()) {
            if (pieceType.getNotation() == notation) {
                return pieceType;
            }
        }
        return null;
    }

    public char getNotation() {
        return notation;
    }
}

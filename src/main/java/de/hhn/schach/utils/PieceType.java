package de.hhn.schach.utils;

public enum PieceType {
    QUEEN('Q'),
    ROOK('R'),
    BISHOP('B'),
    KNIGHT('N'),
    PAWN('P'),
    KING('K');

    private final char notation;

    PieceType(char notation) {
        this.notation = notation;
    }

    public static PieceType fromNotation(char notation) {
        for (PieceType pieceType : values()) {
            if (pieceType.getNotation() == Character.toUpperCase(notation)) {
                return pieceType;
            }
        }
        return null;
    }

    public char getNotation() {
        return notation;
    }
}

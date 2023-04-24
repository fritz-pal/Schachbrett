package de.hhn.schach.utils;

public record Move(Vec2 from, Vec2 to, Piece piece, String notation) {
    public boolean isCapture() {
        return notation.contains("x");
    }

    public boolean isCheck() {
        return notation.contains("+") || notation.contains("#");
    }

    public boolean isCheckmate() {
        return notation.contains("#");
    }

    public boolean isPromotion() {
        return notation.contains("=");
    }

    public boolean isCastling() {
        return notation.contains("O-O");
    }

    public String getEngineNotation() {
        if (!isPromotion()) return from.getName() + to.getName();
        return from.getName() + to.getName() + (notation.charAt(notation.length() - 1) + "").toLowerCase();
    }
}
package de.hhn.schach;

public enum Result {
    WHITEWONBYCHECKMATE,
    BLACKWONBYCHECKMATE,
    BLACKWONBYRESIGNATION,
    WHITEWONBYRESIGNATION,
    DRAWBYSTALEMATE,
    DRAWBYFIFTYMOVESRULE,
    DRAWBYTHREEFOLDREPETITION,
    DRAWBYINSUFFICIENTMATERIAL,
    DRAWBYAGREEMENT,
    NOTFINISHED;

    @Override
    public String toString() {
        return switch (this) {
            case WHITEWONBYCHECKMATE -> "White won by checkmate!";
            case BLACKWONBYCHECKMATE -> "Black won by checkmate!";
            case BLACKWONBYRESIGNATION -> "Black won by resignation!";
            case WHITEWONBYRESIGNATION -> "White won by resignation!";
            case DRAWBYSTALEMATE -> "Draw by stalemate!";
            case DRAWBYFIFTYMOVESRULE -> "Draw by fifty moves rule!";
            case DRAWBYTHREEFOLDREPETITION -> "Draw by threefold repetition!";
            case DRAWBYINSUFFICIENTMATERIAL -> "Draw by insufficient material!";
            case DRAWBYAGREEMENT -> "Draw by agreement!";
            case NOTFINISHED -> "Game not finished!";
        };
    }
}

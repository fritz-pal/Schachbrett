package de.hhn.schach.utils;

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

    public String getNotation(){
        return switch (this) {
            case WHITEWONBYCHECKMATE -> "1-0";
            case BLACKWONBYCHECKMATE -> "0-1";
            case BLACKWONBYRESIGNATION -> "0-1";
            case WHITEWONBYRESIGNATION -> "1-0";
            case DRAWBYSTALEMATE -> "1/2-1/2";
            case DRAWBYFIFTYMOVESRULE -> "1/2-1/2";
            case DRAWBYTHREEFOLDREPETITION -> "1/2-1/2";
            case DRAWBYINSUFFICIENTMATERIAL -> "1/2-1/2";
            case DRAWBYAGREEMENT -> "1/2-1/2";
            case NOTFINISHED -> "*";
        };
    }
}

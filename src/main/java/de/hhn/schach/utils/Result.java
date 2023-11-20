package de.hhn.schach.utils;

public enum Result {
  WHITEWONBYCHECKMATE("1-0"),
  BLACKWONBYCHECKMATE("0-1"),
  BLACKWONBYRESIGNATION("0-1"),
  WHITEWONBYRESIGNATION("1-0"),
  DRAWBYSTALEMATE("1/2-1/2"),
  DRAWBYFIFTYMOVESRULE("1/2-1/2"),
  DRAWBYTHREEFOLDREPETITION("1/2-1/2"),
  DRAWBYINSUFFICIENTMATERIAL("1/2-1/2"),
  DRAWBYAGREEMENT("1/2-1/2"),
  NOTFINISHED("*");

  private final String notation;

  Result(String notation) {
    this.notation = notation;
  }

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

  public boolean whiteWon() {
    return this == WHITEWONBYCHECKMATE || this == WHITEWONBYRESIGNATION;
  }

  public boolean blackWon() {
    return this == BLACKWONBYCHECKMATE || this == BLACKWONBYRESIGNATION;
  }

  public boolean isDraw() {
    return this == DRAWBYSTALEMATE || this == DRAWBYFIFTYMOVESRULE ||
        this == DRAWBYTHREEFOLDREPETITION || this == DRAWBYINSUFFICIENTMATERIAL ||
        this == DRAWBYAGREEMENT;
  }

  public String getNotation() {
    return notation;
  }
}
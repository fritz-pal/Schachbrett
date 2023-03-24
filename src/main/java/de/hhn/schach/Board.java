package de.hhn.schach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private final Map<Vec2, Piece> pieces = new HashMap<>();
    private boolean whiteTurn;

    public Board() {
        this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public Board(String fen) {
        if (!isValidFen(fen)) fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        String[] fenParts = fen.split(" ");
        String[] fenRows = fenParts[0].split("/");
        for (int i = 0; i < 8; i++) {
            String fenRow = fenRows[i];
            int j = 0;
            for (int k = 0; k < fenRow.length(); k++) {
                char c = fenRow.charAt(k);
                if (Character.isDigit(c)) {
                    j += Character.getNumericValue(c);
                } else {
                    pieces.put(new Vec2(7 - i, j), new Piece(PieceType.fromNotation(Character.toUpperCase(c)), Character.isUpperCase(c)));
                    j++;
                }
            }
        }
        whiteTurn = fenParts[1] == null || fenParts[1].equals("w");
    }

    public static boolean isValidFen(String fen) {
        if (fen == null || fen.isEmpty()) return false;
        String[] fenParts = fen.split(" ");
        String[] fenRows = fenParts[0].split("/");
        if (fenRows.length != 8) return false;
        for (int i = 0; i < 8; i++) {
            int number = 0;
            String fenRow = fenRows[i];
            for (int j = 0; j < fenRow.length(); j++) {
                char c = fenRow.charAt(j);
                if (Character.isDigit(c)) {
                    number += Character.getNumericValue(c);
                } else {
                    number++;
                }
            }
            if (number != 8) return false;
        }
        if (fenParts[1] != null && !fenParts[1].equals("w") && !fenParts[1].equals("b")) return false;

        return true;
    }

    public String getFen() {
        StringBuilder fen = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int empty = 0;
            for (int j = 0; j < 8; j++) {
                Piece piece = pieces.get(new Vec2(i, j));
                if (piece == null) {
                    empty++;
                } else {
                    if (empty > 0) {
                        fen.append(empty);
                        empty = 0;
                    }
                    fen.append(piece.isWhite() ? piece.getType().getNotation() : Character.toLowerCase(piece.getType().getNotation()));
                }
            }
            if (empty > 0) {
                fen.append(empty);
            }
            if (i < 7) {
                fen.append("/");
            }
        }
        fen.append(" ");
        fen.append(whiteTurn ? "w" : "b");
        return fen.toString();
    }

    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            boardString.append("[");
            for (int j = 0; j < 8; j++) {
                Piece piece = pieces.get(new Vec2(i, j));
                boardString.append(piece == null ? " " : piece.isWhite() ? piece.getType().getNotation() : Character.toLowerCase(piece.getType().getNotation()));
                if (j != 7) boardString.append(", ");
            }
            boardString.append("]\n");
        }
        return boardString.toString();
    }

    public Piece getPiece(Vec2 pos) {
        return pieces.get(pos);
    }

    public void setPiece(Vec2 pos, Piece piece) {
        pieces.put(pos, piece);
    }

    public boolean isLegalMove(Vec2 from, Vec2 to) {
        return false;
        //TODO
    }

    public List<Vec2> getAllLegalMoves(Vec2 pos) {
        List<Vec2> moves = new ArrayList<>();
        if (!occupied(pos)) return moves;
        Piece piece = getPiece(pos);
        switch (piece.getType()) {
            case KING -> {
                moves.addAll(getKingMoves(pos));
            }
            case QUEEN -> {
                moves.addAll(getRookMoves(pos));
                moves.addAll(getBishopMoves(pos));

            }
            case ROOK -> {
                moves.addAll(getRookMoves(pos));
            }
            case BISHOP -> {
                moves.addAll(getBishopMoves(pos));
            }
            case KNIGHT -> {
                moves.addAll(getKnightMoves(pos));
            }
            case PAWN -> {
                moves.addAll(getPawnMoves(pos));
            }
        }
        return moves;
    }

    private List<Vec2> getKingMoves(Vec2 pos) {
        List<Vec2> moves = new ArrayList<>();
        return moves;
    }

    private List<Vec2> getRookMoves(Vec2 pos) {
        List<Vec2> moves = new ArrayList<>();
        return moves;
    }

    private List<Vec2> getBishopMoves(Vec2 pos) {
        List<Vec2> moves = new ArrayList<>();
        return moves;
    }

    private List<Vec2> getKnightMoves(Vec2 pos) {
        List<Vec2> moves = new ArrayList<>();
        return moves;
    }

    private List<Vec2> getPawnMoves(Vec2 pos) {
        List<Vec2> moves = new ArrayList<>();
        return moves;
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public void setTurn(boolean whiteTurn) {
        this.whiteTurn = whiteTurn;
    }

    public boolean occupied(Vec2 pos) {
        return pieces.containsKey(pos);
    }

    public void move(Vec2 from, Vec2 to) {
        Piece piece = pieces.remove(from);
        if (piece.getType().equals(PieceType.PAWN) && ((to.getX() == 0 && !piece.isWhite()) || (to.getX() == 7 && piece.isWhite())))
            piece = new Piece(PieceType.QUEEN, piece.isWhite());
        pieces.put(to, piece);
    }
}

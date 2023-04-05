package de.hhn.schach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board implements Cloneable {
    private final Map<Vec2, Piece> pieces = new HashMap<>();

    private Vec2 enPassantTarget = null;
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

    public Board(Map<Vec2, Piece> pieces, boolean whiteTurn) {
        this.pieces.putAll(pieces);
        this.whiteTurn = whiteTurn;
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
                    fen.append(piece.isWhite() ? piece.type().getNotation() : Character.toLowerCase(piece.type().getNotation()));
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
                boardString.append(piece == null ? " " : piece.isWhite() ? piece.type().getNotation() : Character.toLowerCase(piece.type().getNotation()));
                if (j != 7) boardString.append(", ");
            }
            boardString.append("]\n");
        }
        return boardString.toString();
    }

    public Piece getPiece(Vec2 pos) {
        return pieces.get(pos);
    }

    public Map<Vec2, Piece> getPieces() {
        return pieces;
    }

    public void setPiece(Vec2 pos, Piece piece) {
        pieces.put(pos, piece);
    }

    public boolean isLegalMove(Vec2 from, Vec2 to) {
        return getAllLegalMoves(from).contains(to);
    }

    public List<Vec2> getAllLegalMoves(Vec2 pos) {
        List<Vec2> result = new ArrayList<>();

        for (Vec2 move : getAllPseudoLegalMoves(pos)) {
            if (ableToTakeKing(pos, move)) continue;
            result.add(move);
        }
        return result;
    }

    private List<Vec2> getAllPseudoLegalMoves(Vec2 pos) {
        List<Vec2> moves = new ArrayList<>();
        if (!occupied(pos)) return moves;
        Piece piece = getPiece(pos);
        switch (piece.type()) {
            case KING -> moves.addAll(getKingMoves(pos));
            case QUEEN -> {
                moves.addAll(getRookMoves(pos));
                moves.addAll(getBishopMoves(pos));

            }
            case ROOK -> moves.addAll(getRookMoves(pos));
            case BISHOP -> moves.addAll(getBishopMoves(pos));
            case KNIGHT -> moves.addAll(getKnightMoves(pos));
            case PAWN -> moves.addAll(getPawnMoves(pos));
        }

        return moves;
    }

    private List<Vec2> getKingMoves(Vec2 pos) {
        List<Vec2> moves = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                moves.addAll(inBoundsAndNotOccupied(pos, pos.getX() + i, pos.getY() + j));
            }
        }
        return moves;
    }

    //returns true if opponent is able to take the king if that move is made
    public boolean ableToTakeKing(Vec2 from, Vec2 to) {
        Board tempBoard = this.clone();
        tempBoard.move(from, to);
        return tempBoard.isInCheck(!tempBoard.isWhiteTurn());
    }

    public boolean isInCheck(boolean white) {
        Vec2 kingPos = getKingPos(white);
        for (Vec2 pos : pieces.keySet()) {
            if (pieces.get(pos).isWhite() != white) {
                if (getAllPseudoLegalMoves(pos).contains(kingPos)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<Vec2> getRookMoves(Vec2 pos) {
        List<Vec2> moves = new ArrayList<>();
        int i = pos.getX() + 1;
        while (i < 8) {
            Vec2 move = new Vec2(i, pos.getY());
            if (occupied(move)) {
                if (getPiece(move).isWhite() != getPiece(pos).isWhite()) {
                    moves.add(move);
                }
                break;
            } else moves.add(move);
            i++;
        }
        i = pos.getX() - 1;
        while (i >= 0) {
            Vec2 move = new Vec2(i, pos.getY());
            if (occupied(move)) {
                if (getPiece(move).isWhite() != getPiece(pos).isWhite()) {
                    moves.add(move);
                }
                break;
            } else moves.add(move);
            i--;
        }
        i = pos.getY() + 1;
        while (i < 8) {
            Vec2 move = new Vec2(pos.getX(), i);
            if (occupied(move)) {
                if (getPiece(move).isWhite() != getPiece(pos).isWhite()) {
                    moves.add(move);
                }
                break;
            } else moves.add(move);
            i++;
        }
        i = pos.getY() - 1;
        while (i >= 0) {
            Vec2 move = new Vec2(pos.getX(), i);
            if (occupied(move)) {
                if (getPiece(move).isWhite() != getPiece(pos).isWhite()) {
                    moves.add(move);
                }
                break;
            } else moves.add(move);
            i--;
        }
        return moves;
    }

    private List<Vec2> getBishopMoves(Vec2 pos) {
        List<Vec2> moves = new ArrayList<>();
        int i = pos.getX() + 1;
        int j = pos.getY() + 1;
        while (i < 8 && j < 8) {
            Vec2 move = new Vec2(i, j);
            if (occupied(move)) {
                if (getPiece(move).isWhite() != getPiece(pos).isWhite()) {
                    moves.add(move);
                }
                break;
            } else moves.add(move);
            i++;
            j++;
        }
        i = pos.getX() + 1;
        j = pos.getY() - 1;
        while (i < 8 && j >= 0) {
            Vec2 move = new Vec2(i, j);
            if (occupied(move)) {
                if (getPiece(move).isWhite() != getPiece(pos).isWhite()) {
                    moves.add(move);
                }
                break;
            } else moves.add(move);
            i++;
            j--;
        }
        i = pos.getX() - 1;
        j = pos.getY() - 1;
        while (i >= 0 && j >= 0) {
            Vec2 move = new Vec2(i, j);
            if (occupied(move)) {
                if (getPiece(move).isWhite() != getPiece(pos).isWhite()) {
                    moves.add(move);
                }
                break;
            } else moves.add(move);
            i--;
            j--;
        }

        i = pos.getX() - 1;
        j = pos.getY() + 1;
        while (i >= 0 && j < 8) {
            Vec2 move = new Vec2(i, j);
            if (occupied(move)) {
                if (getPiece(move).isWhite() != getPiece(pos).isWhite()) {
                    moves.add(move);
                }
                break;
            } else moves.add(move);
            i--;
            j++;
        }
        return moves;
    }

    private List<Vec2> getKnightMoves(Vec2 pos) {
        List<Vec2> moves = new ArrayList<>();
        moves.addAll(inBoundsAndNotOccupied(pos, pos.getX() + 2, pos.getY() + 1));
        moves.addAll(inBoundsAndNotOccupied(pos, pos.getX() + 2, pos.getY() - 1));
        moves.addAll(inBoundsAndNotOccupied(pos, pos.getX() - 2, pos.getY() + 1));
        moves.addAll(inBoundsAndNotOccupied(pos, pos.getX() - 2, pos.getY() - 1));
        moves.addAll(inBoundsAndNotOccupied(pos, pos.getX() + 1, pos.getY() + 2));
        moves.addAll(inBoundsAndNotOccupied(pos, pos.getX() + 1, pos.getY() - 2));
        moves.addAll(inBoundsAndNotOccupied(pos, pos.getX() - 1, pos.getY() + 2));
        moves.addAll(inBoundsAndNotOccupied(pos, pos.getX() - 1, pos.getY() - 2));
        return moves;
    }

    private List<Vec2> getPawnMoves(Vec2 pos) {
        List<Vec2> moves = new ArrayList<>();
        Vec2 move = new Vec2(pos.getX() + (getPiece(pos).isWhite() ? 1 : -1), pos.getY());
        if (!occupied(move)) {
            moves.add(move);
        }
        if ((pos.getX() == 1 && getPiece(pos).isWhite()) || (pos.getX() == 6 && !getPiece(pos).isWhite())) {
            move = new Vec2(pos.getX() + (getPiece(pos).isWhite() ? 2 : -2), pos.getY());
            if (!occupied(move)) {
                moves.add(move);
            }
        }
        if (pos.getY() - 1 >= 0) {
            move = new Vec2(pos.getX() + (getPiece(pos).isWhite() ? 1 : -1), pos.getY() - 1);
            if (occupied(move) && getPiece(move).isWhite() != getPiece(pos).isWhite()) {
                moves.add(move);
            }
        }

        if (pos.getY() + 1 < 8) {
            move = new Vec2(pos.getX() + (getPiece(pos).isWhite() ? 1 : -1), pos.getY() + 1);
            if (occupied(move) && getPiece(move).isWhite() != getPiece(pos).isWhite()) {
                moves.add(move);
            }
        }


        return moves;
    }

    private List<Vec2> inBoundsAndNotOccupied(Vec2 pos, int x, int y) {
        List<Vec2> moves = new ArrayList<>();
        if (Vec2.isInBounds(x, y)) {
            Vec2 move = new Vec2(x, y);
            if (!(occupied(move) && getPiece(move).isWhite() == getPiece(pos).isWhite())) {
                moves.add(move);
            }
        }
        return moves;
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public boolean occupied(Vec2 pos) {
        return pieces.containsKey(pos);
    }

    public void move(Vec2 from, Vec2 to) {
        whiteTurn = !whiteTurn;
        Piece piece = pieces.remove(from);
        if (piece.type().equals(PieceType.PAWN)) {
            if ((to.getX() == 0 && !piece.isWhite()) || (to.getX() == 7 && piece.isWhite())) {
                piece = new Piece(PieceType.QUEEN, piece.isWhite());
            }
        }
        pieces.put(to, piece);
    }

    public Vec2 getKingPos(boolean white) {
        for (Vec2 pos : pieces.keySet()) {
            if (pieces.get(pos).type().equals(PieceType.KING) && pieces.get(pos).isWhite() == white) {
                return pos;
            }
        }
        throw new IllegalStateException("No king found");
    }

    @Override
    protected Board clone() {
        return new Board(this.pieces, whiteTurn);
    }
}

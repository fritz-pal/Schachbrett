package de.hhn.schach;

import de.hhn.schach.utils.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board implements Cloneable {
    private final Game game;
    private final Map<Vec2, Piece> pieces = new HashMap<>();
    private final List<Move> moveHistory = new ArrayList<>();
    private Vec2 enPassant;
    private boolean whiteCastleQ;
    private boolean whiteCastleK;
    private boolean blackCastleQ;
    private boolean blackCastleK;
    private boolean whiteTurn;
    private boolean checkmate = false;
    private boolean stalemate = false;
    private Result result = Result.NOTFINISHED;
    private String fromFen = "";
    private int movesWithoutCaptureOrPawnMove = 0;
    private int moveNumber = 1;

    public Board(Game game) {
        this(game, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public Board(Game game, String fen) {
        this.game = game;
        if (!isValidFen(fen)) fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        if (!fen.equals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")) fromFen = fen;
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
        whiteTurn = fenParts[1].equals("w");

        whiteCastleQ = fenParts[2].contains("Q");
        whiteCastleK = fenParts[2].contains("K");
        blackCastleQ = fenParts[2].contains("q");
        blackCastleK = fenParts[2].contains("k");

        if (fenParts[3].equals("-")) enPassant = null;
        else enPassant = new Vec2(fenParts[3]);
    }

    public Board(Game game, Map<Vec2, Piece> pieces, boolean whiteTurn, Vec2 enPassant, boolean whiteCastleQ, boolean whiteCastleK, boolean blackCastleQ, boolean blackCastleK) {
        this.game = game;
        this.pieces.putAll(pieces);
        this.whiteTurn = whiteTurn;
        this.enPassant = enPassant;
        this.whiteCastleQ = whiteCastleQ;
        this.whiteCastleK = whiteCastleK;
        this.blackCastleQ = blackCastleQ;
        this.blackCastleK = blackCastleK;
    }

    public static boolean isValidFen(String fen) {
        if (fen == null || fen.isEmpty()) return false;
        String[] fenParts = fen.split(" ");
        if (fenParts.length != 6) return false;
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
        if (!fenParts[1].equals("w") && !fenParts[1].equals("b")) return false;
        if (!fenParts[2].matches("[KQkq-]{1,4}")) return false;
        if (!fenParts[3].matches("[a-h][1-8]|-")) return false;
        try {
            Integer.parseInt(fenParts[4]);
            Integer.parseInt(fenParts[5]);
        } catch (NumberFormatException e) {
            return false;
        }
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
        fen.append(" ");
        if (whiteCastleQ || whiteCastleK || blackCastleQ || blackCastleK) {
            if (whiteCastleQ) fen.append("Q");
            if (whiteCastleK) fen.append("K");
            if (blackCastleQ) fen.append("q");
            if (blackCastleK) fen.append("k");
        } else {
            fen.append("-");
        }
        fen.append(" ");
        fen.append(enPassant == null ? "-" : enPassant.toString());
        fen.append(" ");
        fen.append(movesWithoutCaptureOrPawnMove);
        fen.append(" ");
        fen.append(moveNumber);
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

    public String getAllMovesInEngineNotation() {
        StringBuilder moves = new StringBuilder();
        for (Move move : moveHistory) {
            moves.append(" ").append(move.getEngineNotation());
        }
        return moves.toString();
    }

    public void setPiece(Vec2 pos, Piece piece) {
        pieces.put(pos, piece);
    }

    public boolean isLegalMove(Vec2 from, Vec2 to) {
        return getAllLegalMoves(from).contains(to);
    }

    public List<Vec2> getAllLegalMoves(Vec2 pos) {
        List<Vec2> result = new ArrayList<>();
        if (pos == null) return result;
        if (!occupied(pos)) return result;

        if (pos.equals(new Vec2(0, 4)) && whiteTurn && !isInCheck(true)) {
            if (whiteCastleK && !occupied(new Vec2(0, 5)) && !occupied(new Vec2(0, 6)) && !ableToTakeKing(pos, new Vec2(0, 5))) {
                result.add(new Vec2(0, 6));
            }
            if (whiteCastleQ && !occupied(new Vec2(0, 3)) && !occupied(new Vec2(0, 2)) && !occupied(new Vec2(0, 1)) && !ableToTakeKing(pos, new Vec2(0, 3))) {
                result.add(new Vec2(0, 2));
            }
        }
        if (pos.equals(new Vec2(7, 4)) && !whiteTurn && !isInCheck(false)) {
            if (blackCastleK && !occupied(new Vec2(7, 5)) && !occupied(new Vec2(7, 6)) && !ableToTakeKing(pos, new Vec2(7, 5))) {
                result.add(new Vec2(7, 6));
            }
            if (blackCastleQ && !occupied(new Vec2(7, 3)) && !occupied(new Vec2(7, 2)) && !occupied(new Vec2(7, 1)) && !ableToTakeKing(pos, new Vec2(7, 3))) {
                result.add(new Vec2(7, 2));
            }
        }

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
        tempBoard.move(from, to, false);
        return tempBoard.isInCheck(!tempBoard.isWhiteTurn());
    }

    public boolean isCheckmate() {
        return checkmate;
    }

    public boolean isStalemate() {
        return stalemate;
    }

    public boolean checkIfCheckmate() {
        for (Vec2 pos : pieces.keySet()) {
            if (pieces.get(pos).isWhite() == whiteTurn) {
                if (!getAllLegalMoves(pos).isEmpty()) {
                    return false;
                }
            }
        }
        return isInCheck(whiteTurn);
    }

    public boolean checkIfStalemate() {
        for (Vec2 pos : pieces.keySet()) {
            if (pieces.get(pos).isWhite() == whiteTurn) {
                if (!getAllLegalMoves(pos).isEmpty()) {
                    return false;
                }
            }
        }
        return !isInCheck(whiteTurn);
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

    public String getPGN() {
        StringBuilder pgn = new StringBuilder("[Event \"?\"]\n");
        pgn.append("[Site \"Schachbrett.jar\"]\n");
        pgn.append("[Date \"").append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))).append("\"]\n");
        pgn.append("[White \"").append(game.getName(true).isBlank() ? "?" : game.getName(true)).append("\"]\n");
        pgn.append("[Black \"").append(game.getName(false).isBlank() ? "?" : game.getName(false)).append("\"]\n");
        pgn.append("[WhiteElo \"").append(game.getElo(true) == -1 ? "?" : game.getElo(true)).append("\"]\n");
        pgn.append("[BlackElo \"").append(game.getElo(false) == -1 ? "?" : game.getElo(false)).append("\"]\n");
        pgn.append("[Result \"").append(result.getNotation()).append("\"]\n");
        if (!fromFen.isEmpty()) pgn.append("[FEN \"").append(fromFen).append("\"]\n");
        pgn.append("\n");
        int count = 1;
        for (int i = 0; i < moveHistory.size(); i++) {
            if (i % 2 == 0) {
                pgn.append(count).append(". ");
                count++;
            }
            pgn.append(moveHistory.get(i).notation()).append(" ");
        }
        pgn.append(result.getNotation());
        return pgn.toString();
    }

    public String getFromFen() {
        return fromFen;
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
            if (!occupied(move) && !occupied(new Vec2(pos.getX() + (getPiece(pos).isWhite() ? 1 : -1), pos.getY()))) {
                moves.add(move);
            }
        }
        if (pos.getY() - 1 >= 0) {
            move = new Vec2(pos.getX() + (getPiece(pos).isWhite() ? 1 : -1), pos.getY() - 1);
            if ((occupied(move) && getPiece(move).isWhite() != getPiece(pos).isWhite()) || (enPassant != null && enPassant.equals(move))) {
                moves.add(move);
            }
        }

        if (pos.getY() + 1 < 8) {
            move = new Vec2(pos.getX() + (getPiece(pos).isWhite() ? 1 : -1), pos.getY() + 1);
            if ((occupied(move) && getPiece(move).isWhite() != getPiece(pos).isWhite()) || (enPassant != null && enPassant.equals(move))) {
                moves.add(move);
            }
        }


        return moves;
    }

    private Vec2 canOtherPieceGetTo(Vec2 from, Vec2 to, Piece piece) {
        for (Vec2 pos : pieces.keySet()) {
            if (pos.equals(from)) continue;
            Piece p = pieces.get(pos);
            if (!p.equals(piece)) continue;
            if (getAllPseudoLegalMoves(pos).contains(to)) return pos;
        }
        return null;
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

    public Move move(Vec2 from, Vec2 to, boolean isMainBoard) {
        whiteTurn = !whiteTurn;
        Piece piece = pieces.get(from);

        String notation = piece.type().getNotation() + (occupied(to) || to.equals(enPassant) ? "x" : "") + to.getName();
        if (isMainBoard) {
            if (notation.startsWith("P")) {
                notation = notation.substring(1);
                if (notation.startsWith("x")) notation = from.getName().charAt(0) + notation;
            } else {
                Vec2 otherPiece = canOtherPieceGetTo(from, to, piece);
                if (otherPiece != null) {
                    String other = otherPiece.getName();
                    notation = "" + notation.charAt(0) + from.getName().charAt((other.charAt(0) != from.getName().charAt(0)) ? 0 : 1) + notation.substring(1);
                }
            }
        }

        if (piece.type().equals(PieceType.PAWN)) {
            if ((to.getX() == 0 && !piece.isWhite()) || (to.getX() == 7 && piece.isWhite())) {
                piece = new Piece(PieceType.QUEEN, piece.isWhite());
                notation += "=Q";
            }
            if (to.equals(enPassant)) pieces.remove(new Vec2(to.getX() + (piece.isWhite() ? -1 : 1), to.getY()));

            if (from.getX() == 1 && to.getX() == 3) {
                enPassant = new Vec2(2, to.getY());
            } else if (from.getX() == 6 && to.getX() == 4) {
                enPassant = new Vec2(5, to.getY());
            } else {
                enPassant = null;
            }
        } else enPassant = null;

        if (piece.type().equals(PieceType.KING)) {
            if (from.equals(new Vec2(0, 4)) && to.equals(new Vec2(0, 6))) {
                Piece rook = pieces.remove(new Vec2(0, 7));
                pieces.put(new Vec2(0, 5), rook);
                notation = "O-O";
            }
            if (from.equals(new Vec2(0, 4)) && to.equals(new Vec2(0, 2))) {
                Piece rook = pieces.remove(new Vec2(0, 0));
                pieces.put(new Vec2(0, 3), rook);
                notation = "O-O-O";
            }
            if (from.equals(new Vec2(7, 4)) && to.equals(new Vec2(7, 6))) {
                Piece rook = pieces.remove(new Vec2(7, 7));
                pieces.put(new Vec2(7, 5), rook);
                notation = "O-O";
            }
            if (from.equals(new Vec2(7, 4)) && to.equals(new Vec2(7, 2))) {
                Piece rook = pieces.remove(new Vec2(7, 0));
                pieces.put(new Vec2(7, 3), rook);
                notation = "O-O-O";
            }

            if (piece.isWhite()) {
                whiteCastleK = false;
                whiteCastleQ = false;
            } else {
                blackCastleK = false;
                blackCastleQ = false;
            }
        }
        if (from.equals(new Vec2(0, 0))) whiteCastleQ = false;
        if (from.equals(new Vec2(0, 7))) whiteCastleK = false;
        if (from.equals(new Vec2(7, 0))) blackCastleQ = false;
        if (from.equals(new Vec2(7, 7))) blackCastleK = false;

        pieces.remove(from);
        pieces.put(to, piece);


        if (isMainBoard) {
            checkmate = checkIfCheckmate();
            if (!checkmate) stalemate = checkIfStalemate();
            if (checkmate) result = whiteTurn ? Result.BLACKWONBYCHECKMATE : Result.WHITEWONBYCHECKMATE;
            else if (stalemate) result = Result.DRAWBYSTALEMATE;
            if (isInCheck(!piece.isWhite())) {
                if (isCheckmate()) notation += "#";
                else notation += "+";
            }

            if (occupied(to) || piece.type().equals(PieceType.PAWN)) movesWithoutCaptureOrPawnMove = 0;
            else movesWithoutCaptureOrPawnMove++;
            if (!piece.isWhite()) moveNumber++;

            Move move = new Move(from, to, piece, notation);
            moveHistory.add(move);
            return move;
        }
        return null;
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
        return new Board(game, pieces, whiteTurn, enPassant, whiteCastleQ, whiteCastleK, blackCastleQ, blackCastleK);
    }

    public Result getResult() {
        return result;
    }
}

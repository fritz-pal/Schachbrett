package de.hhn.schach;

import java.util.HashMap;
import java.util.Map;

public class Board {
    Map<Vec2, Piece> pieces = new HashMap<>();

    public Board() {
        this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public Board(String fen) {
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
                    pieces.put(new Vec2(7-i, j), new Piece(PieceType.fromNotation(Character.toUpperCase(c)), Character.isUpperCase(c)));
                    j++;
                }
            }
        }
    }

    public static boolean isValidFen(String fen){
        if(fen.isEmpty()) return false;
        String[] fenParts = fen.split(" ");
        if(fenParts.length < 1) return false;
        String[] fenRows = fenParts[0].split("/");
        if(fenRows.length != 8) return false;
        for (int i = 0; i < 8; i++) {
            int number = 0;
            String fenRow = fenRows[i];
            for(int j = 0; j < fenRow.length(); j++){
                char c = fenRow.charAt(j);
                if(Character.isDigit(c)){
                    number += Character.getNumericValue(c);
                }else{
                    number++;
                }
            }
            if(number != 8) return false;
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

    public String movePiece(Vec2 from, Vec2 to) {
        //TODO
        return null;
    }
}

package de.hhn;

public enum PieceType {
    BISHOP,KNIGHT,KING,QUEEN,ROOK,PAWN, INVALID;

    public static PieceType fromString(String s){
        for(PieceType p : PieceType.values()){
            if(p.toString().equals(s)){
                return p;
            }
        }
        return INVALID;
    }

    public static PieceType fromNotation(char c){
        return switch (c) {
            case 'B', 'b' -> BISHOP;
            case 'N', 'n' -> KNIGHT;
            case 'K', 'k' -> KING;
            case 'Q', 'q' -> QUEEN;
            case 'R', 'r' -> ROOK;
            case 'P', 'p' -> PAWN;
            default -> INVALID;
        };
    }
}

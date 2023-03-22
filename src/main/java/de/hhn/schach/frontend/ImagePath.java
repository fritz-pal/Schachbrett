package de.hhn.schach.frontend;


import de.hhn.schach.PieceType;

public final class ImagePath {
    public static String getPath(String file) {
        return "src/main/resources/" + file;
    }

    public static String getPieceImage(PieceType type, boolean isWhite, boolean rotated) {
        String result = type.toString().toLowerCase();
        if (isWhite) result += "_white";

        else result += "_black";

        if (rotated) result += "-";

        else result += "+";

        result += ".svg";
        return getPath(result);
    }
}

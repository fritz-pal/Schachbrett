package de.hhn.schach.frontend;


import de.hhn.schach.PieceType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public final class ImagePath {
    public static Image getResource(String file) {
        Image image = null;
        try {
            image = ImageIO.read(ImagePath.class.getResource("/" + file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public static Image getPieceImage(PieceType type, boolean isWhite, boolean rotated) {
        String result = type.toString().toLowerCase();
        if (isWhite) result += "_white";

        else result += "_black";

        if (rotated) result += "-";

        else result += "+";

        result += ".png";
        return getResource(result);
    }
}

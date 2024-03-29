package de.hhn.schach.frontend;


import de.hhn.schach.utils.PieceType;
import java.awt.Image;
import java.net.URL;
import javax.imageio.ImageIO;

public final class ImagePath {
  public static Image getResource(String file) {
    Image image = null;
    try {
      URL resource = ImagePath.class.getResource("/images/" + file);
      if (resource == null) {
        throw new Exception("Resource not found: " + file);
      }
      image = ImageIO.read(resource);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return image;
  }

  public static Image getPieceImage(PieceType type, boolean isWhite, boolean rotated) {
    String result = type.toString().toLowerCase();
    if (isWhite) {
      result += "_white";
    } else {
      result += "_black";
    }

    if (rotated) {
      result += "-";
    } else {
      result += "+";
    }

    result += ".png";
    return getResource(result);
  }
}
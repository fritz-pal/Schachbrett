package de.hhn;

import javax.swing.*;
import java.awt.*;

public class Tile extends JPanel {
    private int x;
    private int y;
    private int size;
    private boolean isWhite;

    public Tile(int x, int y, int size, boolean isWhite) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.isWhite = isWhite;
        this.setBounds(x, y, size, size);
        this.setBackground(isWhite ? new Color(0xedd6b0) : new Color(0xb88762));
    }
}

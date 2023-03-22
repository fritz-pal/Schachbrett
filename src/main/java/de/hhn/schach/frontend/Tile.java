package de.hhn.schach.frontend;

import de.hhn.schach.Vec2;

import javax.swing.*;
import java.awt.*;

public class Tile extends JButton {
    private final Vec2 pos;
    private final boolean isWhite;
    Window window;

    public Tile(Vec2 pos, Window window) {
        this.window = window;
        this.pos = pos;
        this.isWhite = (pos.getX() + pos.getY()+1) % 2 == 0;
        this.setBounds(pos.getX() * 100, (7 - pos.getY()) * 100, 100, 100);
        this.setBackground(isWhite ? new Color(0xedd6b0) : new Color(0xb88762));
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        window.add(this);
    }


}

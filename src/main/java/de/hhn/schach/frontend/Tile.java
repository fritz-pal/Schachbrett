package de.hhn.schach.frontend;

import de.hhn.schach.Game;
import de.hhn.schach.utils.Piece;
import de.hhn.schach.utils.Vec2;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class Tile extends JButton {
    private final Vec2 pos;
    private final Game game;
    private final boolean isWhite;
    Window window;
    private Piece piece = null;
    private boolean mousePressed = false;
    private boolean hovering = false;
    private boolean selected = false;
    private boolean legalMoveIcon = false;
    private boolean check = false;
    private boolean checkmate = false;

    public Tile(Game game, Vec2 pos, Window window) {
        this.window = window;
        this.pos = pos;
        this.game = game;
        this.isWhite = (pos.getX() + pos.getY() + 1) % 2 == 0;
        this.setBorderPainted(false);
        this.setFocusable(false);
        this.setUI(new MetalButtonUI() {
            protected void paintButtonPressed(Graphics g, AbstractButton b) {
            }
        });
        this.addMouseListener(mouseListener());
    }

    private MouseListener mouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovering = true;
                setBackGroundColor();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovering = false;
                mousePressed = false;
                setBackGroundColor();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1 && hovering) {
                    mousePressed = true;
                    game.getState().onTileClick(pos);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1 && mousePressed) {
                    mousePressed = false;
                }
            }
        };
    }

    private ImageIcon legalMoveIconImage() {
        BufferedImage img = new BufferedImage(this.getWidth(), this.getWidth(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D imgGraphics = img.createGraphics();
        imgGraphics.setColor(new Color(0, 0, 0, 60));
        if (this.piece == null) {
            imgGraphics.fillOval(this.getWidth() / 4, this.getWidth() / 4, this.getWidth() / 2, this.getWidth() / 2);
        } else {
            int borderSize = this.getWidth() / 8;
            imgGraphics.fillRect(borderSize, 0, this.getWidth(), borderSize);
            imgGraphics.fillRect(0, 0, borderSize, this.getWidth() - borderSize);
            imgGraphics.fillRect(this.getWidth() - borderSize, borderSize, borderSize, this.getWidth());
            imgGraphics.fillRect(0, this.getWidth() - borderSize, this.getWidth() - borderSize, borderSize);
        }
        return new ImageIcon(img);
    }

    private ImageIcon getImg() {
        BufferedImage img = new BufferedImage(this.getWidth(), this.getWidth(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D imgGraphics = img.createGraphics();
        if (this.legalMoveIcon) imgGraphics.drawImage(this.legalMoveIconImage().getImage(), 0, 0, null);
        if (piece != null) {
            ImageIcon pieceImg = new ImageIcon(ImagePath.getPieceImage(piece.type(), piece.isWhite(), game.isRotated(piece.isWhite())));
            imgGraphics.drawImage(pieceImg.getImage(), 0, 0, this.getWidth(), this.getWidth(), null);
        }
        if (checkmate) {
            ImageIcon checkmateImg = new ImageIcon(ImagePath.getResource("Checkmate.png"));
            imgGraphics.drawImage(checkmateImg.getImage(), 0, 0, this.getWidth(), this.getWidth(), null);
        }
        return new ImageIcon(img);
    }

    public Vec2 getPos() {
        return pos;
    }

    public void update(Piece piece, boolean legalMoveIcon, boolean check, boolean checkmate) {
        this.piece = piece;
        this.selected = pos.equals(game.getSelectedTile());
        this.legalMoveIcon = legalMoveIcon;
        this.check = check;
        this.checkmate = checkmate;
        this.setBackGroundColor();
        this.setIcon(getImg());
    }

    private void setBackGroundColor() {
        Color color;
        if (this.check) {
            color = new Color(0xfa362c);
        } else if (this.selected) {
            color = isWhite ? new Color(0xf7eb58) : new Color(0xdcc431);
        } else if (this.hovering) {
            color = isWhite ? new Color(0xF0DD8E) : new Color(0xC39960);
        } else {
            color = isWhite ? new Color(0xedd6b0) : new Color(0xb88762);
        }
        this.setBackground(color);
    }
}
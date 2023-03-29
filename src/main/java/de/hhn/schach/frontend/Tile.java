package de.hhn.schach.frontend;

import de.hhn.schach.Piece;
import de.hhn.schach.Vec2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class Tile extends JButton {
    private final Vec2 pos;
    private final boolean isWhite;
    private final Point translatedPos;
    Window window;
    private Piece piece = null;
    private boolean mousePressed = false;
    private boolean hovering = false;
    private boolean selected = false;
    private boolean legalMoveIcon = false;

    public Tile(Vec2 pos, Window window) {
        this.window = window;
        this.pos = pos;
        this.isWhite = (pos.getX() + pos.getY() + 1) % 2 == 0;
        translatedPos = new Point((!window.isRotatedBoard() ? pos.getY() : (7 - pos.getY())) * tileSize(), (window.isRotatedBoard() ? pos.getX() : (7 - pos.getX())) * tileSize());
        this.setBounds(translatedPos.x, translatedPos.y, tileSize(), tileSize());
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.addMouseListener(mouseListener());
        window.add(this);
    }

    private MouseListener mouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovering = true;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovering = false;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1 && hovering) {
                    mousePressed = true;
                    window.game.getState().onTileClick(pos);
                    System.out.println("Clicked " + pos);
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

    private int tileSize() {
        return Math.min(window.getContentPane().getHeight(), window.getContentPane().getWidth()) / 8;
    }

    private ImageIcon emptyTileImage() {
        BufferedImage img = new BufferedImage(tileSize(), tileSize(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D imgGraphics = img.createGraphics();
        if (this.mousePressed || this.selected) {
            imgGraphics.setColor(isWhite ? new Color(0xf7eb58) : new Color(0xdcc431));
            imgGraphics.fillRect(0, 0, tileSize(), tileSize());
        } else if (this.hovering) {
            imgGraphics.setColor(new Color(0xf7, 0xeb, 0x58, 60));
            imgGraphics.fillRect(0, 0, tileSize(), tileSize());
        }
        if (this.legalMoveIcon) {
            imgGraphics.setColor(new Color(0, 0, 0, 60));
            if (this.piece == null) {
                imgGraphics.fillOval(tileSize() / 4, tileSize() / 4, tileSize() / 2, tileSize() / 2);
            } else {
                int borderSize = tileSize() / 8;
                imgGraphics.fillRect(borderSize, 0, tileSize(), borderSize);
                imgGraphics.fillRect(0, 0, borderSize, tileSize() - borderSize);
                imgGraphics.fillRect(tileSize() - borderSize, borderSize, borderSize, tileSize());
                imgGraphics.fillRect(0, tileSize() - borderSize, tileSize() - borderSize, borderSize);
            }
        }
        return new ImageIcon(img);
    }

    private void calcPosition() {
        if (window.getContentPane().getHeight() > window.getContentPane().getWidth()) {
            translatedPos.x = (!window.isRotatedBoard() ? pos.getY() : (7 - pos.getY())) * tileSize();
            translatedPos.y = (window.isRotatedBoard() ? pos.getX() : (7 - pos.getX())) * tileSize() + ((window.getContentPane().getHeight() - window.getContentPane().getWidth()) / 2);
        }
        if (window.getContentPane().getHeight() < window.getContentPane().getWidth()) {
            translatedPos.x = (!window.isRotatedBoard() ? pos.getY() : (7 - pos.getY())) * tileSize() + ((window.getContentPane().getWidth() - window.getContentPane().getHeight()) / 2);
            translatedPos.y = (window.isRotatedBoard() ? pos.getX() : (7 - pos.getX())) * tileSize();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        this.setIcon(this.getImg());
        calcPosition();
        this.setBounds(translatedPos.x, translatedPos.y, tileSize(), tileSize());
    }

    private ImageIcon getImg() {
        BufferedImage img = new BufferedImage(tileSize(), tileSize(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D imgGraphics = img.createGraphics();
        imgGraphics.setColor(isWhite ? new Color(0xedd6b0) : new Color(0xb88762));
        imgGraphics.fillRect(0, 0, tileSize(), tileSize());

        imgGraphics.drawImage(this.emptyTileImage().getImage(), 0, 0, null);
        if (piece != null) {
            boolean rotated = !piece.isWhite() && window.isRotatedPieces() && !window.isRotatedBoard() || piece.isWhite() && window.isRotatedPieces() && window.isRotatedBoard();
            ImageIcon pieceImg = new ImageIcon(ImagePath.getPieceImage(piece.type(), piece.isWhite(), rotated));
            imgGraphics.drawImage(pieceImg.getImage(), 0, 0, tileSize(), tileSize(), null);
        }
        return new ImageIcon(img);
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Vec2 getPos() {
        return pos;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setLegalMove(boolean legalMoveIcon) {
        this.legalMoveIcon = legalMoveIcon;
    }
}

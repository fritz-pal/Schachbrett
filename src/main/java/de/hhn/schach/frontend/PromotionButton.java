package de.hhn.schach.frontend;

import de.hhn.schach.Board;
import de.hhn.schach.Game;
import de.hhn.schach.stateMachine.PieceSelectedAgainstEngineState;
import de.hhn.schach.stateMachine.PieceSelectedState;
import de.hhn.schach.stateMachine.TurnState;
import de.hhn.schach.utils.PieceType;
import de.hhn.schach.utils.Vec2;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class PromotionButton extends JButton {
    public PromotionButton(Game game, boolean white, Vec2 position, PieceType type, PromotionWindow parent) {
        int width = game.getWindow().getTile(position).getWidth();
        this.setBackground(new Color(!white ? 0xf2f3f5 : 0x312e2b));
        this.setBorderPainted(false);
        this.setFocusable(false);
        this.setUI(new MetalButtonUI() {
            protected void paintButtonPressed(Graphics g, AbstractButton b) {
            }
        });
        BufferedImage img = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
        Graphics2D imgGraphics = img.createGraphics();
        imgGraphics.drawImage(new ImageIcon(ImagePath.getPieceImage(type, white, game.isRotated(white))).getImage(), 0, 0, width, width, null);
        this.setIcon(new ImageIcon(img));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Board board = game.getMainBoard();
                board.move(game.getSelectedTile(), position, true, type);
                if (board.isCheckmate() || board.isStalemate()) {
                    game.endGame();
                } else {
                    game.setSelectedTile(null);
                    if (game.getState() instanceof PieceSelectedState) {
                        game.changeState(new TurnState(game));
                    } else if (game.getState() instanceof PieceSelectedAgainstEngineState) {
                        game.startEngine();
                    }
                }
                parent.dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                PromotionButton.this.setBackground(new Color(!white ? 0xe3e5e8 : 0x514e4b));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                PromotionButton.this.setBackground(new Color(!white ? 0xf2f3f5 : 0x312e2b));
            }
        });
        parent.add(this);
    }
}
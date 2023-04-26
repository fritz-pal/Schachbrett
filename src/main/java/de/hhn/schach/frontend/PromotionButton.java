package de.hhn.schach.frontend;

import de.hhn.schach.Board;
import de.hhn.schach.Game;
import de.hhn.schach.stateMachine.PieceSelectedAgainstEngineState;
import de.hhn.schach.stateMachine.PieceSelectedState;
import de.hhn.schach.stateMachine.TurnState;
import de.hhn.schach.utils.PieceType;
import de.hhn.schach.utils.Vec2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PromotionButton extends JButton {
    public PromotionButton(Game game, boolean white, Vec2 position, PieceType type, PromotionWindow parent) {
        this.setBackground(new Color(!white ? 0xf2f3f5 : 0x312e2b));
        this.setBorderPainted(false);
        this.setFocusPainted(false);
        this.setFocusable(false);
        this.setIcon(new ImageIcon(ImagePath.getPieceImage(type, white, game.isRotated(white))));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Board board = game.getMainBoard();
                board.move(game.getSelectedTile(), position, true, type);
                if (board.isCheckmate() || board.isStalemate()) game.endGame();
                game.setSelectedTile(null);
                if (game.getState() instanceof PieceSelectedState) {
                    game.changeState(new TurnState(game));
                } else if (game.getState() instanceof PieceSelectedAgainstEngineState) {
                    game.startEngine();
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
package de.hhn.schach.frontend;

import de.hhn.schach.Board;
import de.hhn.schach.Game;
import de.hhn.schach.stateMachine.PieceSelectedState;
import de.hhn.schach.stateMachine.TurnAgainstEngineState;
import de.hhn.schach.stateMachine.TurnState;
import de.hhn.schach.utils.PieceType;
import de.hhn.schach.utils.Vec2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PromotionButton extends JButton {
    public PromotionButton(Game game, boolean white, Vec2 position, PieceType type, PromotionWindow parent) {
        JButton button = new JButton();
        button.setBackground(new Color(0x312e2b));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setIcon(new ImageIcon(ImagePath.getPieceImage(type, white, false)));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Board board = game.getMainBoard();
                board.move(game.getSelectedTile(), position, true, type);
                if (board.isCheckmate() || board.isStalemate()) game.endGame();
                game.setSelectedTile(null);
                if (game.getState() instanceof PieceSelectedState) {
                    game.changeState(new TurnState(game));
                } else {
                    game.startEngine();
                    game.changeState(new TurnAgainstEngineState(game));
                }
                parent.dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0x514e4b));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0x312e2b));
            }
        });
        parent.add(button);
    }
}

package de.hhn.schach.frontend;

import de.hhn.schach.Game;
import de.hhn.schach.stateMachine.PieceSelectedAgainstEngineState;
import de.hhn.schach.stateMachine.PieceSelectedState;
import de.hhn.schach.stateMachine.TurnAgainstEngineState;
import de.hhn.schach.stateMachine.TurnState;
import de.hhn.schach.utils.PieceType;
import de.hhn.schach.utils.Vec2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class PromotionWindow extends JFrame {
    public PromotionWindow(Game game, boolean white, Vec2 position) {
        Tile tile = game.getWindow().getTile(position);
        this.setSize(tile.getWidth(), tile.getWidth() * 4);
        this.setResizable(false);
        this.setUndecorated(true);
        this.setLocationRelativeTo(tile);
        this.setAlwaysOnTop(true);
        this.setLayout(new GridLayout(4, 1));
        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                game.setSelectedTile(null);
                if (game.getState() instanceof PieceSelectedState) {
                    game.changeState(new TurnState(game));
                } else if (game.getState() instanceof PieceSelectedAgainstEngineState) {
                    game.changeState(new TurnAgainstEngineState(game));
                }
                PromotionWindow.this.dispose();
            }
        });
        if (game.isRotated(white)) {
            for (int i = 3; i >= 0; i--) {
                new PromotionButton(game, white, position, PieceType.values()[i], this);
            }
        } else {
            for (int i = 0; i < 4; i++) {
                new PromotionButton(game, white, position, PieceType.values()[i], this);
            }
        }
        this.setVisible(true);
    }
}
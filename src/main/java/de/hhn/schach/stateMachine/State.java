package de.hhn.schach.stateMachine;

import de.hhn.schach.utils.Vec2;

public interface State {
    void onTileClick(Vec2 pos);
}

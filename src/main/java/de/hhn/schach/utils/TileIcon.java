package de.hhn.schach.utils;

public enum TileIcon {
    CHECKMATE("checkmate_icon.png"),
    DRAW("draw_icon.png"),
    WIN("win_icon.png");

    private final String path;

    TileIcon(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

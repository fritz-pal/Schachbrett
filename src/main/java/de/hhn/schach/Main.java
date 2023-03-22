package de.hhn.schach;

import de.hhn.schach.frontend.Window;

public class Main {
    public static void main(String[] args) {
        Window window = new Window();
        window.setVisible(true);
        Board test = new Board();
        System.out.println(test + "\n" + test.getFen());
        window.update(test);
        System.out.println(test.getPiece(new Vec2("a1")));

    }
}

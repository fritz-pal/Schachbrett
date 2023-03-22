package de.hhn.schach;

import de.hhn.schach.frontend.Window;

public class Main {
    public static void main(String[] args) {
        new Window().setVisible(true);
        Board test = new Board();
        System.out.println(test + "\n" + test.getFen());
    }
}

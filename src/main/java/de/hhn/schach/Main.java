package de.hhn.schach;

import de.hhn.schach.frontend.Window;

public class Main {
    public static void main(String[] args) {
        Window window = new Window();
        window.setVisible(true);
        Board test = new Board("r3qb1r/pppk2p1/2n2n1p/1B1b1pN1/3PpQ2/1P4P1/PB3P1P/RN1K3R b - - 1 17");
        System.out.println(test + "\n" + test.getFen());
        window.update(test);
    }
}

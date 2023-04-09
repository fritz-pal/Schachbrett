package de.hhn.schach;

import de.hhn.schach.frontend.ImagePath;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Sound {
    public static void play(Move move) {
        String fileName = "move.wav";
        if (move.isCapture())
            fileName = "capture.wav";
        if (move.isPromotion())
            fileName = "promotion.wav";
        if (move.isCastling())
            fileName = "castling.wav";
        if (move.isCheck())
            fileName = "check.wav";
        if (move.isCheckmate())
            fileName = "checkmate.wav";

        try {
            URL resource = ImagePath.class.getResource("/" + fileName);
            File soundFile = new File(resource.toURI());

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

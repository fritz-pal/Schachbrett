package de.hhn.schach.frontend;

import de.hhn.schach.utils.Move;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

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
            InputStream soundStream = Sound.class.getResourceAsStream("/sound/" + fileName);
            if (soundStream == null) throw new IOException("Sound file not found: " + fileName);
            InputStream bufferedIn = new BufferedInputStream(soundStream);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}

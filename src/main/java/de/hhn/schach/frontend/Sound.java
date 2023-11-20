package de.hhn.schach.frontend;

import de.hhn.schach.utils.Move;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
  public static void endSound() {
    play("checkmate.wav");
  }

  public static void play(String fileName) {
    try {
      InputStream soundStream = Sound.class.getResourceAsStream("/sound/" + fileName);
      if (soundStream == null) {
        throw new IOException("Sound file not found: " + fileName);
      }
      InputStream bufferedIn = new BufferedInputStream(soundStream);
      AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);
      Clip clip = AudioSystem.getClip();
      clip.open(audioIn);
      clip.start();
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
      e.printStackTrace();
    }
  }

  public static void moveSound(Move move) {
    String fileName = "move.wav";
    if (move.isCapture()) {
      fileName = "capture.wav";
    }
    if (move.isPromotion()) {
      fileName = "promotion.wav";
    }
    if (move.isCastling()) {
      fileName = "castling.wav";
    }
    if (move.isCheck()) {
      fileName = "check.wav";
    }

    play(fileName);
  }
}
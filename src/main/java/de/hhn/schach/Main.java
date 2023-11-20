package de.hhn.schach;

import de.hhn.schach.frontend.StartScreen;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Main {
  public static String enginePath = "";
  public static int highScore = -1;

  public static void main(String[] args) {
    readData();
    new StartScreen();
  }

  public static void readData() {
    String appDirectory =
        System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Roaming" +
            File.separator + "Schachbrett";
    File directory = new File(appDirectory);
    if (!directory.exists()) {
      saveData();
    } else {
      try {
        File file = new File(appDirectory + File.separator + "data.txt");
        if (!file.exists()) {
          saveData();
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        enginePath = reader.readLine();
        try {
          String line = reader.readLine();
          if (line != null) {
            highScore = Integer.parseInt(line);
          }
        } catch (NumberFormatException e) {
          highScore = 0;
        }
        reader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void saveData() {
    System.out.println("Saving data: " + enginePath + " " + highScore);
    String appDirectory =
        System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Roaming" +
            File.separator + "Schachbrett";
    File directory = new File(appDirectory);
    if (!directory.exists()) {
      if (!directory.mkdir()) {
        throw new RuntimeException("Could not create directory " + appDirectory);
      }
    }
    try {
      File file = new File(appDirectory + File.separator + "data.txt");
      if (!file.exists()) {
        file.createNewFile();
      }
      Files.writeString(file.toPath(), enginePath + "\n" + highScore,
          StandardCharsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
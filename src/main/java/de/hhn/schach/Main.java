package de.hhn.schach;

import de.hhn.schach.frontend.StartScreen;

import java.io.*;

public class Main {
    public static String enginePath;

    public static void main(String[] args) {
        readEnginePath();
        new StartScreen();
    }

    public static void readEnginePath() {
        String appDirectory = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Roaming" + File.separator + "Schachbrett";
        File directory = new File(appDirectory);
        if (!directory.exists()) {
            saveEnginePath();
        } else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(appDirectory + File.separator + "enginePath.txt"));
                enginePath = reader.readLine();
                if (enginePath == null) enginePath = "";
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveEnginePath() {
        String appDirectory = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Roaming" + File.separator + "Schachbrett";
        File directory = new File(appDirectory);
        if (!directory.exists()) {
            directory.mkdir();
        }
        if (enginePath == null) enginePath = "";
        try {
            FileWriter writer = new FileWriter(appDirectory + File.separator + "enginePath.txt");
            writer.write(enginePath);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

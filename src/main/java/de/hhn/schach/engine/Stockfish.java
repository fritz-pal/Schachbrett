package de.hhn.schach.engine;

import de.hhn.schach.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Stockfish extends Thread {
    Process process;

    public void run() {
        try {
            process = new ProcessBuilder(Main.enginePath).start();
            int errorCode = process.waitFor();
            if (errorCode != 0) {
                try (BufferedReader reader = process.errorReader(StandardCharsets.UTF_8)) {
                    throw new RuntimeException(String.format("Program execution failed (code %d): %s", errorCode,
                            reader.lines().collect(Collectors.joining())));
                }
            } else {
                System.out.println("Process terminated successfully.");
                System.exit(0);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not invoke program.", e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Could not wait for process exit.", e);
        }
    }
}

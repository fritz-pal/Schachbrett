package de.hhn.schach.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandListener extends Thread {
    UCIProtocol uci;
    Process process;

    public CommandListener(UCIProtocol uci, Process process) {
        this.uci = uci;
        this.process = process;
        start();
    }

    @Override
    public void run() {
        try {
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(" ");
                String command = split[0];
                if (split.length == 1) {
                    uci.receiveCommand(command, new String[0]);
                    continue;
                }
                String[] params = new String[split.length - 1];
                System.arraycopy(split, 1, params, 0, split.length - 1);
                uci.receiveCommand(command, params);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

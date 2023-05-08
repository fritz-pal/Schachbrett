package de.hhn.schach.frontend;

import de.hhn.schach.utils.Result;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class EvalBar extends JPanel {
    private final JPanel bar = new JPanel();
    private final Window window;
    private final JLabel whiteLabel = new JLabel("0.0");
    private final JLabel blackLabel = new JLabel("0.0");
    private int interpolation = 0;

    public EvalBar(Window window) {
        this.window = window;
        this.setBounds((window.getContentPane().getWidth() - window.getBoardSize()) / 2 - 50, (window.getContentPane().getHeight() - window.getBoardSize()) / 2, 40, window.getBoardSize());
        this.setBackground(Color.WHITE);
        this.setLayout(null);
        window.add(this);

        bar.setBounds(0, 0, 40, this.getHeight() / 2);
        bar.setBackground(Color.BLACK);
        this.add(bar, 0);

        whiteLabel.setHorizontalAlignment(SwingConstants.CENTER);
        whiteLabel.setVerticalAlignment(SwingConstants.CENTER);
        whiteLabel.setFont(new Font("Arial Black", Font.PLAIN, 14));
        whiteLabel.setForeground(Color.BLACK);
        whiteLabel.setBounds(0, this.getHeight() - 40, 40, 40);
        whiteLabel.setVisible(true);
        this.add(whiteLabel, 1);

        blackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        blackLabel.setVerticalAlignment(SwingConstants.CENTER);
        blackLabel.setFont(new Font("Arial Black", Font.PLAIN, 14));
        blackLabel.setForeground(Color.WHITE);
        blackLabel.setBounds(0, 0, 40, 40);
        blackLabel.setVisible(true);
        bar.add(blackLabel, 0);

        new Timer(10, event -> bar.setBounds(0, 0, 40, (interpolation + bar.getHeight() * 3) / 4)).start();
    }

    public void setEval(int cp, int mate) {
        if (mate != 0) {
            if (mate < 0) {
                interpolation = this.getHeight() + 3;
                blackLabel.setText("-M" + Math.abs(mate));
                whiteLabel.setVisible(false);
                blackLabel.setVisible(true);
            } else {
                interpolation = 0;
                whiteLabel.setText("M" + (mate - 1));
                blackLabel.setVisible(false);
                whiteLabel.setVisible(true);
            }
            return;
        }
        if (cp < 1000 && cp > -1000) {
            whiteLabel.setText(new DecimalFormat("0.0", new DecimalFormatSymbols(Locale.ENGLISH)).format(cp / 100.0));
            blackLabel.setText(new DecimalFormat("0.0", new DecimalFormatSymbols(Locale.ENGLISH)).format(cp / 100.0));
        } else {
            blackLabel.setText(String.valueOf((int) (cp / 100f)));
            whiteLabel.setText(String.valueOf((int) (cp / 100f)));
        }
        if (cp > 0) {
            blackLabel.setVisible(false);
            whiteLabel.setVisible(true);
        } else {
            whiteLabel.setVisible(false);
            blackLabel.setVisible(true);
        }
        interpolation = (int) (this.getHeight() / 2.0 * (1 - Math.tanh(cp / 1000.0)));
    }

    public void setEndResult(Result result) {
        if (result.whiteWon()) {
            whiteLabel.setText("1-0");
            blackLabel.setVisible(false);
            whiteLabel.setVisible(true);
        } else if (result.blackWon()) {
            blackLabel.setText("0-1");
            whiteLabel.setVisible(false);
            blackLabel.setVisible(true);
        }
    }
}

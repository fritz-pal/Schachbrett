package de.hhn.schach.frontend;

import de.hhn.schach.Board;
import de.hhn.schach.Game;
import de.hhn.schach.Main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class StartScreen extends JFrame {
  private int checkboxNum = 0;

  public StartScreen() {
    super("Chess");
    this.setLayout(null);
    this.getContentPane().setBackground(new Color(0x312e2b));
    this.setSize(500, 600);
    this.getContentPane().setPreferredSize(new Dimension(500, 600));
    this.pack();
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.setIconImage(new ImageIcon(ImagePath.getResource("icon.png")).getImage());

    JLabel titleLabel = new JLabel("Chess");
    titleLabel.setBounds(200, 10, 100, 50);
    titleLabel.setFont(new Font("Arial Black", Font.PLAIN, 24));
    titleLabel.setForeground(Color.WHITE);
    titleLabel.setHorizontalAlignment(JLabel.CENTER);
    this.getContentPane().add(titleLabel, 0);

    JLabel imageLabel = new JLabel(new ImageIcon(ImagePath.getResource("thumbnail.png")));
    imageLabel.setBounds(100, 75, 300, 225);
    this.add(imageLabel);

    JLabel difficultyLabel = new JLabel("Difficulty: 1350");
    difficultyLabel.setBounds(250, 310, 200, 50);
    difficultyLabel.setFont(new Font("Arial Black", Font.PLAIN, 14));
    difficultyLabel.setForeground(Color.WHITE);
    difficultyLabel.setHorizontalAlignment(JLabel.CENTER);
    difficultyLabel.setVisible(false);
    this.getContentPane().add(difficultyLabel, 1);

    JSlider difficultySlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 0);
    difficultySlider.setBounds(250, 310, 200, 20);
    difficultySlider.setBackground(new Color(0x312e2b));
    difficultySlider.addChangeListener(
        e -> difficultyLabel.setText("Difficulty: " + Game.map(difficultySlider.getValue())));
    difficultySlider.setVisible(false);
    this.getContentPane().add(difficultySlider, 0);

    JCheckBox rotatedPieces = makeCheckbox("Rotated pieces");
    JCheckBox rotatedBoard = makeCheckbox("Rotated board");
    JCheckBox singlePlayer = makeCheckbox("Singleplayer");
    JCheckBox evalBar = makeCheckbox("Eval bar");
    evalBar.setVisible(false);
    singlePlayer.addActionListener(e -> {
      difficultySlider.setVisible(singlePlayer.isSelected());
      difficultyLabel.setVisible(singlePlayer.isSelected());
      evalBar.setVisible(singlePlayer.isSelected());
    });

    JTextField fenInput = new JTextField("");
    fenInput.setToolTipText("Custom Position (Fen)");
    fenInput.setBounds(50, 490, 400, 25);
    fenInput.setBackground(new Color(0x514e4b));
    fenInput.setForeground(Color.WHITE);
    fenInput.setFont(new Font("Arial", Font.PLAIN, 14));
    this.getContentPane().add(fenInput, 0);

    JTextField whitePlayerName = new JTextField("");
    whitePlayerName.setToolTipText("White Player Name");
    whitePlayerName.setBounds(300, 350, 95, 25);
    whitePlayerName.setBackground(new Color(0x514e4b));
    whitePlayerName.setForeground(Color.WHITE);
    whitePlayerName.setFont(new Font("Arial", Font.PLAIN, 14));
    this.getContentPane().add(whitePlayerName, 0);

    JTextField blackPlayerName = new JTextField("");
    blackPlayerName.setToolTipText("Black Player Name");
    blackPlayerName.setBounds(300, 400, 95, 25);
    blackPlayerName.setBackground(new Color(0x514e4b));
    blackPlayerName.setForeground(Color.WHITE);
    blackPlayerName.setFont(new Font("Arial", Font.PLAIN, 14));
    this.getContentPane().add(blackPlayerName, 0);

    JLabel whitePlayerLabel = new JLabel("White");
    whitePlayerLabel.setBounds(250, 350, 50, 25);
    whitePlayerLabel.setFont(new Font("Arial Black", Font.PLAIN, 14));
    whitePlayerLabel.setForeground(Color.WHITE);
    this.getContentPane().add(whitePlayerLabel, 0);

    JLabel blackPlayerLabel = new JLabel("Black");
    blackPlayerLabel.setBounds(250, 400, 50, 25);
    blackPlayerLabel.setFont(new Font("Arial Black", Font.PLAIN, 14));
    blackPlayerLabel.setForeground(Color.WHITE);
    this.getContentPane().add(blackPlayerLabel, 0);

    JTextField whiteElo = new JTextField(4);
    whiteElo.setToolTipText("White Player Elo");
    whiteElo.setBounds(400, 350, 40, 25);
    whiteElo.setBackground(new Color(0x514e4b));
    whiteElo.setForeground(Color.WHITE);
    whiteElo.setFont(new Font("Arial", Font.PLAIN, 14));
    this.getContentPane().add(whiteElo, 0);

    JTextField blackElo = new JTextField(4);
    blackElo.setToolTipText("Black Player Elo");
    blackElo.setBounds(400, 400, 40, 25);
    blackElo.setBackground(new Color(0x514e4b));
    blackElo.setForeground(Color.WHITE);
    blackElo.setFont(new Font("Arial", Font.PLAIN, 14));
    this.getContentPane().add(blackElo, 0);

    JLabel fenLabel = new JLabel("FEN");
    fenLabel.setBounds(50, 465, 50, 25);
    fenLabel.setFont(new Font("Arial Black", Font.PLAIN, 14));
    fenLabel.setForeground(Color.WHITE);
    this.getContentPane().add(fenLabel, 0);

    JLabel errorLabel = new JLabel("");
    errorLabel.setBounds(50, 500, 400, 25);
    errorLabel.setFont(new Font("Arial Black", Font.PLAIN, 14));
    errorLabel.setForeground(new Color(0xfa362c));
    this.getContentPane().add(errorLabel, 0);

    JButton engineButton = new JButton("Engine");
    engineButton.setBounds(10, 10, 90, 25);
    engineButton.setFont(new Font("Arial Black", Font.PLAIN, 14));
    engineButton.setForeground(Color.WHITE);
    engineButton.setBackground(new Color(0x514e4b));
    engineButton.setFocusable(false);
    engineButton.addActionListener(e -> new EngineSelector(this));
    this.getContentPane().add(engineButton, 0);

    JButton startButton = new JButton("Start Game");
    startButton.setBounds(250, 525, 200, 50);
    startButton.setFont(new Font("Arial Black", Font.PLAIN, 14));
    startButton.setForeground(Color.WHITE);
    startButton.setBackground(new Color(0x514e4b));
    startButton.setFocusable(false);
    startButton.addActionListener(e -> {
      try {
        if (singlePlayer.isSelected() && (Main.enginePath == null || Main.enginePath.isBlank())) {
          errorLabel.setText("No engine detected!");
          return;
        }
        boolean validFen = Board.isValidFen(fenInput.getText());
        if (validFen || fenInput.getText().isEmpty()) {
          new Game(rotatedPieces.isSelected(),
              rotatedBoard.isSelected(),
              singlePlayer.isSelected(),
              fenInput.getText(),
              whitePlayerName.getText(),
              blackPlayerName.getText(),
              whiteElo.getText().isEmpty() ? -1 : Integer.parseInt(whiteElo.getText()),
              blackElo.getText().isEmpty() ? -1 : Integer.parseInt(blackElo.getText()),
              singlePlayer.isSelected() ? difficultySlider.getValue() : -1,
              singlePlayer.isSelected() && evalBar.isSelected());
        } else {
          errorLabel.setText("Invalid FEN!");
        }
      } catch (NumberFormatException exception) {
        errorLabel.setText("Invalid Elo!");
      }
    });
    this.getContentPane().add(startButton, 0);

    this.setVisible(true);
  }

  public JCheckBox makeCheckbox(String name) {
    JCheckBox checkbox = new JCheckBox(name);
    checkbox.setBounds(50, 300 + checkboxNum * 40, 150, 40);
    checkbox.setFont(new Font("Arial Black", Font.PLAIN, 14));
    checkbox.setForeground(Color.WHITE);
    checkbox.setFocusable(false);
    checkbox.setBackground(new Color(0x312e2b));
    this.getContentPane().add(checkbox, 0);
    checkboxNum++;
    return checkbox;
  }
}
package de.hhn.schach.frontend;

import de.hhn.schach.Main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class EngineSelector extends JFrame {
  JTextField pathField = new JTextField();

  public EngineSelector(JFrame parent) {
    super("Engine Selector");
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setSize(600, 100);
    this.setResizable(false);
    this.getContentPane().setPreferredSize(new Dimension(600, 100));
    this.pack();
    this.setLayout(null);
    this.setLocationRelativeTo(parent);
    this.getContentPane().setBackground(new Color(0x312e2b));
    this.setIconImage(new ImageIcon(ImagePath.getResource("icon.png")).getImage());

    JLabel fenLabel = new JLabel("Current Path:");
    fenLabel.setBounds(10, 0, 200, 25);
    fenLabel.setFont(new Font("Arial Black", Font.PLAIN, 14));
    fenLabel.setForeground(Color.WHITE);
    this.add(fenLabel);

    pathField.setBounds(10, 25, 580, 25);
    pathField.setBackground(new Color(0x514e4b));
    pathField.setForeground(Color.WHITE);
    pathField.setFont(new Font("Arial", Font.PLAIN, 14));
    pathField.setText(Main.enginePath);
    this.add(pathField);

    JButton saveButton = new JButton("Save");
    saveButton.setBounds(50, 60, 80, 30);
    saveButton.setFont(new Font("Arial Black", Font.PLAIN, 14));
    saveButton.setForeground(Color.WHITE);
    saveButton.setBackground(new Color(0x514e4b));
    saveButton.setFocusable(false);
    saveButton.addActionListener(e -> save());
    this.add(saveButton);

    JButton fileChooserButton = new JButton("Choose File");
    fileChooserButton.setBounds(150, 60, 140, 30);
    fileChooserButton.setFont(new Font("Arial Black", Font.PLAIN, 14));
    fileChooserButton.setForeground(Color.WHITE);
    fileChooserButton.setBackground(new Color(0x514e4b));
    fileChooserButton.setFocusable(false);
    fileChooserButton.addActionListener(e -> fileChooser());
    this.add(fileChooserButton);
    this.setVisible(true);
  }

  private void save() {
    Main.enginePath = pathField.getText();
    Main.saveEnginePath();
    this.dispose();
  }

  private void fileChooser() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Select Engine");
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.setFileFilter(new FileNameExtensionFilter("Executable", "exe"));
    int returnValue = fileChooser.showOpenDialog(this);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      pathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
    }
  }
}
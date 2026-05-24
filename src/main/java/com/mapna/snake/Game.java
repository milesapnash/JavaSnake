package com.mapna.snake;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Game {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrame window = new Window();
      window.setVisible(true);
    });
  }
}

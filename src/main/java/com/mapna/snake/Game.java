package com.mapna.snake;

import javax.swing.*;

public class Game {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrame window = new Window();
      window.setVisible(true);
    });
  }
}

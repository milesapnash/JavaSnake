package com.mapna.snake;

import java.awt.Toolkit;
import java.net.URL;
import java.nio.file.Path;
import javax.swing.JFrame;

public class Window extends JFrame {
  public Window() {
    super("JavaSnake");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setIcon();

    add(new Board());
    pack();
    setResizable(false);
    setLocationRelativeTo(null);
  }

  private void setIcon() {
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    URL iconUrl = getClass().getResource("/images/icon.png");
    if (iconUrl != null) {
      setIconImage(toolkit.getImage(iconUrl));
      return;
    }
    Path devIconPath = Path.of("src", "main", "resources", "images", "icon.png");
    setIconImage(toolkit.getImage(devIconPath.toAbsolutePath().toString()));
  }
}

import javax.swing.*;
import java.awt.*;

/** A window to hold the game board inside. */
public class Window extends JFrame {
  public Window() {
    setTitle("JavaSnake");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setIconImage(Toolkit.getDefaultToolkit().getImage("images/icon.png"));

    add(new Board());
    pack();
    setResizable(false);
    setLocationRelativeTo(null);
  }
}

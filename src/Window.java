import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
  public Window() {
    setTitle("JavaSnake");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));

    add(new Board());
    pack();
    setResizable(false);
    setLocationRelativeTo(null);
  }
}

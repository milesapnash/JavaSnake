import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {
  private Game() {
    initFrame();
  }

  public static void main(String[] args) {
    JFrame game = new Game();
    game.setVisible(true);
  }

  private void initFrame() {
    setTitle("JavaSnake");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));

    add(new Board());
    pack();
    setResizable(false);
    setLocationRelativeTo(null);
  }
}

import javax.swing.*;

public class Snake extends JFrame {
  private Snake() {
    initFrame();
  }

  public static void main(String[] args) {
    JFrame game = new Snake();
    game.setVisible(true);
  }

  private void initFrame() {
    setTitle("JavaSnake");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    add(new Gameboard());
    pack();
    setResizable(false);
    setLocationRelativeTo(null);
  }
}

import javax.swing.*;

public class Game {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      final JFrame window = new Window();
      window.setVisible(true);
    });
  }
}

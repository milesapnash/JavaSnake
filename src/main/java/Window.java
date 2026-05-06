import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.nio.file.Path;

/** A window to hold the game board inside. */
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
    final Toolkit toolkit = Toolkit.getDefaultToolkit();
    final URL iconUrl = getClass().getResource("/images/icon.png");
    if (iconUrl != null) {
      setIconImage(toolkit.getImage(iconUrl));
      return;
    }
    final Path devIconPath = Path.of("src", "main", "resources", "images", "icon.png");
    setIconImage(toolkit.getImage(devIconPath.toAbsolutePath().toString()));
  }
}

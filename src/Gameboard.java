import javax.swing.*;

public class Gameboard extends JPanel {
  private final int BOARD_WIDTH = 320;
  private final int BOARD_HEIGHT = 320;
  private final int PIXEL_SIZE = 16;
  private final int PIXEL_WIDTH = BOARD_WIDTH / PIXEL_SIZE;
  private final int PIXEL_HEIGHT = BOARD_HEIGHT / PIXEL_SIZE;

  private final int grid[][] = new int[PIXEL_WIDTH][PIXEL_HEIGHT];
  private int length;
  private Orientation direction;
  private Ball ball;
  private Timer timer;

  public Gameboard() {
    ball = new Ball(PIXEL_WIDTH, PIXEL_HEIGHT);
  }

  private enum Orientation {
    UP,
    DOWN,
    LEFT,
    RIGHT
  }
}

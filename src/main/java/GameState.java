import java.awt.*;

public class GameState {
  private Snake snake;
  private final Point lemon = new Point();
  private Direction direction = Direction.UP;
  private GameMode mode = GameMode.RUNNING;
  private int highScore = -1;

  public Snake getSnake() {
    return snake;
  }

  public void setSnake(Snake snake) {
    this.snake = snake;
  }

  public Point getLemon() {
    return lemon;
  }

  public Direction getDirection() {
    return direction;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  public GameMode getMode() {
    return mode;
  }

  public void setMode(GameMode mode) {
    this.mode = mode;
  }

  public int getHighScore() {
    return highScore;
  }

  public void setHighScore(int highScore) {
    this.highScore = highScore;
  }
}

import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class GameEngine {
  private final Random random;

  public GameEngine() {
    this(new Random());
  }

  public GameEngine(Random random) {
    this.random = Objects.requireNonNull(random, "random");
  }

  public void reset(GameState state) {
    state.setSnake(new Snake(BoardConfig.PIXEL_WIDTH, BoardConfig.PIXEL_HEIGHT));
    state.setDirection(Direction.UP);
    state.setNextDirection(Direction.UP);
    state.setMode(GameMode.RUNNING);
    spawnFood(state);
  }

  public void requestDirection(GameState state, Direction requested) {
    if (!state.getDirection().isOpposite(requested)) {
      state.setNextDirection(requested);
    }
  }

  public void togglePause(GameState state) {
    if (state.getMode() == GameMode.RUNNING) {
      state.setMode(GameMode.PAUSED);
    } else if (state.getMode() == GameMode.PAUSED) {
      state.setMode(GameMode.RUNNING);
    }
  }

  public void tick(GameState state) {
    if (state.getMode() != GameMode.RUNNING) {
      return;
    }

    final Snake snake = state.getSnake();
    final Direction direction = state.getNextDirection();
    final boolean growing = nextHeadPosition(snake.getHead(), direction).equals(state.getLemon());

    state.setDirection(direction);
    snake.move(direction, BoardConfig.PIXEL_WIDTH, BoardConfig.PIXEL_HEIGHT, growing);

    if (snake.eatingSelf()) {
      state.setMode(GameMode.GAME_OVER);
      return;
    }

    if (growing) {
      spawnFood(state);
    }
  }

  private Point nextHeadPosition(Point head, Direction direction) {
    int nextX = head.x;
    int nextY = head.y;

    switch (direction) {
      case DOWN -> nextY = (nextY + 1) % BoardConfig.PIXEL_HEIGHT;
      case UP -> nextY = (nextY - 1 + BoardConfig.PIXEL_HEIGHT) % BoardConfig.PIXEL_HEIGHT;
      case LEFT -> nextX = (nextX - 1 + BoardConfig.PIXEL_WIDTH) % BoardConfig.PIXEL_WIDTH;
      case RIGHT -> nextX = (nextX + 1) % BoardConfig.PIXEL_WIDTH;
    }

    return new Point(nextX, nextY);
  }

  private void spawnFood(GameState state) {
    final Snake snake = state.getSnake();
    while (true) {
      final Point candidate = new Point(
          random.nextInt(BoardConfig.PIXEL_WIDTH),
          random.nextInt(BoardConfig.PIXEL_HEIGHT)
      );
      if (!snake.containsPoint(candidate)) {
        state.getLemon().setLocation(candidate);
        return;
      }
    }
  }
}

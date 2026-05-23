import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class GameEngine {
  private final Random random;
  private Direction nextDirection = Direction.UP;

  public GameEngine() {
    this(new Random());
  }

  public GameEngine(Random random) {
    this.random = Objects.requireNonNull(random, "random");
  }

  public Direction getNextDirection() {
    return nextDirection;
  }

  public void reset(GameState state) {
    state.setSnake(new Snake(random, BoardConfig.PIXEL_WIDTH, BoardConfig.PIXEL_HEIGHT));
    state.setDirection(Direction.UP);
    nextDirection = Direction.UP;
    state.setMode(GameMode.RUNNING);
    spawnFood(state);
  }

  public void requestDirection(GameState state, Direction requested) {
    if (!state.getDirection().isOpposite(requested)) {
      nextDirection = requested;
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
    final boolean growing = snake.nextHead(nextDirection, BoardConfig.PIXEL_WIDTH, BoardConfig.PIXEL_HEIGHT)
        .equals(state.getLemon());

    state.setDirection(nextDirection);
    snake.move(nextDirection, BoardConfig.PIXEL_WIDTH, BoardConfig.PIXEL_HEIGHT, growing);

    if (snake.eatingSelf()) {
      state.setMode(GameMode.GAME_OVER);
      return;
    }

    if (growing) {
      spawnFood(state);
    }
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

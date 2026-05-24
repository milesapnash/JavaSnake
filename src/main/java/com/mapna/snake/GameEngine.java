package com.mapna.snake;

import java.util.ArrayList;
import java.util.List;
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

    Snake snake = state.getSnake();
    Position head = snake.nextHead(nextDirection, BoardConfig.PIXEL_WIDTH, BoardConfig.PIXEL_HEIGHT);
    boolean growing = head.equals(state.getFood());

    state.setDirection(nextDirection);
    snake.move(head, growing);

    if (snake.eatingSelf()) {
      state.setMode(GameMode.GAME_OVER);
      return;
    }

    if (growing) {
      if (snake.getBody().size() >= BoardConfig.PIXEL_WIDTH * BoardConfig.PIXEL_HEIGHT) {
        state.setMode(GameMode.WON);
      } else {
        spawnFood(state);
      }
    }
  }

  private void spawnFood(GameState state) {
    Snake snake = state.getSnake();
    List<Position> free = new ArrayList<>();
    for (int x = 0; x < BoardConfig.PIXEL_WIDTH; x++) {
      for (int y = 0; y < BoardConfig.PIXEL_HEIGHT; y++) {
        Position p = new Position(x, y);
        if (!snake.contains(p)) {
          free.add(p);
        }
      }
    }
    state.setFood(free.get(random.nextInt(free.size())));
  }
}

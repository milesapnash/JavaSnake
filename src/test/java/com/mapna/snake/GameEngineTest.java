package com.mapna.snake;

import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GameEngineTest {

  private static GameState runningState(Snake snake) {
    GameState state = new GameState();
    state.setSnake(snake);
    state.setDirection(Direction.UP);
    state.setMode(GameMode.RUNNING);
    return state;
  }

  @Test
  void requestDirectionBlocksImmediateReverse() {
    GameEngine engine = new GameEngine(new Random(1L));
    GameState state = runningState(Snake.createFixed(5, 5));
    state.setDirection(Direction.UP);

    engine.requestDirection(state, Direction.DOWN);

    assertEquals(Direction.UP, engine.getNextDirection());
  }

  @Test
  void requestDirectionAcceptsNonOppositeTurn() {
    GameEngine engine = new GameEngine(new Random(1L));
    GameState state = runningState(Snake.createFixed(5, 5));
    state.setDirection(Direction.UP);

    engine.requestDirection(state, Direction.LEFT);

    assertEquals(Direction.LEFT, engine.getNextDirection());
  }

  @Test
  void togglePauseCyclesRunningAndPaused() {
    GameEngine engine = new GameEngine(new Random(1L));
    GameState state = runningState(Snake.createFixed(5, 5));

    engine.togglePause(state);
    assertEquals(GameMode.PAUSED, state.getMode());

    engine.togglePause(state);
    assertEquals(GameMode.RUNNING, state.getMode());
  }

  @Test
  void tickDoesNothingWhenPaused() {
    GameEngine engine = new GameEngine(new Random(1L));
    Snake snake = Snake.createFixed(10, 10);
    Point headBefore = new Point(snake.getHead());
    GameState state = runningState(snake);
    state.setMode(GameMode.PAUSED);

    engine.tick(state);

    assertEquals(headBefore, snake.getHead());
    assertEquals(GameMode.PAUSED, state.getMode());
  }

  @Test
  void tickMovesHeadAccordingToNextDirection() {
    GameEngine engine = new GameEngine(new Random(1L));
    GameState state = runningState(Snake.createFixed(10, 10));
    state.setFood(new Point(0, 0));

    engine.tick(state);

    assertEquals(new Point(10, 9), state.getSnake().getHead());
    assertEquals(Direction.UP, state.getDirection());
  }

  @Test
  void tickWrapsVerticallyAtTopEdge() {
    GameEngine engine = new GameEngine(new Random(1L));
    GameState state = runningState(Snake.createFixed(10, 0));
    state.setFood(new Point(0, 0));

    engine.tick(state);

    assertEquals(new Point(10, BoardConfig.PIXEL_HEIGHT - 1), state.getSnake().getHead());
  }

  @Test
  void tickEatingFoodGrowsSnakeAndRespawnsFood() {
    GameEngine engine = new GameEngine(new Random(42L));
    GameState state = runningState(Snake.createFixed(10, 10));
    state.setFood(new Point(10, 9));

    engine.tick(state);

    assertEquals(new Point(10, 9), state.getSnake().getHead());
    assertEquals(4, state.getSnake().getBody().size());
    assertEquals(1, state.getSnake().growth());
    assertFalse(state.getSnake().containsPoint(state.getFood()));
  }

  @Test
  void tickDetectsSelfCollision() {
    GameEngine engine = new GameEngine(new Random(1L));
    GameState state = runningState(Snake.createFixed(5, 5));
    state.setDirection(Direction.DOWN);
    engine.requestDirection(state, Direction.DOWN);
    state.setFood(new Point(0, 0));

    engine.tick(state);

    assertEquals(GameMode.GAME_OVER, state.getMode());
  }

  @Test
  void tickAllowsMovingToVacatedTailPosition() {
    GameEngine engine = new GameEngine(new Random(42L));
    GameState state = runningState(Snake.createFixed(5, 5));

    // Grow snake to length 4 by eating food
    state.setFood(new Point(5, 4));
    engine.tick(state);
    assertEquals(4, state.getSnake().getBody().size());

    // Navigate a tight U-turn where head lands on the vacated tail position
    state.setFood(new Point(0, 0));
    engine.requestDirection(state, Direction.RIGHT);
    engine.tick(state); // (6,4), (5,4), (5,5), (5,6)

    engine.requestDirection(state, Direction.DOWN);
    engine.tick(state); // (6,5), (6,4), (5,4), (5,5)

    engine.requestDirection(state, Direction.LEFT);
    engine.tick(state); // head → (5,5), old tail was at (5,5)

    assertEquals(GameMode.RUNNING, state.getMode());
    assertEquals(new Point(5, 5), state.getSnake().getHead());
  }

  @Test
  void resetStartsRunningAndPlacesFoodOffSnake() {
    GameEngine engine = new GameEngine(new Random(99L));
    GameState state = new GameState();

    engine.reset(state);

    assertEquals(GameMode.RUNNING, state.getMode());
    assertEquals(Direction.UP, state.getDirection());
    assertEquals(Direction.UP, engine.getNextDirection());
    assertEquals(3, state.getSnake().getBody().size());
    assertFalse(state.getSnake().containsPoint(state.getFood()));
  }
}

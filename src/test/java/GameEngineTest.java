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
    state.setLemon(new Point(0, 0));

    engine.tick(state);

    assertEquals(new Point(10, 9), state.getSnake().getHead());
    assertEquals(Direction.UP, state.getDirection());
  }

  @Test
  void tickWrapsVerticallyAtTopEdge() {
    GameEngine engine = new GameEngine(new Random(1L));
    GameState state = runningState(Snake.createFixed(10, 0));
    state.setLemon(new Point(0, 0));

    engine.tick(state);

    assertEquals(new Point(10, BoardConfig.PIXEL_HEIGHT - 1), state.getSnake().getHead());
  }

  @Test
  void tickEatingFoodGrowsSnakeAndRespawnsFood() {
    GameEngine engine = new GameEngine(new Random(42L));
    GameState state = runningState(Snake.createFixed(10, 10));
    state.setLemon(new Point(10, 9));

    engine.tick(state);

    assertEquals(new Point(10, 9), state.getSnake().getHead());
    assertEquals(4, state.getSnake().getBody().size());
    assertEquals(1, state.getSnake().growth());
    assertFalse(state.getSnake().containsPoint(state.getLemon()));
  }

  @Test
  void tickDetectsSelfCollision() {
    GameEngine engine = new GameEngine(new Random(1L));
    GameState state = runningState(Snake.createFixed(5, 5));
    state.setDirection(Direction.DOWN);
    engine.requestDirection(state, Direction.DOWN);
    state.setLemon(new Point(0, 0));

    engine.tick(state);

    assertEquals(GameMode.GAME_OVER, state.getMode());
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
    assertFalse(state.getSnake().containsPoint(state.getLemon()));
  }
}

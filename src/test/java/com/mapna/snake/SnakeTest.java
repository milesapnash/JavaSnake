package com.mapna.snake;

import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SnakeTest {

  @Test
  void createFixedPlacesThreeSegmentsVertically() {
    Snake snake = Snake.createFixed(5, 3);

    assertEquals(3, snake.getBody().size());
    assertEquals(new Point(5, 3), snake.getHead());
    assertEquals(new Point(5, 4), snake.getBody().get(1));
    assertEquals(new Point(5, 5), snake.getBody().get(2));
  }

  @Test
  void randomConstructorPlacesWithinBounds() {
    Snake snake = new Snake(new Random(42), 20, 20);

    assertEquals(3, snake.getBody().size());
    for (Point p : snake.getBody()) {
      assertTrue(p.x >= 0 && p.x < 20);
      assertTrue(p.y >= 0 && p.y < 20);
    }
  }

  @Test
  void containsPointMatchesBodySegments() {
    Snake snake = Snake.createFixed(5, 5);

    assertTrue(snake.containsPoint(new Point(5, 5)));
    assertTrue(snake.containsPoint(new Point(5, 6)));
    assertTrue(snake.containsPoint(new Point(5, 7)));
    assertFalse(snake.containsPoint(new Point(0, 0)));
  }

  @Test
  void nextHeadDoesNotMutateSnake() {
    Snake snake = Snake.createFixed(5, 5);
    Point originalHead = new Point(snake.getHead());

    snake.nextHead(Direction.UP, 20, 20);

    assertEquals(originalHead, snake.getHead());
    assertEquals(3, snake.getBody().size());
  }

  @Test
  void nextHeadWrapsAtTopEdge() {
    Snake snake = Snake.createFixed(5, 0);
    assertEquals(new Point(5, 19), snake.nextHead(Direction.UP, 20, 20));
  }

  @Test
  void nextHeadWrapsAtBottomEdge() {
    Snake snake = Snake.createFixed(5, 19);
    assertEquals(new Point(5, 0), snake.nextHead(Direction.DOWN, 20, 20));
  }

  @Test
  void nextHeadWrapsAtLeftEdge() {
    Snake snake = Snake.createFixed(0, 5);
    assertEquals(new Point(19, 5), snake.nextHead(Direction.LEFT, 20, 20));
  }

  @Test
  void nextHeadWrapsAtRightEdge() {
    Snake snake = Snake.createFixed(19, 5);
    assertEquals(new Point(0, 5), snake.nextHead(Direction.RIGHT, 20, 20));
  }

  @Test
  void moveWithoutGrowingRemovesTail() {
    Snake snake = Snake.createFixed(5, 5);
    Point oldTail = snake.getBody().getLast();

    snake.move(new Point(5, 4), false);

    assertEquals(3, snake.getBody().size());
    assertEquals(new Point(5, 4), snake.getHead());
    assertFalse(snake.containsPoint(oldTail));
  }

  @Test
  void moveWithGrowingKeepsTail() {
    Snake snake = Snake.createFixed(5, 5);

    snake.move(new Point(5, 4), true);

    assertEquals(4, snake.getBody().size());
    assertEquals(new Point(5, 4), snake.getHead());
    assertTrue(snake.containsPoint(new Point(5, 7)));
  }

  @Test
  void eatingSelfFalseInitially() {
    assertFalse(Snake.createFixed(5, 5).eatingSelf());
  }

  @Test
  void eatingSelfDetectsOverlap() {
    Snake snake = Snake.createFixed(5, 5);
    // Grow to length 4
    snake.move(new Point(5, 4), true);
    // Move head onto an existing body segment
    snake.move(new Point(5, 5), false);

    assertTrue(snake.eatingSelf());
  }

  @Test
  void moveToVacatedTailIsNotSelfCollision() {
    Snake snake = Snake.createFixed(5, 5);
    snake.move(new Point(5, 4), true);  // len 4: (5,4),(5,5),(5,6),(5,7)
    snake.move(new Point(6, 4), false); // len 4: (6,4),(5,4),(5,5),(5,6)
    snake.move(new Point(6, 5), false); // len 4: (6,5),(6,4),(5,4),(5,5)
    snake.move(new Point(6, 6), false); // len 4: (6,6),(6,5),(6,4),(5,4)

    // (5,4) is the current tail — this move vacates it, then places the head there
    snake.move(new Point(5, 4), false);

    assertFalse(snake.eatingSelf());
  }

  @Test
  void growthReturnsSegmentsBeyondInitialLength() {
    Snake snake = Snake.createFixed(5, 5);
    assertEquals(0, snake.growth());

    snake.move(new Point(5, 4), true);
    assertEquals(1, snake.growth());

    snake.move(new Point(5, 3), true);
    assertEquals(2, snake.growth());
  }

  @Test
  void bodyListIsUnmodifiable() {
    Snake snake = Snake.createFixed(5, 5);
    assertThrows(UnsupportedOperationException.class, () -> snake.getBody().add(new Point(0, 0)));
  }
}

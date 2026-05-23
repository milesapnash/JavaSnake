import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/** The snake, constructed in terms of the board size. */
public class Snake {
  private static final int INITIAL_LENGTH = 3;
  private final LinkedList<Point> body = new LinkedList<>();

  public Snake(int width, int height) {
    final int x = new Random().nextInt(width);
    final int y = new Random().nextInt(height - INITIAL_LENGTH);

    body.add(new Point(x, y));
    body.add(new Point(x, y + 1));
    body.add(new Point(x, y + 2));
  }

  /**
   * Creates a length-3 vertical snake: {@code (headX, headY)}, {@code (headX, headY + 1)}, {@code (headX, headY + 2)}.
   * Intended for tests and deterministic setups.
   */
  public static Snake createFixed(int headX, int headY) {
    return new Snake(new Point(headX, headY), new Point(headX, headY + 1), new Point(headX, headY + 2));
  }

  private Snake(Point head, Point seg2, Point seg3) {
    body.add(head);
    body.add(seg2);
    body.add(seg3);
  }

  public Point getHead() {
    return body.getFirst();
  }

  public List<Point> getBody() {
    return Collections.unmodifiableList(body);
  }

  public boolean containsPoint(Point point) {
    return body.contains(point);
  }

  /** Determines if the snake's head is touching any other part of the body. */
  public boolean eatingSelf() {
    for (int i = 1; i < body.size(); i++) {
      if (body.getFirst().equals(body.get(i))) {
        return true;
      }
    }
    return false;
  }

  /** Returns where the head would be after one step, without mutating state. */
  public Point nextHead(Direction direction, int boardWidth, int boardHeight) {
    Point head = body.getFirst();
    return switch (direction) {
      case DOWN -> new Point(head.x, (head.y + 1) % boardHeight);
      case UP -> new Point(head.x, (head.y - 1 + boardHeight) % boardHeight);
      case LEFT -> new Point((head.x - 1 + boardWidth) % boardWidth, head.y);
      case RIGHT -> new Point((head.x + 1) % boardWidth, head.y);
    };
  }

  /** Moves the snake one step in the given direction, wrapping around the board. Grows if specified. */
  public void move(Direction direction, int boardWidth, int boardHeight, boolean growing) {
    body.addFirst(nextHead(direction, boardWidth, boardHeight));
    if (!growing) {
      body.removeLast();
    }
  }

  /** Returns the difference in size of the body from the original length. */
  public int growth() {
    return body.size() - INITIAL_LENGTH;
  }
}

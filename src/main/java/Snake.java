import java.awt.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/** The snake, constructed in terms of the board size. */
public class Snake {
  private static final int INITIAL_LENGTH = 3;
  private final LinkedList<Point> body = new LinkedList<>();
  private final Set<Point> occupied = new HashSet<>();

  public Snake(Random random, int width, int height) {
    final int x = random.nextInt(width);
    final int y = random.nextInt(height - INITIAL_LENGTH);

    addSegment(new Point(x, y));
    addSegment(new Point(x, y + 1));
    addSegment(new Point(x, y + 2));
  }

  /**
   * Creates a length-3 vertical snake: {@code (headX, headY)}, {@code (headX, headY + 1)}, {@code (headX, headY + 2)}.
   * Intended for tests and deterministic setups.
   */
  public static Snake createFixed(int headX, int headY) {
    return new Snake(new Point(headX, headY), new Point(headX, headY + 1), new Point(headX, headY + 2));
  }

  private Snake(Point head, Point seg2, Point seg3) {
    addSegment(head);
    addSegment(seg2);
    addSegment(seg3);
  }

  private void addSegment(Point p) {
    body.add(p);
    occupied.add(p);
  }

  public Point getHead() {
    return body.getFirst();
  }

  public List<Point> getBody() {
    return Collections.unmodifiableList(body);
  }

  public boolean containsPoint(Point point) {
    return occupied.contains(point);
  }

  /** Determines if the snake's head is touching any other part of the body. */
  public boolean eatingSelf() {
    // Head occupies a duplicate position in the set only if it overlaps another segment.
    // Since the set deduplicates, a collision means the set is smaller than the list.
    return occupied.size() < body.size();
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
    Point head = nextHead(direction, boardWidth, boardHeight);
    body.addFirst(head);
    occupied.add(head);
    if (!growing) {
      occupied.remove(body.removeLast());
    }
  }

  /** Returns the difference in size of the body from the original length. */
  public int growth() {
    return body.size() - INITIAL_LENGTH;
  }
}

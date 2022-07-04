import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

/** The snake, constructed in terms of the board size. */
public class Snake {
  private final Point head;
  private final LinkedList<Point> body = new LinkedList<>();

  public Snake(int width, int height) {
    final int x = new Random().nextInt(width);
    final int y = new Random().nextInt(height - 3);

    head = new Point(x, y);
    body.add(head);
    body.add(new Point(x, y + 1));
    body.add(new Point(x, y + 2));
  }

  public Point getHead() {
    return head;
  }

  public LinkedList<Point> getBody() {
    return body;
  }

  public boolean containsPoint(Point point) {
    return body.contains(point);
  }

  /** Determines if the snake's head is touching any other part of the body. */
  public boolean eatingSelf() {
    for (int i = 1; i < body.size(); i++) {
      if (head.equals(body.get(i))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Moves each point of the snake's body to the preceding point's location. Adds a new point to the
   * body if the snake is growing.
   */
  public void changePosition(boolean growing) {
    Point tail = new Point(body.getLast());
    for (int i = body.size() - 1; i > 0; i--) {
      body.get(i).setLocation(body.get(i - 1));
    }
    if (growing) {
      body.add(tail);
    }
  }

  /** Returns the difference in size of the body from the original length. */
  public int growth() {
    return body.size() - 3;
  }
}

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

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

  public boolean eatingSelf() {
    for (int i = 1; i < body.size(); i++) {
      if (head.equals(body.get(i))) {
        return true;
      }
    }
    return false;
  }

  public void changePosition(boolean growing) {
    Point tail = new Point(body.getLast());
    for (int i = body.size() - 1; i > 0; i--) {
      body.get(i).setLocation(body.get(i - 1));
    }
    if (growing) {
      body.add(tail);
    }
  }
}

package com.mapna.snake;

import java.awt.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Snake {
  private static final int INITIAL_LENGTH = 3;
  private final LinkedList<Point> body = new LinkedList<>();
  private final List<Point> unmodifiableBody = Collections.unmodifiableList(body);
  private final Set<Point> occupied = new HashSet<>();

  public Snake(Random random, int width, int height) {
    int x = random.nextInt(width);
    int y = random.nextInt(height - INITIAL_LENGTH);

    addSegment(new Point(x, y));
    addSegment(new Point(x, y + 1));
    addSegment(new Point(x, y + 2));
  }

  /** Creates a length-3 vertical snake at the given head position. */
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
    return unmodifiableBody;
  }

  public boolean containsPoint(Point point) {
    return occupied.contains(point);
  }

  public boolean eatingSelf() {
    // Head occupies a duplicate position in the set only if it overlaps another segment.
    // Since the set deduplicates, a collision means the set is smaller than the list.
    return occupied.size() < body.size();
  }

  /** Returns the next head position without mutating state. */
  public Point nextHead(Direction direction, int boardWidth, int boardHeight) {
    Point head = body.getFirst();
    return switch (direction) {
      case DOWN -> new Point(head.x, (head.y + 1) % boardHeight);
      case UP -> new Point(head.x, (head.y - 1 + boardHeight) % boardHeight);
      case LEFT -> new Point((head.x - 1 + boardWidth) % boardWidth, head.y);
      case RIGHT -> new Point((head.x + 1) % boardWidth, head.y);
    };
  }

  public void move(Point newHead, boolean growing) {
    if (!growing) {
      occupied.remove(body.removeLast());
    }
    body.addFirst(newHead);
    occupied.add(newHead);
  }

  public int growth() {
    return body.size() - INITIAL_LENGTH;
  }
}

package com.mapna.snake;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Snake {
  private static final int INITIAL_LENGTH = 3;
  private final LinkedList<Position> body = new LinkedList<>();
  private final List<Position> unmodifiableBody = Collections.unmodifiableList(body);
  private final Set<Position> occupied = new HashSet<>();

  public Snake(Random random, int width, int height) {
    int x = random.nextInt(width);
    int y = random.nextInt(height - INITIAL_LENGTH);

    addSegment(new Position(x, y));
    addSegment(new Position(x, y + 1));
    addSegment(new Position(x, y + 2));
  }

  /** Creates a length-3 vertical snake at the given head position. */
  public static Snake createFixed(int headX, int headY) {
    return new Snake(new Position(headX, headY), new Position(headX, headY + 1), new Position(headX, headY + 2));
  }

  private Snake(Position head, Position seg2, Position seg3) {
    addSegment(head);
    addSegment(seg2);
    addSegment(seg3);
  }

  private void addSegment(Position p) {
    body.add(p);
    occupied.add(p);
  }

  public Position getHead() {
    return body.getFirst();
  }

  public List<Position> getBody() {
    return unmodifiableBody;
  }

  public boolean contains(Position point) {
    return occupied.contains(point);
  }

  public boolean eatingSelf() {
    // Head occupies a duplicate position in the set only if it overlaps another segment.
    // Since the set deduplicates, a collision means the set is smaller than the list.
    return occupied.size() < body.size();
  }

  /** Returns the next head position without mutating state. */
  public Position nextHead(Direction direction, int boardWidth, int boardHeight) {
    Position head = body.getFirst();
    return switch (direction) {
      case DOWN -> new Position(head.x(), (head.y() + 1) % boardHeight);
      case UP -> new Position(head.x(), (head.y() - 1 + boardHeight) % boardHeight);
      case LEFT -> new Position((head.x() - 1 + boardWidth) % boardWidth, head.y());
      case RIGHT -> new Position((head.x() + 1) % boardWidth, head.y());
    };
  }

  public void move(Position newHead, boolean growing) {
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

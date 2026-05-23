package com.mapna.snake;

public enum Direction {
  DOWN,
  UP,
  LEFT,
  RIGHT;

  public Direction opposite() {
    return switch (this) {
      case DOWN -> UP;
      case UP -> DOWN;
      case LEFT -> RIGHT;
      case RIGHT -> LEFT;
    };
  }

  public boolean isOpposite(Direction other) {
    return this.opposite() == other;
  }
}

public enum Direction {
  DOWN,
  UP,
  LEFT,
  RIGHT;

  public boolean isOpposite(Direction other) {
    return (this == DOWN && other == UP)
        || (this == UP && other == DOWN)
        || (this == LEFT && other == RIGHT)
        || (this == RIGHT && other == LEFT);
  }
}

package com.mapna.snake;

import java.awt.*;

public class GameState {
  private Snake snake;
  private Point food = new Point();
  private Direction direction = Direction.UP;
  private GameMode mode = GameMode.RUNNING;
  private int highScore = -1;

  public Snake getSnake() {
    return snake;
  }

  public void setSnake(Snake snake) {
    this.snake = snake;
  }

  public Point getFood() {
    return food;
  }

  public void setFood(Point food) {
    this.food = food;
  }

  public Direction getDirection() {
    return direction;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  public GameMode getMode() {
    return mode;
  }

  public void setMode(GameMode mode) {
    this.mode = mode;
  }

  public int getHighScore() {
    return highScore;
  }

  public void setHighScore(int highScore) {
    this.highScore = highScore;
  }
}

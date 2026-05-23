package com.mapna.snake;

import java.awt.*;

public class BoardRenderer {
  private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 64);
  private static final Font CAPTION_FONT = new Font("Courier New", Font.BOLD, 24);

  public void paint(Graphics g, GameState state) {
    switch (state.getMode()) {
      case GAME_OVER -> paintGameOver(g, state);
      case WON -> paintWon(g, state);
      case PAUSED -> paintPause(g, state);
      case RUNNING -> paintGameContent(g, state, Color.white, Color.yellow, Color.green);
    }
  }

  private void paintPause(Graphics g, GameState state) {
    paintGameContent(g, state, Color.lightGray, Color.gray, Color.darkGray);

    g.setColor(Color.yellow);
    paintTitles(g, "PAUSED", "-PRESS P TO CONTINUE-");
  }

  private void paintWon(Graphics g, GameState state) {
    g.setColor(Color.green);
    paintTitles(g, "YOU WIN!", "-PRESS R TO RESTART-");
    paintScoreOverlay(g, state);
  }

  private void paintGameOver(Graphics g, GameState state) {
    g.setColor(Color.red);
    paintTitles(g, "GAME OVER", "-PRESS R TO RESTART-");
    paintScoreOverlay(g, state);
  }

  private void paintScoreOverlay(Graphics g, GameState state) {
    String scoreText = "SCORE: " + state.getSnake().growth();
    String highScoreText = "HIGH SCORE: " + state.getHighScore();

    g.setFont(CAPTION_FONT);
    g.setColor(Color.white);
    g.drawString(highScoreText, (BoardConfig.BOARD_WIDTH - g.getFontMetrics().stringWidth(highScoreText)) / 2, BoardConfig.COMPONENT_HEIGHT / 4);
    g.setColor(Color.yellow);
    g.drawString(scoreText, (BoardConfig.BOARD_WIDTH - g.getFontMetrics().stringWidth(scoreText)) / 2, BoardConfig.COMPONENT_HEIGHT / 8);
  }

  private void paintTitles(Graphics g, String title, String caption) {
    g.setFont(TITLE_FONT);
    g.drawString(title, (BoardConfig.BOARD_WIDTH - g.getFontMetrics(TITLE_FONT).stringWidth(title)) / 2, BoardConfig.COMPONENT_HEIGHT / 2);

    g.setColor(Color.white);
    g.setFont(CAPTION_FONT);
    g.drawString(caption, (BoardConfig.BOARD_WIDTH - g.getFontMetrics(CAPTION_FONT).stringWidth(caption)) / 2, BoardConfig.COMPONENT_HEIGHT * 5 / 8);
  }

  private void paintGameContent(Graphics g, GameState state, Color hudColor, Color foodColor, Color snakeColor) {
    Graphics2D g2D = (Graphics2D) g;

    g2D.setPaint(hudColor);
    g2D.fillRect(0, BoardConfig.BOARD_HEIGHT, BoardConfig.BOARD_WIDTH, BoardConfig.HUD_ROWS * BoardConfig.PIXEL_SIZE);

    g2D.setPaint(Color.black);
    paintScore(g2D, state.getSnake().growth());

    Point food = state.getFood();
    g2D.setPaint(foodColor);
    g2D.fillRect(food.x * BoardConfig.PIXEL_SIZE, food.y * BoardConfig.PIXEL_SIZE, BoardConfig.BORDERED_PIXEL_SIZE, BoardConfig.BORDERED_PIXEL_SIZE);

    g2D.setPaint(snakeColor);
    for (Point point : state.getSnake().getBody()) {
      g2D.fillRect(point.x * BoardConfig.PIXEL_SIZE, point.y * BoardConfig.PIXEL_SIZE, BoardConfig.BORDERED_PIXEL_SIZE, BoardConfig.BORDERED_PIXEL_SIZE);
    }
  }

  private void paintScore(Graphics2D g2D, int points) {
    String digits = Integer.toString(points);
    int digitWidth = 4 * BoardConfig.PIXEL_SIZE;
    int totalWidth = digits.length() * digitWidth - BoardConfig.PIXEL_SIZE;
    int startX = (BoardConfig.BOARD_WIDTH - totalWidth) / 2;
    for (int i = 0; i < digits.length(); i++) {
      paintDigit(PIXEL_DIGITS[digits.charAt(i) - '0'], g2D, startX + i * digitWidth);
    }
  }

  private void paintDigit(PixelDigit digit, Graphics2D g2D, int startX) {
    boolean[][] graphic = digit.graphics;
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 5; y++) {
        if (graphic[y][x]) {
          g2D.fillRect(
              startX + x * BoardConfig.PIXEL_SIZE,
              BoardConfig.BOARD_HEIGHT + BoardConfig.PIXEL_SIZE + y * BoardConfig.PIXEL_SIZE,
              BoardConfig.BORDERED_PIXEL_SIZE,
              BoardConfig.BORDERED_PIXEL_SIZE
          );
        }
      }
    }
  }

  private static final PixelDigit[] PIXEL_DIGITS = PixelDigit.values();

  private enum PixelDigit {
    ZERO(new boolean[][]{{true, true, true}, {true, false, true}, {true, false, true}, {true, false, true}, {true, true, true}}),
    ONE(new boolean[][]{{false, true, false}, {false, true, false}, {false, true, false}, {false, true, false}, {false, true, false}}),
    TWO(new boolean[][]{{true, true, true}, {false, false, true}, {true, true, true}, {true, false, false}, {true, true, true}}),
    THREE(new boolean[][]{{true, true, true}, {false, false, true}, {false, true, true}, {false, false, true}, {true, true, true}}),
    FOUR(new boolean[][]{{true, false, true}, {true, false, true}, {true, true, true}, {false, false, true}, {false, false, true}}),
    FIVE(new boolean[][]{{true, true, true}, {true, false, false}, {true, true, true}, {false, false, true}, {true, true, true}}),
    SIX(new boolean[][]{{true, true, true}, {true, false, false}, {true, true, true}, {true, false, true}, {true, true, true}}),
    SEVEN(new boolean[][]{{true, true, true}, {false, false, true}, {false, false, true}, {false, false, true}, {false, false, true}}),
    EIGHT(new boolean[][]{{true, true, true}, {true, false, true}, {true, true, true}, {true, false, true}, {true, true, true}}),
    NINE(new boolean[][]{{true, true, true}, {true, false, true}, {true, true, true}, {false, false, true}, {false, false, true}});

    private final boolean[][] graphics;

    PixelDigit(boolean[][] graphics) {
      this.graphics = graphics;
    }
  }
}

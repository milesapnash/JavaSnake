import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class Board extends JPanel implements ActionListener {
  private final int TICK_RATE = 80;
  private final int BOARD_WIDTH = 480;
  private final int BOARD_HEIGHT = 480;
  private final int PIXEL_SIZE = 24;
  private final int COMPONENT_HEIGHT = BOARD_HEIGHT + 7 * PIXEL_SIZE;
  private final int PIXEL_WIDTH = BOARD_WIDTH / PIXEL_SIZE;
  private final int PIXEL_HEIGHT = BOARD_HEIGHT / PIXEL_SIZE;
  private final int BORDERED_PIXEL_SIZE = PIXEL_SIZE - 1;

  private final Timer timer = new Timer(TICK_RATE, this);
  private final Point lemon = new Point();

  private Orientation direction;
  private Orientation nextDirection;
  private Snake snake;
  private boolean gameOver;
  private boolean paused;
  private int highScore;

  /** A board to display the game and deal with user input. */
  public Board() {
    addKeyListener(new DirectionAdapter());
    setPreferredSize(new Dimension(BOARD_WIDTH, COMPONENT_HEIGHT));
    setBackground(Color.BLACK);
    setFocusable(true);

    initBoard();
  }

  /** Resets fields of the Board for a new game. */
  private void initBoard() {
    snake = new Snake(PIXEL_WIDTH, PIXEL_HEIGHT);
    direction = Orientation.UP;
    nextDirection = direction;
    gameOver = false;
    paused = false;
    highScore = -1;
    newLemonLocation();
    timer.start();
  }

  /** Sets the lemon to a new point on the board that isn't part of the snake. */
  private void newLemonLocation() {
    while (true) {
      final int newX = new Random().nextInt(PIXEL_WIDTH);
      final int newY = new Random().nextInt(PIXEL_HEIGHT);
      final Point newP = new Point(newX, newY);
      if (!snake.containsPoint(newP)) {
        lemon.setLocation(newX, newY);
        return;
      }
    }
  }

  /** Game management code, running with each tick. */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (!gameOver) {
      boolean growSnake = false;
      if (lemon.equals(snake.getHead())) {
        newLemonLocation();
        growSnake = true;
      } else if (snake.eatingSelf()) {
        gameOver = true;
        timer.stop();
      }
      snake.changePosition(growSnake);
      updateHead();
    }
    repaint();
  }

  /** Updates position of the snake's head based on current direction. */
  private void updateHead() {
    final Point head = snake.getHead();
    direction = nextDirection;
    switch (direction) {
      case DOWN -> head.y = (head.y + 1) % PIXEL_HEIGHT;
      case UP -> head.y = (head.y - 1) % PIXEL_HEIGHT;
      case LEFT -> head.x = (head.x - 1) % PIXEL_WIDTH;
      case RIGHT -> head.x = (head.x + 1) % PIXEL_WIDTH;
    }

    if (head.y == -1) {
      head.y += PIXEL_HEIGHT;
    }
    if (head.x == -1) {
      head.x += PIXEL_WIDTH;
    }
  }

  /** Paints board or other output depending on current game state. */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (gameOver) {
      updateHighScore();
      paintGameOver(g);
    } else if (paused) {
      paintPause(g);
      timer.stop();
    } else {
      paintPixels(g);
    }
  }

  /** If a high score already exists, reads the text file and updates the value stored if it has been surpassed.
   *  Otherwise, creates a text file to store the current score. Stores high score in field to prevent lookups
   *  where possible.
   */
  private void updateHighScore() {
    final int points = snake.growth();
    if (highScore < points) {
      final String root = "highscore.txt";
      if (new File(root).exists()) {
        try {
          highScore = Integer.parseInt(Files.readAllLines(Path.of(root)).get(0));
          if (points > highScore) {
            highScore = points;
            final FileWriter fileWriter = new FileWriter(root);
            fileWriter.write(Integer.toString(points));
            fileWriter.close();
          }
        }
        catch (Exception ignored) {
          highScore = -1;
        }
      } else {
        highScore = points;
        try {
          final FileWriter fileWriter = new FileWriter(root);
          fileWriter.write(Integer.toString(points));
          fileWriter.close();
        }
        catch (Exception ignored) {
          highScore = -1;
        }
      }
    }
  }

  /** Paints pause screen. */
  private void paintPause(Graphics g) {
    final Graphics2D g2D = (Graphics2D) g;

    g2D.setPaint(Color.lightGray);
    g2D.fillRect(0, BOARD_HEIGHT, BOARD_WIDTH, 7 * PIXEL_SIZE);

    g2D.setPaint(Color.black);
    translatePoints(g2D);

    g2D.setPaint(Color.gray);
    g2D.fillRect(lemon.x * PIXEL_SIZE, lemon.y * PIXEL_SIZE, BORDERED_PIXEL_SIZE, BORDERED_PIXEL_SIZE);

    g2D.setPaint(Color.darkGray);
    for (Point point : snake.getBody()) {
      g2D.fillRect(point.x * PIXEL_SIZE, point.y * PIXEL_SIZE, BORDERED_PIXEL_SIZE, BORDERED_PIXEL_SIZE);
    }

    g.setColor(Color.yellow);
    paintTitles(g, "PAUSED", "-PRESS P TO CONTINUE-");
  }

  /** Paints Game Over screen. */
  private void paintGameOver(Graphics g) {
    final Font captionFont = new Font("Courier New", Font.BOLD, 24);
    final String scoreOutput = "SCORE: " + snake.growth();
    final String highScoreOutput = "HIGH SCORE: " + highScore;

    g.setColor(Color.red);
    paintTitles(g, "GAME OVER", "-PRESS R TO RESTART-");
    g.drawString(highScoreOutput, (BOARD_WIDTH - getFontMetrics(captionFont).stringWidth(highScoreOutput)) / 2, COMPONENT_HEIGHT / 4);
    g.setColor(Color.yellow);
    g.drawString(scoreOutput, (BOARD_WIDTH - getFontMetrics(captionFont).stringWidth(scoreOutput)) / 2,  COMPONENT_HEIGHT / 8);
  }

  /** Prints title and caption elements of displays. */
  private void paintTitles(Graphics g, String titleOutput, String captionOutput) {
    final Font titleFont = new Font("Arial", Font.BOLD, 64);
    final Font captionFont = new Font("Courier New", Font.BOLD, 24);

    g.setFont(titleFont);
    g.drawString(titleOutput, (BOARD_WIDTH - getFontMetrics(titleFont).stringWidth(titleOutput)) / 2, COMPONENT_HEIGHT / 2);

    g.setColor(Color.white);
    g.setFont(captionFont);
    g.drawString(captionOutput, (BOARD_WIDTH - getFontMetrics(captionFont).stringWidth(captionOutput)) / 2, COMPONENT_HEIGHT * 5 / 8);
  }

  /** Paints pixels on the board including snake, lemon and points. */
  private void paintPixels(Graphics g) {
    final Graphics2D g2D = (Graphics2D) g;

    g2D.setPaint(Color.white);
    g2D.fillRect(0, BOARD_HEIGHT, BOARD_WIDTH, 7 * PIXEL_SIZE);

    g2D.setPaint(Color.black);
    translatePoints(g2D);

    g2D.setPaint(Color.yellow);
    g2D.fillRect(lemon.x * PIXEL_SIZE, lemon.y * PIXEL_SIZE, BORDERED_PIXEL_SIZE, BORDERED_PIXEL_SIZE);

    g2D.setPaint(Color.green);
    for (Point point : snake.getBody()) {
      g2D.fillRect(point.x * PIXEL_SIZE, point.y * PIXEL_SIZE, BORDERED_PIXEL_SIZE, BORDERED_PIXEL_SIZE);
    }
  }

  /** Determines how the current number of points will be painted. */
  private void translatePoints(Graphics2D g2D) {
    final int points = snake.growth();
    final int pointsRemainder = points % 10;
    if (points > 99) {
      paintDigit(PixelPoints.values()[points / 100], g2D, (int) (BOARD_WIDTH / 2 - 5.5 * PIXEL_SIZE));
      paintDigit(PixelPoints.values()[(points / 10) % 10], g2D, (int) (BOARD_WIDTH / 2 - PIXEL_SIZE * 1.5));
      paintDigit(PixelPoints.values()[pointsRemainder], g2D, (int) (BOARD_WIDTH / 2 + 2.5 * PIXEL_SIZE));
    } else if (points > 9) {
      paintDigit(PixelPoints.values()[points / 10], g2D, (int) (BOARD_WIDTH / 2 - 3.5 * PIXEL_SIZE));
      paintDigit(PixelPoints.values()[pointsRemainder], g2D, (int) (BOARD_WIDTH / 2 + 0.5 * PIXEL_SIZE));
    } else {
      paintDigit(PixelPoints.values()[pointsRemainder], g2D, (int) (BOARD_WIDTH / 2 - PIXEL_SIZE * 1.5));
    }
  }

  /** Takes graphical representation of a digit to paint as pixels. */
  private void paintDigit(PixelPoints val, Graphics2D g2D, int startX) {
    final boolean[][] g = val.graphics;
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 5; y++) {
        if (g[y][x]) {
          g2D.fillRect(startX + x * PIXEL_SIZE, BOARD_HEIGHT + PIXEL_SIZE + y * PIXEL_SIZE, BORDERED_PIXEL_SIZE, BORDERED_PIXEL_SIZE);
        }
      }
    }
  }

  /** An enum to represent the different directions the snake can move. */
  private enum Orientation {
    DOWN,
    UP,
    LEFT,
    RIGHT
  }

  /** An enum to represent the digits 0-9 graphically with a 2D-Array of boolean values. */
  private enum PixelPoints {
    ZERO (new boolean[][]{{true, true, true}, {true, false, true}, {true, false, true}, {true, false, true}, {true, true, true}}),
    ONE (new boolean[][]{{false, true, false}, {false, true, false}, {false, true, false}, {false, true, false}, {false, true, false}}),
    TWO (new boolean[][]{{true, true, true}, {false, false, true}, {true, true, true}, {true, false, false}, {true, true, true}}),
    THREE (new boolean[][]{{true, true, true}, {false, false, true}, {false, true, true}, {false, false, true}, {true, true, true}}),
    FOUR (new boolean[][]{{true, false, true}, {true, false, true}, {true, true, true}, {false, false, true}, {false, false, true}}),
    FIVE (new boolean[][]{{true, true, true}, {true, false, false}, {true, true, true}, {false, false, true}, {true, true, true}}),
    SIX (new boolean[][]{{true, true, true}, {true, false, false}, {true, true, true}, {true, false, true}, {true, true, true}}),
    SEVEN (new boolean[][]{{true, true, true}, {false, false, true}, {false, false, true}, {false, false, true}, {false, false, true}}),
    EIGHT (new boolean[][]{{true, true, true}, {true, false, true}, {true, true, true}, {true, false, true}, {true, true, true}}),
    NINE (new boolean[][]{{true, true, true}, {true, false, true}, {true, true, true}, {false, false, true}, {false, false, true}});

    private final boolean[][] graphics;

    PixelPoints(boolean[][] graphics) {this.graphics = graphics;}
    }

  /** Class to determine appropriate response to key presses. */
  private class DirectionAdapter extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
        case (KeyEvent.VK_DOWN), (KeyEvent.VK_S) -> {
          if (direction != Orientation.UP) {
            nextDirection = Orientation.DOWN;
          }
        }
        case (KeyEvent.VK_UP), (KeyEvent.VK_W) -> {
          if (direction != Orientation.DOWN) {
            nextDirection = Orientation.UP;
          }
        }
        case (KeyEvent.VK_LEFT), (KeyEvent.VK_A) -> {
          if (direction != Orientation.RIGHT) {
            nextDirection = Orientation.LEFT;
          }
        }
        case (KeyEvent.VK_RIGHT), (KeyEvent.VK_D) -> {
          if (direction != Orientation.LEFT) {
            nextDirection = Orientation.RIGHT;
          }
        }
        case (KeyEvent.VK_P) -> {
          if (paused) {
            timer.start();
            paused = false;
          } else {
            paused = true;
          }
        }
        case (KeyEvent.VK_R) -> {
          if (gameOver) {
            initBoard();
          }
        }
        case (KeyEvent.VK_ESCAPE) -> System.exit(0);
      }
    }
  }
}

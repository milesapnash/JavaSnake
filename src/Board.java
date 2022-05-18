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
  private final int BOARD_WIDTH = 480;
  private final int BOARD_HEIGHT = 480;
  private final int PIXEL_SIZE = 24;
  private final int COMPONENT_HEIGHT = BOARD_HEIGHT + 7 * PIXEL_SIZE;
  private final int TICK_RATE = 90;
  private final int PIXEL_WIDTH = BOARD_WIDTH / PIXEL_SIZE;
  private final int PIXEL_HEIGHT = BOARD_HEIGHT / PIXEL_SIZE;
  private final int BORDERED_PIXEL_SIZE = PIXEL_SIZE - 1;

  private final Timer timer = new Timer(TICK_RATE, this);
  private final Point lemon = new Point();
  private Snake snake;
  private Orientation direction;
  private Orientation nextDirection;
  private boolean gameOver;
  private int points;
  private int highScore;

  public Board() {
    addKeyListener(new DirectionAdapter());
    setPreferredSize(new Dimension(BOARD_WIDTH, COMPONENT_HEIGHT));
    setBackground(Color.BLACK);
    setFocusable(true);
    initBoard();
  }

  private void initBoard() {
    snake = new Snake(PIXEL_WIDTH, PIXEL_HEIGHT);
    direction = Orientation.UP;
    nextDirection = direction;
    gameOver = false;
    points = 0;
    highScore = -1;
    newLemonLocation();
    timer.start();
  }

  private void newLemonLocation(){
    while (true) {
      final int newX = new Random().nextInt(PIXEL_WIDTH);
      final int newY = new Random().nextInt(PIXEL_HEIGHT);
      final Point newP = new Point(newX, newY);
      if (!snake.containsPoint(newP)){
        lemon.setLocation(newX, newY);
        return;
      }
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (gameOver){
      getHighScore();
      displayGameOver(g);
    } else {
      paintPixels(g);
    }
  }

  private void paintPixels(Graphics g){
    Graphics2D g2D = (Graphics2D) g;

    g2D.setPaint(new Color(250, 255, 10));
    g2D.fillRect(lemon.x * PIXEL_SIZE, lemon.y * PIXEL_SIZE, BORDERED_PIXEL_SIZE, BORDERED_PIXEL_SIZE);

    g2D.setPaint(new Color(255, 255, 255));
    g2D.fillRect(0, BOARD_HEIGHT, BOARD_WIDTH, 7 * PIXEL_SIZE);

    g2D.setPaint(new Color(0, 0, 0));
    translatePoints(g2D);

    g2D.setPaint(new Color(14, 200, 10));
    for (Point point : snake.getBody()) {
      g2D.fillRect(point.x * PIXEL_SIZE, point.y * PIXEL_SIZE, BORDERED_PIXEL_SIZE, BORDERED_PIXEL_SIZE);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (!gameOver){
      boolean growSnake = false;
      if (lemon.equals(snake.getHead())){
        points++;
        newLemonLocation();
        growSnake = true;
      } else if (snake.eatingSelf()){
        gameOver = true;
        timer.stop();
      }
      snake.changePosition(growSnake);
      updateHead();
    }
    repaint();
  }

  private void translatePoints(Graphics2D g2D){
    int pointsRemainder = points % 10;
    if (points > 99){
      displayPoints(PixelPoints.values()[points / 100], g2D, (int) (BOARD_WIDTH / 2 - 5.5 * PIXEL_SIZE));
      displayPoints(PixelPoints.values()[(points / 10) % 10], g2D, (int) (BOARD_WIDTH / 2 - PIXEL_SIZE * 1.5));
      displayPoints(PixelPoints.values()[pointsRemainder], g2D, (int) (BOARD_WIDTH / 2 + 2.5 * PIXEL_SIZE));
    } else if (points > 9){
      displayPoints(PixelPoints.values()[points / 10], g2D, (int) (BOARD_WIDTH / 2 - 3.5 * PIXEL_SIZE));
      displayPoints(PixelPoints.values()[pointsRemainder], g2D, (int) (BOARD_WIDTH / 2 + 0.5 * PIXEL_SIZE));
    } else {
      displayPoints(PixelPoints.values()[pointsRemainder], g2D, (int) (BOARD_WIDTH / 2 - PIXEL_SIZE * 1.5));
    }
  }

  private void displayPoints(PixelPoints val, Graphics2D g2D, int startX){
    final boolean[][] g = val.graphics;
    for (int x = 0; x < 3; x++){
      for (int y = 0; y < 5; y++){
        if (g[y][x]){
          g2D.fillRect(startX + x * PIXEL_SIZE, BOARD_HEIGHT + PIXEL_SIZE + y * PIXEL_SIZE, BORDERED_PIXEL_SIZE, BORDERED_PIXEL_SIZE);
        }
      }
    }
  }

  private void updateHead(){
    final Point head = snake.getHead();
    direction = nextDirection;
    switch (direction) {
      case DOWN -> head.y = (head.y + 1) % PIXEL_HEIGHT;
      case UP -> head.y = (head.y - 1) % PIXEL_HEIGHT;
      case LEFT -> head.x = (head.x - 1) % PIXEL_WIDTH;
      case RIGHT -> head.x = (head.x + 1) % PIXEL_WIDTH;
    }

    if (head.y == -1){
      head.y += PIXEL_HEIGHT;
    }
    if (head.x == -1){
      head.x += PIXEL_WIDTH;
    }
  }

  private void getHighScore(){
    if (highScore < points) {
      final String root = "highscore.txt";
      if (new File(root).exists()){
        try{
          highScore = Integer.parseInt(Files.readAllLines(Path.of(root)).get(0));
          if (points > highScore) {
            highScore = points;
            final FileWriter fileWriter = new FileWriter(root);
            fileWriter.write(Integer.toString(points));
            fileWriter.close();
          }
        }
        catch (Exception ignored){
          highScore = -1;
        }
      } else {
        highScore = points;
        try{
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

  private void displayGameOver(Graphics g){
    final String highScoreOutput = "HIGH SCORE: " + highScore;
    final String titleOutput = "GAME OVER";
    final String captionOutput = "-PRESS R TO RESTART-";
    final Font titleFont = new Font("Arial", Font.BOLD, 64);
    final Font captionFont = new Font("Courier New", Font.BOLD, 20);

    g.setFont(titleFont);
    g.setColor(Color.red);
    g.drawString(titleOutput, (BOARD_WIDTH - getFontMetrics(titleFont).stringWidth(titleOutput)) / 2, COMPONENT_HEIGHT / 2);
    
    g.setColor(Color.white);
    g.setFont(captionFont);
    g.drawString(captionOutput, (BOARD_WIDTH - getFontMetrics(captionFont).stringWidth(captionOutput)) / 2, COMPONENT_HEIGHT * 5 / 8);
    g.drawString(highScoreOutput, (BOARD_WIDTH - getFontMetrics(captionFont).stringWidth(highScoreOutput)) / 2, COMPONENT_HEIGHT / 8);
  }

  private enum Orientation {
    DOWN,
    UP,
    LEFT,
    RIGHT
  }

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

  private class DirectionAdapter extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()){
        case (KeyEvent.VK_DOWN):
          if (direction != Orientation.UP) {
            nextDirection = Orientation.DOWN;
          }
          break;
        case (KeyEvent.VK_UP):
          if (direction != Orientation.DOWN) {
            nextDirection = Orientation.UP;
          }
          break;
        case (KeyEvent.VK_LEFT):
          if (direction != Orientation.RIGHT) {
            nextDirection = Orientation.LEFT;
          }
          break;
        case (KeyEvent.VK_RIGHT):
          if (direction != Orientation.LEFT) {
            nextDirection = Orientation.RIGHT;
          }
          break;
        case (KeyEvent.VK_R):
          if (gameOver) {
            initBoard();
          }
          break;
      }
    }
  }
}

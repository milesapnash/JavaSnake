import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

public class Board extends JPanel implements ActionListener {
  private final int BOARD_WIDTH = 320;
  private final int BOARD_HEIGHT = 320;
  private final int PIXEL_SIZE = 16;
  private final int TICK_RATE = 100;
  private final int PIXEL_WIDTH = BOARD_WIDTH / PIXEL_SIZE;
  private final int PIXEL_HEIGHT = BOARD_HEIGHT / PIXEL_SIZE;

  private final Status[][] grid = new Status[PIXEL_WIDTH][PIXEL_HEIGHT];
  private final Point head = new Point(new Random().nextInt(PIXEL_WIDTH), new Random().nextInt(PIXEL_HEIGHT - 3));
  private final Point lemon = new Point(new Random().nextInt(PIXEL_WIDTH), new Random().nextInt(PIXEL_HEIGHT));
  private final Timer timer;
  private final LinkedList<Point> snake = new LinkedList<>();
  private Orientation direction = Orientation.UP;
  private boolean growSnake = false;
  private boolean gameOver = false;

  public Board() {
    addKeyListener(new DirectionAdapter());
    setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    setBackground(Color.BLACK);
    setFocusable(true);

    clearGrid();

    snake.add(head);
    snake.add(new Point(head.x, head.y + 1));
    snake.add(new Point(head.x, head.y + 2));

    setGridPoint(lemon, Status.LEMON);
    setGridPoint(head, Status.SNAKE);
    setGridPoint(snake.get(1), Status.SNAKE);
    setGridPoint(snake.get(2), Status.SNAKE);

    timer = new Timer(TICK_RATE, this);
    timer.start();
  }

  private void newLemonLocation(){
    setGridPoint(lemon, Status.SNAKE);
    lemon.setLocation(new Random().nextInt(PIXEL_WIDTH), new Random().nextInt(PIXEL_HEIGHT));
    setGridPoint(lemon, Status.LEMON);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (!gameOver){
      if (lemon.equals(head)){
        newLemonLocation();
        growSnake = true;
      }
      checkEatingSelf();
      changePosition();
      growSnake = false;
    }
    repaint();
  }

  private void clearGrid(){
    for (int x = 0; x < PIXEL_WIDTH; x++){
      for (int y = 0; y < PIXEL_HEIGHT; y++){
        grid[x][y] = Status.CLEAR;
      }
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    paintPixels(g);
  }

  private void paintPixels(Graphics g){
    Graphics2D g2D = (Graphics2D) g;
    for (int x = 0; x < PIXEL_WIDTH; x++){
      for (int y = 0; y < PIXEL_HEIGHT; y++){
        Color pixel;
        switch (grid[x][y]){
          case SNAKE -> pixel = new Color(0, 180, 0);
          case LEMON -> pixel = new Color(255, 255, 0);
          default -> pixel = new Color(0, 0, 0);
        }
        g2D.setPaint(pixel);
        g2D.fillRect(x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
      }
    }
  }

  private void changePosition(){
    changeSnakePosition();
    switch (direction){
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


    setGridPoint(head, Status.SNAKE);
  }

  private void checkEatingSelf(){
    for (int i = 1; i < snake.size(); i ++){
      if (head.equals(snake.get(i))){
        gameOver = true;
        break;
      }
    }

    if (gameOver){
      timer.stop();
    }
  }

  private void changeSnakePosition(){
    Point tail = new Point(snake.getLast());
    setGridPoint(tail, Status.CLEAR);
    for (int i = snake.size() - 1; i > 0; i--){
      snake.get(i).setLocation(snake.get(i - 1));
    }
    if (growSnake) {
      snake.add(tail);
      setGridPoint(tail, Status.SNAKE);
    }
  }

  private void setGridPoint(Point change, Status status){
    grid[change.x][change.y] = status;
  }

  private enum Orientation {
    DOWN,
    UP,
    LEFT,
    RIGHT
  }

  private enum Status {
    CLEAR,
    SNAKE,
    LEMON
  }

  private class DirectionAdapter extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      int key = e.getKeyCode();

      if ((key == KeyEvent.VK_DOWN) && (direction != Orientation.UP)) {
        direction = Orientation.DOWN;
      }
      if ((key == KeyEvent.VK_UP) && (direction != Orientation.DOWN)) {
        direction = Orientation.UP;
      }
      if ((key == KeyEvent.VK_LEFT) && (direction != Orientation.RIGHT)) {
        direction = Orientation.LEFT;
      }
      if ((key == KeyEvent.VK_RIGHT) && (direction != Orientation.LEFT)) {
        direction = Orientation.RIGHT;
      }
    }
  }
}

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
  private final int TICK_RATE = 80;
  private final int PIXEL_WIDTH = BOARD_WIDTH / PIXEL_SIZE;
  private final int PIXEL_HEIGHT = BOARD_HEIGHT / PIXEL_SIZE;

  private final Status[][] grid = new Status[PIXEL_WIDTH][PIXEL_HEIGHT];
  private final Point head = new Point(new Random().nextInt(PIXEL_WIDTH), new Random().nextInt(PIXEL_HEIGHT - 3));
  private final Timer timer = new Timer(TICK_RATE, this);
  private final Point lemon = new Point();
  private final LinkedList<Point> snake = new LinkedList<>();
  private Orientation direction = Orientation.UP;
  private Orientation nextDirection = direction;
  private boolean gameOver = false;

  public Board() {
    addKeyListener(new DirectionAdapter());
    setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    setFocusable(true);
    setBackground(Color.BLACK);
    clearGrid();

    addToSnake(head);
    addToSnake(new Point(head.x, head.y + 1));
    addToSnake(new Point(head.x, head.y + 2));
    newLemonLocation();

    timer.start();
  }

  private void clearGrid(){
    for (int x = 0; x < PIXEL_WIDTH; x++){
      for (int y = 0; y < PIXEL_HEIGHT; y++){
        grid[x][y] = Status.CLEAR;
      }
    }
  }

  private void addToSnake(Point point){
    snake.add(point);
    setGridPoint(point, Status.SNAKE);
  }

  private void setGridPoint(Point change, Status status){
    grid[change.x][change.y] = status;
  }

  private void newLemonLocation(){
    while (true) {
      final int newX = new Random().nextInt(PIXEL_WIDTH);
      final int newY = new Random().nextInt(PIXEL_HEIGHT);
      final Point newP = new Point(newX, newY);
      if (!pointIsSnake(newP, 0)){
        lemon.setLocation(newX, newY);
        setGridPoint(lemon, Status.LEMON);
        return;
      }
    }
  }

  private boolean pointIsSnake(Point check, int startIndex){
    for (int i = startIndex; i < snake.size(); i++){
      if (check.equals(snake.get(i))){
        return true;
      }
    }
    return false;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (gameOver){
      displayGameOver(g);
    } else {
      paintPixels(g);
    }
  }

  private void paintPixels(Graphics g){
    Graphics2D g2D = (Graphics2D) g;
    for (int x = 0; x < PIXEL_WIDTH; x++){
      for (int y = 0; y < PIXEL_HEIGHT; y++){
        final Color pixel = switch (grid[x][y]) {
          case CLEAR -> new Color(0, 0, 0);
          case SNAKE -> new Color(0, 180, 0);
          case LEMON -> new Color(255, 255, 0);
        };
        g2D.setPaint(pixel);
        g2D.fillRect(x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
      }
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (!gameOver){
      boolean growSnake = false;
      if (lemon.equals(head)){
        setGridPoint(lemon, Status.SNAKE);
        newLemonLocation();
        growSnake = true;
      }
      checkEatingSelf();
      changePosition(growSnake);
    }

    repaint();
  }

  private void checkEatingSelf(){
    if (pointIsSnake(head, 1)){
      gameOver = true;
      timer.stop();
    }
  }

  private void changePosition(boolean grow){
    direction = nextDirection;
    changeSnakePosition(grow);
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

    setGridPoint(head, Status.SNAKE);
  }

  private void changeSnakePosition(boolean growSnake){
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

  private void displayGameOver(Graphics g){
    final String output = "GAME OVER";
    final Font font = new Font("Arial", Font.BOLD, 32);

    g.setColor(Color.red);
    g.setFont(font);
    g.drawString(output, (BOARD_WIDTH - getFontMetrics(font).stringWidth(output)) / 2, BOARD_HEIGHT / 2);
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
        nextDirection = Orientation.DOWN;
      } else if ((key == KeyEvent.VK_UP) && (direction != Orientation.DOWN)) {
        nextDirection = Orientation.UP;
      } else if ((key == KeyEvent.VK_LEFT) && (direction != Orientation.RIGHT)) {
        nextDirection = Orientation.LEFT;
      } else if ((key == KeyEvent.VK_RIGHT) && (direction != Orientation.LEFT)) {
        nextDirection = Orientation.RIGHT;
      }
    }
  }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Board extends JPanel implements ActionListener {
  private final int BOARD_WIDTH = 480;
  private final int BOARD_HEIGHT = 480;
  private final int PIXEL_SIZE = 24;
  private final int TICK_RATE = 90;
  private final int PIXEL_WIDTH = BOARD_WIDTH / PIXEL_SIZE;
  private final int PIXEL_HEIGHT = BOARD_HEIGHT / PIXEL_SIZE;

  private final Snake snake = new Snake(PIXEL_WIDTH, PIXEL_HEIGHT);
  private final Timer timer = new Timer(TICK_RATE, this);
  private final Point lemon = new Point();
  private Orientation direction = Orientation.UP;
  private Orientation nextDirection = direction;
  private boolean gameOver = false;

  public Board() {
    addKeyListener(new DirectionAdapter());
    setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    setFocusable(true);
    setBackground(Color.BLACK);
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
      displayGameOver(g);
    } else {
      paintPixels(g);
    }
  }

  private void paintPixels(Graphics g){
    Graphics2D g2D = (Graphics2D) g;
    g2D.setPaint(new Color(0, 0, 0));
    g2D.fillRect(0, 0, PIXEL_WIDTH, PIXEL_HEIGHT);

    g2D.setPaint(new Color(255, 255, 0));
    g2D.fillRect(lemon.x * PIXEL_SIZE, lemon.y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);

    g2D.setPaint(new Color(14, 200, 10));
    for (Point point : snake.getBody()) {
      g2D.fillRect(point.x * PIXEL_SIZE, point.y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (!gameOver){
      boolean growSnake = false;
      if (lemon.equals(snake.getHead())){
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

  private void displayGameOver(Graphics g){
    final String output = "GAME OVER";
    final Font font = new Font("Arial", Font.BOLD, 48);

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

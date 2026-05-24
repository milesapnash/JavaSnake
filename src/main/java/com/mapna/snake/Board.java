package com.mapna.snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {
  private final Timer timer = new Timer(BoardConfig.TICK_RATE_MS, this);
  private final GameEngine engine = new GameEngine();
  private final GameState state = new GameState();
  private final BoardRenderer renderer = new BoardRenderer();
  private final HighScoreStore highScoreStore = new FileHighScoreStore(BoardConfig.HIGHSCORE_FILE);
  private boolean highScoreSaved;

  public Board() {
    addKeyListener(new DirectionAdapter());
    setPreferredSize(new Dimension(BoardConfig.BOARD_WIDTH, BoardConfig.COMPONENT_HEIGHT));
    setBackground(Color.BLACK);
    setFocusable(true);

    initBoard();
  }

  private void initBoard() {
    engine.reset(state);
    state.setHighScore(highScoreStore.load());
    highScoreSaved = false;
    timer.start();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    engine.tick(state);
    if (state.getMode() == GameMode.PAUSED || state.getMode() == GameMode.GAME_OVER || state.getMode() == GameMode.WON) {
      timer.stop();
    }
    if ((state.getMode() == GameMode.GAME_OVER || state.getMode() == GameMode.WON) && !highScoreSaved) {
      state.setHighScore(highScoreStore.saveIfHigher(state.getSnake().growth()));
      highScoreSaved = true;
    }
    repaint();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    renderer.paint(g, state);
  }

  private class DirectionAdapter extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_DOWN, KeyEvent.VK_S -> engine.requestDirection(state, Direction.DOWN);
        case KeyEvent.VK_UP, KeyEvent.VK_W -> engine.requestDirection(state, Direction.UP);
        case KeyEvent.VK_LEFT, KeyEvent.VK_A -> engine.requestDirection(state, Direction.LEFT);
        case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> engine.requestDirection(state, Direction.RIGHT);
        case KeyEvent.VK_P -> {
          engine.togglePause(state);
          if (state.getMode() == GameMode.RUNNING) {
            timer.start();
          }
        }
        case KeyEvent.VK_R -> {
          if (state.getMode() == GameMode.GAME_OVER || state.getMode() == GameMode.WON) {
            initBoard();
          }
        }
        case KeyEvent.VK_ESCAPE -> SwingUtilities.getWindowAncestor(Board.this).dispose();
        default -> { }
      }
    }
  }
}

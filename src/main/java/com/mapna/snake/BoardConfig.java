package com.mapna.snake;

public final class BoardConfig {
  public static final int TICK_RATE_MS = 80;
  public static final int MIN_TICK_RATE_MS = 40;
  public static final int SPEEDUP_THRESHOLD = 10;
  public static final int SPEED_STEP_MS = 2;
  public static final int BOARD_WIDTH = 480;
  public static final int BOARD_HEIGHT = 480;
  public static final int PIXEL_SIZE = 24;
  public static final int HUD_ROWS = 7;
  public static final int COMPONENT_HEIGHT = BOARD_HEIGHT + HUD_ROWS * PIXEL_SIZE;
  public static final int PIXEL_WIDTH = BOARD_WIDTH / PIXEL_SIZE;
  public static final int PIXEL_HEIGHT = BOARD_HEIGHT / PIXEL_SIZE;
  public static final int BORDERED_PIXEL_SIZE = PIXEL_SIZE - 1;
  public static final String HIGHSCORE_FILE = "highscore.txt";

  private BoardConfig() {
  }

  public static int tickRateMs(int growth) {
    int speedups = Math.max(0, growth - SPEEDUP_THRESHOLD);
    return Math.max(MIN_TICK_RATE_MS, TICK_RATE_MS - speedups * SPEED_STEP_MS);
  }
}

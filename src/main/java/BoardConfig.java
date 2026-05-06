public final class BoardConfig {
  public static final int TICK_RATE_MS = 80;
  public static final int BOARD_WIDTH = 480;
  public static final int BOARD_HEIGHT = 480;
  public static final int PIXEL_SIZE = 24;
  public static final int HUD_ROWS = 7;
  public static final int COMPONENT_HEIGHT = BOARD_HEIGHT + HUD_ROWS * PIXEL_SIZE;
  public static final int PIXEL_WIDTH = BOARD_WIDTH / PIXEL_SIZE;
  public static final int PIXEL_HEIGHT = BOARD_HEIGHT / PIXEL_SIZE;
  public static final int BORDERED_PIXEL_SIZE = PIXEL_SIZE - 1;

  private BoardConfig() {
  }
}

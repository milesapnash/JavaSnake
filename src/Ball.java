import java.util.Random;

public class Ball {
  private final int X_LIMIT;
  private final int Y_LIMIT;
  private int x;
  private int y;

  public Ball(int xLimit, int yLimit){
    X_LIMIT = xLimit;
    Y_LIMIT = yLimit;
    randomLocation();
  }

  public void randomLocation(){
    x = new Random().nextInt(X_LIMIT);
    y = new Random().nextInt(Y_LIMIT);
  }

  public boolean isTouched(int x, int y){
    return (this.x == x) && (this.y == y);
  }
}

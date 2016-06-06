/**
 * Created by PetruscaFamily on 6/16/2015.
 */
public class PacBox {
  public static enum BoxType {
    EMPTY,
    DOT,
    POWER,
    OBSTACLE
  }

  int x;
  int y;

  BoxType type;

  public PacBox(int x, int y, BoxType type) {
    this.x = x;
    this.y = y;
    this.type = type;
  }

  public void setType(BoxType type) {
    this.type = type;
  }
}

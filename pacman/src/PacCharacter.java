import java.awt.*;

/**
 * Created by PetruscaFamily on 6/17/2015.
 */
public class PacCharacter {
  int x;
  int y;
  int direction = 0;
  int direction2 = 0;
  Point position;
  int speed = 2;
  Rectangle rect;

  public PacCharacter(int x, int y) {
    this.x = x;
    this.y = y;
    position = new Point(x,y);
  }
}

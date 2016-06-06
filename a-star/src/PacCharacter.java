import java.awt.*;
import java.util.*;

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

  public void chooseDirection(java.util.List<Point> path) {
    if(!path.isEmpty()) {
      Point point = path.get(0);
      Point position = new Point(this.position.x / 20, this.position.y / 20);

      int dx = point.x - position.x;
      int dy = point.y - position.y;

      speed = 2;

      if(this.position.x % 20 == 0 && this.position.y % 20 == 0) {
        if (dx == 1 && dy == 0) {
          direction = 0;
        }
        if (dx == -1 && dy == 0) {
          direction = 2;
        }
        if (dx == 0 && dy == -1) {
          direction = 1;
        }
        if (dx == 0 && dy == 1) {
          direction = 3;
        }
      }
    }
  }

}

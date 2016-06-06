import java.awt.*;
import java.util.ArrayList;

/**
 * Created by PetruscaFamily on 6/9/2015.
 */
public class TetrisPiece {
  ArrayList<Point> points = new ArrayList<Point>(4);
  public int x;
  public int y;
  public Point axis;
  int state = 1;
  Color color;

  public TetrisPiece(int x, int y, Point axis) {
    this.x = x;
    this.y = y;
    this.axis = axis;
  }

  public void moveDown() {
    y++;
  }

  public void changeState() {
    if(state == 4) {
      state = 1;
    } else {
      state++;
    }
    updateState();
  }
  public void updateState() {
    points.remove(0);
    points.remove(0);
    points.remove(0);
    points.remove(0);
  }

  public void paint(Graphics2D g) {
    g.setColor(color);
    for (Point point : points) {
      g.fillRect((x + point.x)* TetrisRun.BOX_WIDTH + 1, (y + point.y) * TetrisRun.BOX_HEIGHT + 1,
          TetrisRun.BOX_WIDTH - 1, TetrisRun.BOX_HEIGHT - 1);
    }
  }
}

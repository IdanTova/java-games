import java.awt.*;

/**
 * Created by PetruscaFamily on 6/9/2015.
 */
public class J_Piece extends TetrisPiece {
  public J_Piece(int x, int y) {
    super(x, y, new Point(0, 1));
    points.add(new Point(0, 0));
    points.add(new Point(0, 1));
    points.add(new Point(0, 2));
    points.add(new Point(-1, 2));
    color = Color.blue;
  }

  public void updateState() {
    super.updateState();

    if (state == 1) {
      points.add(new Point(0, 0));
      points.add(new Point(0, 1));
      points.add(new Point(0, 2));
      points.add(new Point(-1, 2));
    }
    if (state == 2) {
      points.add(new Point(-1, 0));
      points.add(new Point(-1, 1));
      points.add(new Point(0, 1));
      points.add(new Point(1, 1));
    }
    if (state == 3) {
      points.add(new Point(0, 0));
      points.add(new Point(1, 0));
      points.add(new Point(0, 1));
      points.add(new Point(0, 2));
    }
    if (state == 4) {
      points.add(new Point(0, 1));
      points.add(new Point(-1, 1));
      points.add(new Point(1, 1));
      points.add(new Point(1, 2));
    }
  }
}

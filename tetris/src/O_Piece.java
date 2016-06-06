import java.awt.*;

public class O_Piece extends TetrisPiece {
  public O_Piece(int x, int y) {
    super(x, y, new Point(0, 0));
    points.add(new Point(0, 0));
    points.add(new Point(1, 0));
    points.add(new Point(0, 1));
    points.add(new Point(1, 1));
    color = Color.yellow.darker();
  }

  public void updateState() {
    super.updateState();

    if (state == 1) {
      points.add(new Point(0, 0));
      points.add(new Point(1, 0));
      points.add(new Point(0, 1));
      points.add(new Point(1, 1));
    }
    if (state == 2) {
      points.add(new Point(0, 0));
      points.add(new Point(1, 0));
      points.add(new Point(0, 1));
      points.add(new Point(1, 1));
    }
    if (state == 3) {
      points.add(new Point(0, 0));
      points.add(new Point(1, 0));
      points.add(new Point(0, 1));
      points.add(new Point(1, 1));
    }
    if (state == 4) {
      points.add(new Point(0, 0));
      points.add(new Point(1, 0));
      points.add(new Point(0, 1));
      points.add(new Point(1, 1));
    }
  }
}

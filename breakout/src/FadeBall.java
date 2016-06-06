import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

/**
 * Created by PetruscaFamily on 2/18/2016.
 */
public class FadeBall {
  private double radius = 7.0f;
  private Point2D point;
  private Arc2D bounds;
  private float alpha = 1.0f;
  private boolean isDone;

  public FadeBall(Point2D point) {
    this.point = point;
    bounds = new Arc2D.Double(point.getX() - radius/2.0, point.getY() - radius/2.0, radius * 2, radius * 2, 0, 2 * Math.PI, Arc2D.CHORD);
  }

  public FadeBall(double x, double y) {
    point = new Point2D.Double(x, y);
    bounds = new Arc2D.Double(x - radius/2.0, y - radius/2.0, radius * 2, radius * 2, 0, 360, Arc2D.CHORD);
  }

  public void draw(Graphics2D g) {
    updateBounds();
    g.setColor(new Color(0, 156, 209));
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    if(!isDone) {
      alpha -= 0.0499f;
      radius -= 0.3;
    }
    g.setColor(Color.lightGray);
    g.fill(bounds);
    if(alpha <= 0.05f) {
      isDone = true;
      alpha = 0;
    }
  }

  public Arc2D getBounds() {
    return bounds;
  }

  public void updateBounds() {
    bounds = new Arc2D.Double(point.getX() - radius/2.0, point.getY() - radius/2.0, radius * 2, radius * 2, 0, 360, Arc2D.CHORD);
  }

  public double getX() {
    return point.getX();
  }

  public double getY() {
    return  point.getY();
  }

  public boolean isDone() {
    return isDone;
  }
}

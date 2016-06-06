import animators.OutFadeAnimator;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class BreakBox {
  private Color color;
  private Point2D point;
  private Rectangle2D bounds;
  private boolean isUnbreakable;

  public BreakBox(Color color, Point2D point) {
    this.color = color;
    this.point = point;
    bounds = new Rectangle2D.Double(point.getX(), point.getY(), 4*25, 25);
  }

  public BreakBox(Color color, double x, double y) {
    this.color = color;
    point = new Point2D.Double(x, y);
    bounds = new Rectangle2D.Double(x, y, 4*25, 25);
  }

  public void draw(Graphics2D g) {
    g.setColor(color);
    g.fill(bounds);
    if(isUnbreakable) {
      g.setClip(bounds);
      g.setColor(Color.BLACK);
      for (int i = 2; i < bounds.getMaxX() + 20; i+=5) {
        g.draw(new Line2D.Double(i, bounds.getMinY(), i - 15, bounds.getMaxY()));
      }
      g.setClip(0, 0, 1920, 1080);
    }
    g.setColor(Color.WHITE);
    g.draw(bounds);
  }

  public Rectangle2D getBounds() {
    return bounds;
  }

  public Point2D getPoint() {
    return point;
  }

  public Color getColor() {
    return color;
  }

  public boolean isUnbreakable() {
    return isUnbreakable;
  }

  public void setUnbreakable() {
    color = Color.lightGray.darker();
    isUnbreakable = true;
  }
}

import animators.OutFadeAnimator;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class DeadBox {
  private Color color;
  private Point2D point;
  private Rectangle2D bounds;
  private OutFadeAnimator animator;

  public DeadBox(Color color, Point2D point) {
    this.color = color;
    this.point = point;
    bounds = new Rectangle2D.Double(point.getX(), point.getY(), 4*25, 25);
    animator = new OutFadeAnimator(0.099f);
  }

  public DeadBox(Color color, double x, double y) {
    this.color = color;
    point = new Point2D.Double(x, y);
    bounds = new Rectangle2D.Double(x, y, 4*25, 25);
    animator = new OutFadeAnimator(0.099f);
  }

  public DeadBox(BreakBox box) {
    color = box.getColor();
    point = box.getPoint();
    bounds = new Rectangle2D.Double(point.getX(), point.getY(), 4*25, 25);
    animator = new OutFadeAnimator(0.199f);
  }

  public void draw(Graphics2D g) {
    animator.animate(g);
    g.setColor(color);
    g.fill(bounds);
    g.setColor(Color.WHITE);
    g.draw(bounds);
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.00f));
  }

  public Rectangle2D getBounds() {
    return bounds;
  }

  public OutFadeAnimator getAnimator() {
    return animator;
  }
}



import animationsFX.OutFadeAnimatorFX;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class DeadBoxFX {
  private Color color;
  private Point2D point;
  private Rectangle2D bounds;
  private OutFadeAnimatorFX animator;

  public DeadBoxFX(Color color, Point2D point) {
    this.color = color;
    this.point = point;
    bounds = new Rectangle2D.Double(point.getX(), point.getY(), 4*25, 25);
    animator = new OutFadeAnimatorFX(0.099f);
  }

  public DeadBoxFX(Color color, double x, double y) {
    this.color = color;
    point = new Point2D.Double(x, y);
    bounds = new Rectangle2D.Double(x, y, 4*25, 25);
    animator = new OutFadeAnimatorFX(0.099f);
  }

  public DeadBoxFX(BreakBoxFX box) {
    color = box.getColor();
    point = box.getPoint();
    bounds = new Rectangle2D.Double(point.getX(), point.getY(), 4*25, 25);
    animator = new OutFadeAnimatorFX(0.199f);
  }

  public void draw(GraphicsContext g) {
    animator.animate(g);
    if(!BreakoutScreenFX.isNeon) {
      g.setStroke(Color.WHITE);
      g.strokeRect(bounds.getX() + 0.5, bounds.getY() + 0.5, bounds.getWidth(), bounds.getHeight());
    } else {
      g.setStroke(color);
      g.setLineWidth(4);
      g.setLineJoin(StrokeLineJoin.ROUND);
      g.setLineCap(StrokeLineCap.BUTT);
      g.setFill(new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.4));
      g.fillRect(bounds.getX() + 3.5, bounds.getY() + 3.5, bounds.getWidth() - 6, bounds.getHeight() - 6);
      g.strokeRect(bounds.getX() + 3.5, bounds.getY() + 3.5, bounds.getWidth() - 6, bounds.getHeight() - 6);
    }
    g.setLineCap(StrokeLineCap.SQUARE);
    g.setLineWidth(1);
    g.setLineJoin(StrokeLineJoin.MITER);
  }

  public Rectangle2D getBounds() {
    return bounds;
  }

  public OutFadeAnimatorFX getAnimator() {
    return animator;
  }
}

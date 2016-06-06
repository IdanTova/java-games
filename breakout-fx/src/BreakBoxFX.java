import animationsFX.PhysicsAnimatorFX;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import org.age.math.PhysicsVector;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static java.lang.Math.PI;
import static java.lang.Math.random;

public class BreakBoxFX {
  private double finalY;
  private Color color;
  private Point2D point;
  private Rectangle2D bounds;
  private boolean isUnbreakable;
  private PhysicsAnimatorFX physics;
  private boolean isAnimatingOut;
  private boolean isStationary;

  public BreakBoxFX(Color color, double x, double y) {
    finalY = y;
    this.color = color;
    point = new Point2D.Double(x, 35*25 + y);
    bounds = new Rectangle2D.Double(x, 35*25 + y, 4*25, 25);
    PhysicsVector vel = new PhysicsVector(random() * 5 + 5, (float)(random() * 7*PI/4 + PI/8));
    PhysicsVector acc = new PhysicsVector(0, (float)(PI));
    PhysicsVector jer = new PhysicsVector(0.05, (float)(PI));
    physics = new PhysicsAnimatorFX(vel, acc, jer);
  }

  private BreakBoxFX(Color color, double x, double y, boolean isStationary) {
    this.color = color;
    point = new Point2D.Double(x, y);
    bounds = new Rectangle2D.Double(x, y, 4*25, 25);
    this.isStationary = isStationary;
  }

  public static BreakBoxFX getStationaryInstance(Color color, double x, double y) {
    return new BreakBoxFX(color, x, y, true);
  }

  public void draw(GraphicsContext g) {
    double x = 0;
    double y = 0;
    if(!isStationary) {
      x = physics.getX();
      y = physics.getY();
    }
    g.setGlobalAlpha(1);
    g.setStroke(color);
    g.setLineWidth(4);
    g.setLineJoin(StrokeLineJoin.ROUND);
    g.setLineCap(StrokeLineCap.BUTT);
    g.setFill(new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.4));

    g.fillRect(bounds.getX() + 3.5 + x, bounds.getY() + 3.5 + y, bounds.getWidth() - 6, bounds.getHeight() - 6);
    g.strokeRect(bounds.getX() + 3.5 + x, bounds.getY() + 3.5 + y, bounds.getWidth() - 6, bounds.getHeight() - 6);

    g.setLineCap(StrokeLineCap.SQUARE);
    g.setLineWidth(1);
    g.setLineJoin(StrokeLineJoin.MITER);
    if(isAnimatingOut) {
      animateOut();
    }
  }

  public void incrementForStartUp(double increment) {
    if(bounds.getY() > finalY) {
      bounds = new Rectangle2D.Double(bounds.getX(), bounds.getY() - increment, bounds.getWidth(), bounds.getHeight());
      point = new Point2D.Double(bounds.getX(), bounds.getY() - increment);
    } else if(bounds.getY() - increment <= finalY) {
      bounds = new Rectangle2D.Double(bounds.getX(), finalY, bounds.getWidth(), bounds.getHeight());
      point = new Point2D.Double(bounds.getX(), finalY);
    } else {
      bounds = new Rectangle2D.Double(bounds.getX(), finalY, bounds.getWidth(), bounds.getHeight());
      point = new Point2D.Double(bounds.getX(), finalY);
    }
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

  public boolean isDone() {
    return finalY == bounds.getY();
  }

  public void setUnbreakable() {
    color = Color.LIGHTGRAY.darker();
    isUnbreakable = true;
  }

  public void animateOut() {
    if(physics.getCounter() > 35) {
      physics.animate();
    }
    physics.incrementCounter();
  }

  public void startAnimateOut() {
    isAnimatingOut = true;
  }

  public boolean isFinishedExiting() {
    return bounds.getY() + physics.getY() > BreakoutRunFX.upperCanvas.getHeight() + 200;
  }

}

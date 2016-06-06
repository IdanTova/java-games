import org.age.math.PolarVector;
import org.age.math.Vector;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Created by PetruscaFamily on 6/28/2015.
 */
public class Missile {
  public static final int INITIAL_SPEED = 10;
  Point2D position;
  PolarVector velocity;
  double scale = 1;
  Polygon shape;

  public Missile(Point2D position, double speed, double angle) {
    this.position = position;
    velocity = new PolarVector(speed + INITIAL_SPEED, angle);
    int[] x = {1,2,3};
    int[] y = {1,2,3};
    shape = new Polygon(x,y,3);
  }

  public void paint(Graphics2D g) {
    g.setColor(Color.white);
    Point2D p1 = new Point2D.Double(+0.0 * scale, -1.0 * scale);
    Point2D p2 = new Point2D.Double(+0.0 * scale, +1.0 * scale);
    Point2D p3 = new Point2D.Double(+7.0 * scale, +1.0 * scale);
    Point2D p4 = new Point2D.Double(+7.0 * scale, -1.0 * scale);

    applyRotation(p1);
    applyRotation(p2);
    applyRotation(p3);
    applyRotation(p4);

    drawLine(g, p1, p2);
    drawLine(g, p2, p3);
    drawLine(g, p3, p4);
    drawLine(g, p4, p1);

    int[] x = {  (int)(position.getX() + p1.getX()), (int)(position.getX() + p2.getX()),
        (int)(position.getX() + p3.getX()), (int)(position.getX() + p4.getX()), };
    int[] y = {  (int)(position.getY() + p1.getY()), (int)(position.getY() + p2.getY()),
        (int)(position.getY() + p3.getY()), (int)(position.getY() + p4.getY())  };
    shape = new Polygon(x, y, 4);
  }

  private void drawLine(Graphics2D g, Point2D p1, Point2D p2) {
    g.draw(new Line2D.Double(position.getX() + p1.getX(), position.getY() + p1.getY(),
        position.getX() + p2.getX(), position.getY() + p2.getY()));
  }

  private void applyRotation(Point2D p) {
    double x = p.getX();
    double y = p.getY();

    p.setLocation(x * Math.cos(velocity.a) - y * Math.sin(velocity.a), x * Math.sin(velocity.a) + y * Math.cos(velocity.a));
  }

  public void moveStep() {
    double x = position.getX();
    double y = position.getY();

    double speedX = velocity.m * Math.cos(velocity.a);
    double speedY = velocity.m * Math.sin(velocity.a);

    position.setLocation(x + speedX, y + speedY);
  }
}

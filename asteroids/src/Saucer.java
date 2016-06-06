import org.age.math.PolarVector;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by PetruscaFamily on 6/29/2015.
 */
public class Saucer {
  public static enum SaucerState {
    LARGE,
    SMALL,
    DEAD
  }
  Point2D EastHead;
  Point2D WestHead;
  Point2D centerPoint;
  double scale;
  PolarVector velocity;
  Polygon shape;
  SaucerState state;

  public Saucer(Point2D eastHead, SaucerState state, double speed, double angle) {
    this.EastHead = eastHead;
    this.state = state;
    velocity = new PolarVector(speed, angle);
    WestHead = new Point2D.Double(eastHead.getX(), eastHead.getY());
    centerPoint = new Point2D.Double(eastHead.getX(), eastHead.getY());
    int[] x = { 1,2,3 };
    int[] y = { 1,2,3 };
    shape = new Polygon(x, y, 3);
  }

  public void paint(Graphics2D g) {
    if (state == SaucerState.LARGE) {
      scale = 2;
    } else if (state == SaucerState.SMALL){
      scale = 1;
    }

    Point2D p1 = new Point2D.Double(+0.0 * scale, +0.0 * scale);
    Point2D p2 = new Point2D.Double(-5.0 * scale, +5.0 * scale);
    Point2D p3 = new Point2D.Double(-25.0 * scale, +5.0 * scale);
    Point2D p4 = new Point2D.Double(-29.0 * scale, +0.0 * scale);
    Point2D p5 = new Point2D.Double(-21.0 * scale, -5.0 * scale);
    Point2D p6 = new Point2D.Double(-18.0 * scale, -10.0 * scale);
    Point2D p7 = new Point2D.Double(-11.0 * scale, -10.0 * scale);
    Point2D p8 = new Point2D.Double(-8.0 * scale, -5.0 * scale);

    WestHead.setLocation(p4.getX() + EastHead.getX(), p4.getY() + EastHead.getY());
    centerPoint.setLocation(p4.getX()/2 + EastHead.getX(), p4.getY() + EastHead.getY());

    g.setColor(Color.white);
    drawLine(g, p1, p2);
    drawLine(g, p2, p3);
    drawLine(g, p3, p4);
    drawLine(g, p4, p5);
    drawLine(g, p5, p6);
    drawLine(g, p6, p7);
    drawLine(g, p7, p8);
    drawLine(g, p8, p1);
    drawLine(g, p1, p4);
    drawLine(g, p5, p8);

    int[] x = {  (int)(EastHead.getX() + p1.getX()), (int)(EastHead.getX() + p2.getX()),
        (int)(EastHead.getX() + p3.getX()), (int)(EastHead.getX() + p4.getX()),
        (int)(EastHead.getX() + p5.getX()), (int)(EastHead.getX() + p6.getX()),
        (int)(EastHead.getX() + p7.getX()), (int)(EastHead.getX() + p8.getX())  };
    int[] y = {  (int)(EastHead.getY() + p1.getY()), (int)(EastHead.getY() + p2.getY()),
        (int)(EastHead.getY() + p3.getY()), (int)(EastHead.getY() + p4.getY()),
        (int)(EastHead.getY() + p5.getY()), (int)(EastHead.getY() + p6.getY()),
        (int)(EastHead.getY() + p7.getY()), (int)(EastHead.getY() + p8.getY())  };
    shape = new Polygon(x, y, 8);
  }

  private void drawLine(Graphics2D g, Point2D p1, Point2D p2) {
    g.draw(new Line2D.Double(EastHead.getX() + p1.getX(), EastHead.getY() + p1.getY(),
        EastHead.getX() + p2.getX(), EastHead.getY() + p2.getY()));
  }

  public void setAngle(double angle) {
    velocity.a = angle;
  }

  public double getAngle() {
    return velocity.a;
  }

  public void moveStep() {
    if(!(state == SaucerState.DEAD)) {
      double x = EastHead.getX();
      double y = EastHead.getY();
      double speedX = velocity.m * Math.cos(velocity.a);
      double speedY = velocity.m * Math.sin(velocity.a);
      EastHead.setLocation(x + speedX, y + speedY);
    }
  }

  public void shootMissile(ArrayList<Missile> missiles) {
    if(!(state == SaucerState.DEAD)) {
      Point2D a = new Point2D.Double(WestHead.getX(), WestHead.getY());
      Missile missile = new Missile(a, velocity.m, generateAngle());
      if(missiles.size() < 4) {
        missiles.add(missile);
      }
    }
  }

  private double generateAngle() {
    return (Math.random() * 4 + 1) * 90 + 45;
  }

  public boolean isDead() {
    return state == SaucerState.DEAD;
  }
}

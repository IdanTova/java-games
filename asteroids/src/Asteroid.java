import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Created by PetruscaFamily on 6/27/2015.
 */
public class Asteroid {
  Point2D position;
  Size size;
  Type type;
  double angle;
  double speed;
  double scale;
  Polygon shape;

  final Point2D p1;
  final Point2D p2;
  final Point2D p3;
  final Point2D p4;
  final Point2D p5;
  final Point2D p6;
  final Point2D p7;
  final Point2D p8;
  final Point2D p9;
  final Point2D p10;

  public static enum Size {
    LARGE,
    MEDIUM,
    SMALL
  }
  public static enum Type {
    TYPE_1,
    TYPE_2,
    TYPE_3,
    TYPE_RANDOM
  }

  public Asteroid(Point2D position, Size size, Type type, double speed, double angle) {
    this.position = position;
    this.size = size;
    this.type = type;
    this.speed = speed;
    this.angle = angle;
    int[] x = {1,2,3};
    int[] y = {1,2,3};
    shape = new Polygon(x,y,3);

    if(size == Size.LARGE) {
      scale = 5;
    }
    if(size == Size.MEDIUM) {
      scale = 3;
    }
    if(size == Size.SMALL) {
      scale = 1;
    }

    p1 = new Point2D.Double((+5.0 + Math.random() * 5) * scale, (-8.0 + Math.random() * 5) * scale);
    p2 = new Point2D.Double((+10.0 + Math.random() * 5) * scale, (-3.0 + Math.random() * 5) * scale);
    p3 = new Point2D.Double((+6.0 + Math.random() * 5) * scale, (+3.0 + Math.random() * 5) * scale);
    p4 = new Point2D.Double((+10.0 + Math.random() * 5) * scale, (+7.0 + Math.random() * 5) * scale);
    p5 = new Point2D.Double((+2.0 + Math.random() * 5) * scale, (+11.0 + Math.random() * 5) * scale);
    p6 = new Point2D.Double((-7.0 + Math.random() * 5) * scale, (+11.0 + Math.random() * 5) * scale);
    p7 = new Point2D.Double((-11.0 + Math.random() * 5) * scale, (+7.0 + Math.random() * 5) * scale);
    p8 = new Point2D.Double((-11.0 + Math.random() * 5) * scale, (+3.0 + Math.random() * 5) * scale);
    p9 = new Point2D.Double((-6.0 + Math.random() * 5) * scale, (-6.0 + Math.random() * 5) * scale);
    p10 = new Point2D.Double((-2.0 + Math.random() * 5) * scale, (-3.0 + Math.random() * 5) * scale);
  }

  public void paint(Graphics2D g) {
    g.setColor(Color.white);
    if(type == Type.TYPE_1) {
      p1.setLocation(+5.0 * scale, -8.0 * scale);
      p2.setLocation(+10.0 * scale, -3.0 * scale);
      p3.setLocation(+6.0 * scale, +3.0 * scale);
      p4.setLocation(+10.0 * scale, +7.0 * scale);
      p5.setLocation(+2.0 * scale, +11.0 * scale);
      p6.setLocation(-7.0 * scale, +11.0 * scale);
      p7.setLocation(-11.0 * scale, +7.0 * scale);
      p8.setLocation(-11.0 * scale, +3.0 * scale);
      p9.setLocation(-6.0 * scale, -6.0 * scale);
      p10.setLocation(-2.0 * scale, -3.0 * scale);
    } else if(type == Type.TYPE_2) {
      p1.setLocation(+5.0 * scale, -8.0 * scale);
      p2.setLocation(+10.0 * scale, -3.0 * scale);
      p3.setLocation(+6.0 * scale, +3.0 * scale);
      p4.setLocation(+10.0 * scale, +7.0 * scale);
      p5.setLocation(+2.0 * scale, +11.0 * scale);
      p6.setLocation(-7.0 * scale, +11.0 * scale);
      p7.setLocation(-11.0 * scale, +7.0 * scale);
      p8.setLocation(-11.0 * scale, +3.0 * scale);
      p9.setLocation(-6.0 * scale, -6.0 * scale);
      p10.setLocation(-2.0 * scale, -3.0 * scale);
    } else if(type == Type.TYPE_3) {
      p1.setLocation(+5.0 * scale, -8.0 * scale);
      p2.setLocation(+10.0 * scale, -3.0 * scale);
      p3.setLocation(+6.0 * scale, +3.0 * scale);
      p4.setLocation(+10.0 * scale, +7.0 * scale);
      p5.setLocation(+2.0 * scale, +11.0 * scale);
      p6.setLocation(-7.0 * scale, +11.0 * scale);
      p7.setLocation(-11.0 * scale, +7.0 * scale);
      p8.setLocation(-11.0 * scale, +3.0 * scale);
      p9.setLocation(-6.0 * scale, -6.0 * scale);
      p10.setLocation(-2.0 * scale, -3.0 * scale);
    }

    if(type != Type.TYPE_RANDOM) {
      applyRotation(p1);
      applyRotation(p2);
      applyRotation(p3);
      applyRotation(p4);
      applyRotation(p5);
      applyRotation(p6);
      applyRotation(p7);
      applyRotation(p8);
      applyRotation(p9);
      applyRotation(p10);
    }

    drawLine(g, p1, p2);
    drawLine(g, p2, p3);
    drawLine(g, p3, p4);
    drawLine(g, p4, p5);
    drawLine(g, p5, p6);
    drawLine(g, p6, p7);
    drawLine(g, p7, p8);
    drawLine(g, p8, p9);
    drawLine(g, p9, p10);
    drawLine(g, p10, p1);

    int[] x = {  (int)(position.getX() + p1.getX()), (int)(position.getX() + p2.getX()),
        (int)(position.getX() + p3.getX()), (int)(position.getX() + p4.getX()), (int)(position.getX() + p5.getX()),
        (int)(position.getX() + p6.getX()), (int)(position.getX() + p7.getX()), (int)(position.getX() + p8.getX()),
        (int)(position.getX() + p9.getX()), (int)(position.getX() + p10.getX())  };
    int[] y = {  (int)(position.getY() + p1.getY()), (int)(position.getY() + p2.getY()),
        (int)(position.getY() + p3.getY()), (int)(position.getY() + p4.getY()), (int)(position.getY() + p5.getY()),
        (int)(position.getY() + p6.getY()), (int)(position.getY() + p7.getY()), (int)(position.getY() + p8.getY()),
        (int)(position.getY() + p9.getY()), (int)(position.getY() + p10.getY())  };
    shape = new Polygon(x, y, 10);
  }

  private void drawLine(Graphics2D g, Point2D p1, Point2D p2) {
    g.draw(new Line2D.Double(position.getX() + p1.getX(), position.getY() + p1.getY(),
        position.getX() + p2.getX(), position.getY() + p2.getY()));
  }

  private void applyRotation(Point2D p) {
    double x = p.getX();
    double y = p.getY();

    p.setLocation(x * Math.cos(angle) - y * Math.sin(angle), x * Math.sin(angle) + y * Math.cos(angle));
  }

  public void moveStep() {
    double x = position.getX();
    double y = position.getY();

    double speedX = speed * Math.cos(angle);
    double speedY = speed * Math.sin(angle);

    position.setLocation(x + speedX, y + speedY);
  }
}

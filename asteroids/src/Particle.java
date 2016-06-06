import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by PetruscaFamily on 6/29/2015.
 */
public class Particle {
  int duration;
  int durationCounter;
  Point2D position;
  ParticleType type;
  double angle;
  double speed;

  public static enum ParticleType {
    DOT,
    LINE
  }

  public Particle(Point2D position, ParticleType type, double speed, double angle, int duration) {
    this.position = position;
    this.type = type;
    this.speed = speed;
    this.angle = angle;
    this.duration = duration;
  }

  public void paint(Graphics2D g) {
    durationCounter++;

    g.setColor(Color.white);
    Shape shape = new Rectangle2D.Double(position.getX(), position.getY(), 2, 2);
    g.draw(shape);
  }

  public void moveStep() {
    double x = position.getX();
    double y = position.getY();

    double speedX = speed * Math.cos(angle);
    double speedY = speed * Math.sin(angle);

    position.setLocation(x + speedX, y + speedY);
  }
}

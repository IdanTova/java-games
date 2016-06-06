import org.age.math.PolarVector;
import org.age.math.Vector;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

/**
 * Created by PetruscaFamily on 6/26/2015.
 */
public class SpaceShip {
  public static enum ShipState {
    DEFAULT,
    ACCELERATING,
    DEAD,
    EXPLODING,
  }
  Point2D position;
  Point2D head;
  double w = 25;
  double h = 45;
  PolarVector direction = new PolarVector(1, 0);
  PolarVector velocity = new PolarVector(2, 0);
  double maxSpeed = 12.0;
  double acceleration = 0.3;
  double deceleration = 0.15;
  int counter = 0;
  int lifeCounter = 0;
  int deathCounter = 0;
  Polygon shape;
  private ShipState state;

  public SpaceShip(Point2D position) {
    this.position = position;
    state = ShipState.DEFAULT;
    head = new Point2D.Double(position.getX(), position.getY() - h/2);
    int[] x = { 1,2,3 };
    int[] y = { 1,2,3 };
    shape = new Polygon(x, y, 3);
  }

  public void paint(Graphics2D g) {
    Point2D p1 = new Point2D.Double(0, - h/2);
    Point2D p2 = new Point2D.Double(+ w/2, + h/2);
    Point2D p3 = new Point2D.Double(- w/2, + h/2);
    Point2D p4 = new Point2D.Double(- w/4.0 - w/10.0, + h/4.0);
    Point2D p5 = new Point2D.Double(+ w/4.0 + w/10.0, + h/4.0);
    Point2D f1 = new Point2D.Double(+ w/8.0 + w/12.0, + h/4.0);
    Point2D f2 = new Point2D.Double(- w/8.0 - w/12.0, + h/4.0);
    Point2D f3 = new Point2D.Double(0, + h * 3/4.0);

    if(state == ShipState.EXPLODING) {
      explode(p1, p2, p3, p4, p5);
    }

    applyRotation(p1);
    applyRotation(p2);
    applyRotation(p3);
    applyRotation(p4);
    applyRotation(p5);
    applyRotation(f1);
    applyRotation(f2);
    applyRotation(f3);

    head.setLocation(p1.getX() + position.getX(), p1.getY() + position.getY());

    g.setColor(Color.white);
    if(lifeCounter < 80) {
      lifeCounter ++;
      if(lifeCounter % 10 == 0) {
        drawLine(g, p1, p2);
        drawLine(g, p1, p3);
        drawLine(g, p4, p5);
        if (state == ShipState.ACCELERATING) {
          counter++;
          if (counter == 3) {
            drawLine(g, f1, f3);
            drawLine(g, f2, f3);
            drawLine(g, f1, f2);
            counter = 0;
          }
        }
      }
    } else {
      drawLine(g, p1, p2);
      drawLine(g, p1, p3);
      drawLine(g, p4, p5);
      if (state == ShipState.ACCELERATING) {
        counter++;
        if (counter == 3) {
          drawLine(g, f1, f3);
          drawLine(g, f2, f3);
          drawLine(g, f1, f2);
          counter = 0;
        }
      }
    }

    int[] x = {  (int)(position.getX() + p1.getX()), (int)(position.getX() + p2.getX()),
        (int)(position.getX() + p3.getX()), (int)(position.getX() + p4.getX()), (int)(position.getX() + p5.getX())  };
    int[] y = {  (int)(position.getY() + p1.getY()), (int)(position.getY() + p2.getY()),
        (int)(position.getY() + p3.getY()), (int)(position.getY() + p4.getY()), (int)(position.getY() + p5.getY()) };
    shape = new Polygon(x, y, 5);
  }

  private void drawLine(Graphics2D g, Point2D p1, Point2D p2) {
    g.draw(new Line2D.Double(position.getX() + p1.getX(), position.getY() + p1.getY(),
        position.getX() + p2.getX(), position.getY() + p2.getY()));
  }

  private void applyRotation(Point2D p) {
    double x =  p.getX();
    double y = p.getY();

    double a = direction.a + Math.PI/2;
    p.setLocation(x * Math.cos(a) - y * Math.sin(a), x * Math.sin(a) + y * Math.cos(a));
  }

  public void setAngle(double angle) {
    direction.a = angle;
  }

  public double getAngle() {
    return direction.a;
  }

  public void accelerate() {
      PolarVector dv = new PolarVector(direction).scale(acceleration);
      velocity.add(dv);
      if (velocity.m > maxSpeed) {
        velocity.m = maxSpeed;
      }
  }

  public void decelerate(){
      velocity.m -= deceleration;
      if (velocity.m < 0) {
        velocity.m = 0;
      }
  }

  public void moveStep() {
    if(!(state == SpaceShip.ShipState.EXPLODING)) {
      double x = position.getX();
      double y = position.getY();
      double speedX = velocity.m * Math.cos(velocity.a);
      double speedY = velocity.m * Math.sin(velocity.a);
      position.setLocation(x + speedX, y + speedY);
    }
  }

  public void explode(Point2D p1, Point2D p2, Point2D p3, Point2D p4, Point2D p5) {
    state = ShipState.EXPLODING;
    if(deathCounter < 30) {
      deathCounter++;
      applyRandomMovement(p1);
      applyRandomMovement(p2);
      applyRandomMovement(p3);
      applyRandomMovement(p4);
      applyRandomMovement(p5);
    } else if (deathCounter == 30) {
      deathCounter = 0;
      state = ShipState.DEAD;
    }
  }

  private void applyRandomMovement(Point2D p) {
    double x = p.getX();
    double y = p.getY();

    x += Math.random() * 20 - 1;
    y += Math.random() * 20 - 1;

    p.setLocation(x, y);
  }

  public void shootMissile(ArrayList<Missile> missiles) {
    if(!(state == SpaceShip.ShipState.EXPLODING) && !(state == SpaceShip.ShipState.DEAD)) {
      Point2D a = new Point2D.Double(head.getX(), head.getY());
      Missile missile = new Missile(a, velocity.m, direction.a);
      if(missiles.size() < 4) {
        missiles.add(missile);
      }
    }
  }

  public void startAccelerating() {
    if(!isDead() && !isExploding()) {
      state = SpaceShip.ShipState.ACCELERATING;
    }
  }

  public void stopAccelerating() {
    if(!isDead() && !isExploding()) {
      state = SpaceShip.ShipState.DEFAULT;
    }
  }

  public void startExploding() {
    if(!isDead()) {
      state = ShipState.EXPLODING;
    }
  }

  public boolean isDead() {
    return state == ShipState.DEAD;
  }

  public boolean isExploding() {
    return state == ShipState.EXPLODING;
  }
}


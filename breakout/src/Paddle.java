import animators.ColorToColorAnimator;
import animators.OutFadeAnimator;
import animators.RainbowAnimator2;
import animators.TextParticle;
import org.age.util.String2D;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by PetruscaFamily on 2/18/2016.
 */
public class Paddle {
  private ArrayList<Point2D> bulletPositions;
  private ArrayList<Shape> bullets;
  private Point2D point;
  private Rectangle2D bounds;
  private int lengthFactor;
  private boolean isMagnetic = false;
  private boolean isGun = false;
  private int magnetCounter = -1;
  private int bulletCounter = 0;
  private RainbowAnimator2 anim;

  public Paddle(double x, double y) {
    point = new Point2D.Double(x, y);
    lengthFactor = 25;
    bounds = new Rectangle2D.Double(x - 2.5*lengthFactor, y, 5*lengthFactor, 25);
    anim = new RainbowAnimator2(0.1f);
    bulletPositions = new ArrayList<Point2D>();
    bullets = new ArrayList<Shape>();
  }

  public void draw(Graphics2D g) {
    if(isGun && bulletCounter <= 0) {
      isGun = false;
    }
    if(isGun) {
      double gunWidth = 3;
      double gunHeight = 9;
      Rectangle2D rightGun = new Rectangle2D.Double(getX() + 5*bounds.getWidth()/16 - gunWidth/2, getY() - gunHeight, gunWidth, gunHeight);
      Rectangle2D leftGun = new Rectangle2D.Double(getX() - 5*bounds.getWidth()/16 - gunWidth/2, getY() - gunHeight, gunWidth, gunHeight);
      g.setColor(Color.gray);
      g.fill(rightGun);
      g.fill(leftGun);
      g.setColor(Color.black);
      g.draw(rightGun);
      g.draw(leftGun);
    }
    drawBullets(g);

    updateBounds();
    g.setColor(Color.RED);
    g.fill(bounds);
    g.setColor(Color.BLACK);
    g.draw(bounds);

    if(isGun) {
      drawBulletCounter(g);
    }

    if(isMagnetic) {
      anim.animate(g);
      g.fill(new Rectangle2D.Double(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), 3));
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.00f));
    }
  }

  private void drawBullets(Graphics2D g) {
    bullets = new ArrayList<Shape>();
    for(Point2D point : bulletPositions) {
      Arc2D circleBullet = new Arc2D.Double(point.getX(), point.getY(), 3, 3, 0, 360, Arc2D.CHORD);
      g.setColor(Color.BLACK);
      g.draw(circleBullet);
      g.setColor(Color.red.darker());
      g.fill(circleBullet);
      bullets.add(circleBullet);
    }
    animateBullets();
    g.setColor(Color.BLACK);
  }

  private void drawBulletCounter(Graphics2D g) {
    String2D string = new String2D("" + bulletCounter, new Font(Font.SERIF, Font.BOLD, 16));
    g.setColor(new Color(82, 0, 1));
    g.fill(new Arc2D.Double(bounds.getCenterX() - 9, bounds.getCenterY() - 9, 19, 19, 0, 360, Arc2D.CHORD));
    g.setColor(new Color(255, 255, 255));
    string.paint(g, bounds.getCenterX(), bounds.getCenterY(), 1, 1, 0, 0, 0);
  }

  private void animateBullets() {
    double v = 15;
    for(int i = 0; i < bulletPositions.size(); i++) {
      Point2D newPos = new Point2D.Double(bulletPositions.get(i).getX(), bulletPositions.get(i).getY() - v);
      bulletPositions.set(i, newPos);
    }
  }

  public void updateBounds() {
    if(isMagnetic && magnetCounter < 600) {
      magnetCounter++;
    } else {
      magnetCounter = -1;
      isMagnetic = false;
    }
    bounds = new Rectangle2D.Double(point.getX() - 2.5*lengthFactor, point.getY(), 5*lengthFactor, 25);
  }

  public void increaseLength() {
    if(lengthFactor < 35) {
      lengthFactor += 5;
    }
  }

  public void decreaseLength() {
    if(lengthFactor > 15) {
      lengthFactor -= 5;
    }
  }

  public Rectangle2D getBounds() {
    return bounds;
  }

  public void setPosition(double x, double y) {
    point = new Point2D.Double(x, y);
    updateBounds();
  }

  public boolean isMagnetic() {
    return isMagnetic;
  }


  public double getX() {
    return point.getX();
  }

  public double getY() {
    return point.getY();
  }

  public void magnetize() {
    magnetCounter = 0;
    isMagnetic = true;
  }

  public int getLengthFactor() {
    return lengthFactor;
  }

  public void attachGun() {
    bulletCounter += 15;
    isGun = true;
  }

  public boolean isGun() {
    return isGun;
  }

  public void shootGun() {
    double gunWidth = 3;
    double gunHeight = 9;
    bulletPositions.add(new Point2D.Double(getX() + 5*bounds.getWidth()/16 - gunWidth/2, getY() - gunHeight));
    bulletPositions.add(new Point2D.Double(getX() - 5*bounds.getWidth()/16 - gunWidth/2, getY() - gunHeight));
    bulletCounter--;
  }

  public ArrayList<Shape> getBullets() {
    return bullets;
  }

  public void removeBullet(int i) {
    bulletPositions.remove(i);
    bullets.remove(i);
  }
}

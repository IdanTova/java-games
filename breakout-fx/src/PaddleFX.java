import animationsFX.RainbowAnimator2FX;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import org.age.math.PhysicsVector;

import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class PaddleFX {
  private double currentY;
  private ArrayList<Point2D> bulletPositions;
  private ArrayList<Shape> bullets;
  private Point2D point;
  private Rectangle2D bounds;
  private int lengthFactor;
  private boolean isMagnetic;
  private boolean isGun;
  private int magnetCounter = -1;
  private int bulletCounter = 0;
  private RainbowAnimator2FX anim;
  private List<FireFlyFX> fires;

  public PaddleFX(double x, double y) {
    currentY = 35*25 / 3;
    point = new Point2D(x, y);
    lengthFactor = 25;
    bounds = new Rectangle2D.Double(x - 2.5*lengthFactor, y, 5*lengthFactor, 25);
    anim = new RainbowAnimator2FX(0.1f);
    bulletPositions = new ArrayList<Point2D>();
    bullets = new ArrayList<Shape>();
    fires = new ArrayList<FireFlyFX>();
  }

  public void draw(GraphicsContext g) {
    if(isGun && bulletCounter <= 0) {
      isGun = false;
    }
    if(isGun) {
      double gunWidth = 3;
      double gunHeight = 9;
      Rectangle2D rightGun = new Rectangle2D.Double(getX() + 5*bounds.getWidth()/16 - gunWidth/2, getY() - gunHeight, gunWidth, gunHeight);
      Rectangle2D leftGun = new Rectangle2D.Double(getX() - 5*bounds.getWidth()/16 - gunWidth/2, getY() - gunHeight, gunWidth, gunHeight);
      g.setFill(Color.GRAY);
      g.fillRect(rightGun.getX(), rightGun.getY() - 0.5 + currentY, rightGun.getWidth(), rightGun.getHeight() + 1);
      g.fillRect(leftGun.getX(), leftGun.getY() - 0.5 + currentY, leftGun.getWidth(), leftGun.getHeight() + 1);
      g.setStroke(Color.WHITE);
      g.strokeRect(rightGun.getX(), rightGun.getY() - 0.5 + currentY, rightGun.getWidth(), rightGun.getHeight() + 1);
      g.strokeRect(leftGun.getX(), leftGun.getY() - 0.5 + currentY, leftGun.getWidth(), leftGun.getHeight() + 1);
    }

    GraphicsContext g2 = BreakoutRunFX.lowerCanvas.getGraphicsContext2D();
    drawBullets(g2);

    updateBounds();
    Color color = Color.MAGENTA;
    g.setStroke(color);
    g.setLineWidth(4);
    g.setLineJoin(StrokeLineJoin.ROUND);
    g.setLineCap(StrokeLineCap.BUTT);
    g.setFill(new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.15));
    g.fillRect(bounds.getX() + 3.5, bounds.getY() + 3.5 + currentY, bounds.getWidth() - 6, bounds.getHeight() - 6);
    g.strokeRect(bounds.getX() + 3.5, bounds.getY() + 3.5 + currentY, bounds.getWidth() - 6, bounds.getHeight() - 6);

    if(isGun) {
      drawBulletCounter(g);
    }

    if(isMagnetic) {
      anim.animate(g);
      Rectangle2D rect = new Rectangle2D.Double(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), 3);
      g.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }
  }

  private void drawBullets(GraphicsContext g) {
    g.setLineCap(StrokeLineCap.SQUARE);
    g.setLineWidth(1.0f);
    g.setLineJoin(StrokeLineJoin.MITER);
    bullets = new ArrayList<Shape>();
    for(Point2D point : bulletPositions) {
      int radius = 3;
      Arc2D bullet = new Arc2D.Double(point.getX(), point.getY(), radius*2, radius*2, 0, 360, Arc2D.CHORD);
      Color col = Color.CORAL;
      g.setFill(new Color(col.darker().getRed(), col.darker().getGreen(), col.darker().getBlue(), 0.8));
      g.fillArc(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight(), 0, 360, ArcType.CHORD);
      int random = (int)(Math.random()*4);
      if(random == 0) {
        PhysicsVector veloc = new PhysicsVector(2, (float) (Math.PI + Math.random() * Math.PI / 4 - Math.PI / 8));
        PhysicsVector accel = new PhysicsVector(0.2, (float) (Math.PI));
        FireFlyFX fx = new FireFlyFX(Color.TEAL.brighter(), bullet.getX() + radius, bullet.getY() + radius, 1, veloc, accel, 10);
        fires.add(fx);
      }
      bullets.add(bullet);
    }
    animateBullets();
    g.setStroke(Color.BLACK);
    g.setFill(Color.BLACK);
    for (FireFlyFX fire : fires) {
      fire.animate(g);
    }
    for(int i = 0; i < fires.size(); i++) {
      if(!fires.get(i).isAnimating()) {
        fires.remove(i);
      }
    }
  }

  private void drawBulletCounter(GraphicsContext g) {
    g.setLineWidth(2);
    g.setStroke(Color.WHITE);
    g.strokeArc(bounds.getCenterX() - 9, bounds.getCenterY() - 9 + currentY, 19, 19, 0, 360, ArcType.CHORD);
    g.setFill(Color.WHITE);
    if(bulletCounter < 10) {
      g.setFont(Font.loadFont(getClass().getResource("fonts/AndroidInsomnia.ttf").toExternalForm(), 16));
      g.fillText("" + bulletCounter, bounds.getCenterX() - 4, bounds.getCenterY() + 5 + currentY, 15);
    } else {
      g.setFont(Font.loadFont(getClass().getResource("fonts/AndroidInsomnia.ttf").toExternalForm(), 15));
      g.fillText("" + bulletCounter, bounds.getCenterX() - 6.5, bounds.getCenterY() + 5 + currentY, 15);
    }
  }

  private void animateBullets() {
    double v = 15;
    for(int i = 0; i < bulletPositions.size(); i++) {
      Point2D newPos = new Point2D(bulletPositions.get(i).getX(), bulletPositions.get(i).getY() - v);
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
    point = new Point2D(x, y);
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

  public double getCurrentY() {
    return currentY;
  }

  public void shootGun() {
    if(currentY == 0) {
      double gunWidth = 3;
      double gunHeight = 9;
      bulletPositions.add(new Point2D(getX() + 5 * bounds.getWidth() / 16 - gunWidth / 2, getY() - gunHeight));
      bulletPositions.add(new Point2D(getX() - 5 * bounds.getWidth() / 16 - gunWidth / 2, getY() - gunHeight));
      bulletCounter--;
    }
  }

  public ArrayList<Shape> getBullets() {
    return bullets;
  }

  public void removeBullet(int i) {
    bulletPositions.remove(i);
    bullets.remove(i);
  }

  public void incrementForStartUp(double increment) {
    if(currentY > 0) {
      currentY -= increment;
    } else if(bounds.getY() - increment <= 0) {
      currentY = 0;
    } else {
      currentY = 0;
    }
  }
}

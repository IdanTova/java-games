import animationsFX.InOutFadeAnimatorFX;
import animationsFX.PhysicsAnimatorFX;
import animationsFX.RainbowAnimator2FX;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import org.age.math.PhysicsVector;

import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static java.lang.Math.PI;
import static java.lang.Math.random;

public class BallFX {
  private double currentY;
  private boolean shouldAnimate;
  private int radius = 7;
  private double velX = 0;
  private double velY = 15;
  private double totalVel;
  private double velXSave = -1;
  private double velYSave = -1;
  private int powerUpCounter = -1;
  private Point2D point;
  private Arc2D bounds;
  private Arc2D nextBounds;
  private Arc2D lastBounds;
  private InOutFadeAnimatorFX anim;
  private RainbowAnimator2FX colorAnim1;
  private RainbowAnimator2FX colorAnim2;
  private BallStates ballState;
  private boolean isStuck;
  private boolean doSpawnAnimation;
  private PaddleFX paddle;
  private double diffX = 0;
  private ArrayList<Line2D> lines = new ArrayList<Line2D>();
  private int tailLength = 60;
  private PhysicsAnimatorFX physics;
  private boolean isAnimatingOut;
  private boolean shouldDrawTrail = true;

  public static enum BallStates {
    NORMAL,
    POWER_BALL,
    DRUNK_BALL,
    DRUNK_POWER_BALL
  }

  public BallFX(double x, double y, boolean doSpawnAnimation) {
    currentY = 35*25 / 3;
    point = new Point2D.Double(x, y);
    totalVel = Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));
    bounds = new Arc2D.Double(x - radius/2.0, y - radius/2.0, radius * 2, radius * 2, 0, 360, Arc2D.CHORD);
    nextBounds = new Arc2D.Double(point.getX() - radius/2.0 + velX, point.getY() - radius/2.0 + velY, radius * 2, radius * 2, 0, 360, Arc2D.CHORD);
    anim = new InOutFadeAnimatorFX(1.0f, 0.0499f, InOutFadeAnimatorFX.Fade.FADE_OUT, 100);
    colorAnim1 = new RainbowAnimator2FX(0.05f);
    colorAnim2 = new RainbowAnimator2FX(0.0002f);
    ballState = BallStates.NORMAL;
    this.doSpawnAnimation = doSpawnAnimation;
    PhysicsVector vel = new PhysicsVector(random() * 5 + 5, (float)(random() * 7*PI/4 + PI/8));
    PhysicsVector acc = new PhysicsVector(0, (float)(PI));
    PhysicsVector jer = new PhysicsVector(0.05, (float)(PI));
    physics = new PhysicsAnimatorFX(vel, acc, jer);
  }

  public BallFX(double x, double y, double vX, double vY, boolean doSpawnAnimation) {
    velX = vX;
    velY = vY;
    point = new Point2D.Double(x, y);
    totalVel = Math.sqrt(Math.pow(vX, 2) + Math.pow(vY, 2));
    bounds = new Arc2D.Double(x - radius/2.0, y - radius/2.0, radius * 2, radius * 2, 0, 360, Arc2D.CHORD);
    nextBounds = new Arc2D.Double(point.getX() - radius/2.0 + velX, point.getY() - radius/2.0 + velY, radius * 2, radius * 2, 0, 360, Arc2D.CHORD);
    anim = new InOutFadeAnimatorFX(1.0f, 0.0499f, InOutFadeAnimatorFX.Fade.FADE_OUT, 100);
    colorAnim1 = new RainbowAnimator2FX(0.05f);
    colorAnim2 = new RainbowAnimator2FX(0.0002f);
    ballState = BallStates.NORMAL;
    this.doSpawnAnimation = doSpawnAnimation;
    PhysicsVector vel = new PhysicsVector(random() * 5 + 5, (float)(random() * 7*PI/4 + PI/8));
    PhysicsVector acc = new PhysicsVector(0.75, (float)(PI));
    PhysicsVector jer = new PhysicsVector(0.05, (float)(PI));
    physics = new PhysicsAnimatorFX(vel, acc, jer);
  }

  public void draw(GraphicsContext g) {
    double x = physics.getX();
    double y = physics.getY();
    if(!anim.isAnimating() && shouldDrawTrail) {
      paintTrail(BreakoutRunFX.lowerCanvas.getGraphicsContext2D());
    }
    if(doSpawnAnimation && BreakoutScreenFX.shouldRun && shouldAnimate) {
      anim.animate(g);
    } else if(doSpawnAnimation && BreakoutScreenFX.shouldRun) {
      // keep this here ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^
    } else if (BreakoutScreenFX.shouldRun) {
      anim.setAnimating(false);
    }
    g.setFill(new Color(0/255.0f, 156/255.0f, 209/255.0f, 1.0f));
    if(ballState == BallStates.POWER_BALL || ballState == BallStates.DRUNK_POWER_BALL) {
      colorAnim1.animate(g);
    }
    g.fillArc(bounds.getX() + x, bounds.getY() + currentY + y, bounds.getWidth(), bounds.getHeight(), 0, 360, ArcType.CHORD);
    g.setGlobalAlpha(1.0d);
    if(isAnimatingOut) {
      animateOut();
    }
  }

  private void paintTrail(GraphicsContext g) {
    colorAnim2.animate(g);
    float thickness = 2 * radius - 1;
    float finalThickness = 0;
    float alpha = 0.55f;
    g.setGlobalAlpha(1.0f);
    for (int i = 0; i < lines.size(); i++) {
      g.setLineWidth(thickness);
      g.setLineCap(StrokeLineCap.ROUND);
      g.setLineJoin(StrokeLineJoin.ROUND);
      double red = colorAnim2.getColor().getRed() - i/1.5/lines.size();
      if(red < 0) {
        red = 0;
      }
      double green = colorAnim2.getColor().getGreen() - i/1.5/lines.size();
      if(green < 0) {
        green = 0;
      }
      double blue = colorAnim2.getColor().getBlue() - i/1.5/lines.size();
      if(blue < 0) {
        blue = 0;
      }
      g.setStroke(new Color(red, green, blue, alpha));
      g.strokeLine(lines.get(i).getX1(), lines.get(i).getY1(), lines.get(i).getX2(), lines.get(i).getY2());
      thickness -= (radius - finalThickness) / tailLength;
      alpha -= 0.55f / tailLength;
    }
  }

  public void updateBounds() {
    if(ballState == BallStates.NORMAL) {
      radius = 7;
    } else if(ballState == BallStates.POWER_BALL || ballState == BallStates.DRUNK_POWER_BALL) {
      radius = 10;
    }
    if(currentY == 0) {
      shouldAnimate = true;
    }
    if(powerUpCounter >= 0 && ballState != BallStates.NORMAL) {
      if(powerUpCounter == 400) {
        if(ballState == BallStates.DRUNK_BALL || ballState == BallStates.DRUNK_POWER_BALL) {
          velX = velXSave;
          velY = velYSave;
          totalVel = Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));
          velXSave = -1;
          velYSave = -1;
        }
        ballState = BallStates.NORMAL;
      }
      powerUpCounter++;
    }

    bounds = new Arc2D.Double(point.getX() - radius/2.0, point.getY() - radius/2.0, radius * 2, radius * 2, 0, 360, Arc2D.CHORD);
    nextBounds = new Arc2D.Double(point.getX() - radius/2.0 + velX, point.getY() - radius/2.0 + velY, radius * 2, radius * 2, 0, 360, Arc2D.CHORD);
  }

  public void updateWithPaddle(PaddleFX paddle) {
    this.paddle = paddle;
  }

  public void moveStep() {
    lastBounds = new Arc2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight(), 0, 360, Arc2D.CHORD);
    if(!anim.isAnimating() && !isStuck) {
      point = new Point2D.Double(point.getX() + velX, point.getY() + velY);
    } else if(isStuck) {
      point = new Point2D.Double(paddle.getX() + diffX, paddle.getY() - 9);
    }
    if(ballState == BallStates.DRUNK_BALL || ballState == BallStates.DRUNK_POWER_BALL) {
      if(velXSave == -1 && velYSave == -1) {
        velXSave = velX;
        velYSave = velY;
      }
      velX += 2.49*Math.sin(powerUpCounter);
      velY += 2.49*Math.cos(powerUpCounter);
      if(totalVel > 20 || totalVel < 10) {
        velX = 15*Math.sin(powerUpCounter);
        velY = 15*Math.cos(powerUpCounter);
      }
      totalVel = Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));
    }

    Line2D line = null;
    if(isStuck) {
      line = new Line2D.Double(bounds.getCenterX(), bounds.getCenterY(), bounds.getCenterX(), bounds.getCenterY());
    } else {
      line = new Line2D.Double(lastBounds.getCenterX(), lastBounds.getCenterY(), bounds.getCenterX(), bounds.getCenterY());
    }
    addLine(line);
    if(lines.size() >= tailLength) {
      lines = new ArrayList<Line2D>(lines.subList(0, tailLength));
    }
    updateBounds();
  }

  private void addLine(Line2D line) {
    double xLength = BreakoutScreenFX.mouseDisplacement;
    double yLength = 0;
    if(!isStuck) {
      yLength = velY;
      xLength = velX;
    }
    double x = line.getX1();
    double y = line.getY1();
    int resolution = 7;
    for(int i = 0; i <= resolution; i++) {
      lines.add(0, new Line2D.Double(x + i*xLength/ resolution, y + i*yLength/ resolution, x + (i+1)*xLength/ resolution, y + (i+1)*yLength/ resolution));
    }
  }

  public void decreaseSpeed() {
    double incrementValX = 4 * velX/(Math.abs(velX) + Math.abs(velY));
    double incrementValY = 4 * velY/(Math.abs(velX) + Math.abs(velY));
    velX -= incrementValX;
    velY -= incrementValY;
    totalVel = Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));
    if(totalVel < 10) {
      velX += incrementValX;
      velY += incrementValY;
      totalVel = Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));
    }
  }

  public void increaseSpeed() {
    double incrementValX = 4 * velX/(Math.abs(velX) + Math.abs(velY));
    double incrementValY = 4 * velY/(Math.abs(velX) + Math.abs(velY));
    velX += incrementValX;
    velY += incrementValY;
    totalVel = Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));
    if(totalVel > 30) {
      velX -= incrementValX;
      velY -= incrementValY;
      totalVel = Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));
    }
  }

  public void stick(double diffX) {
    isStuck = true;
    this.diffX = diffX;
  }

  public void unStick() {
    isStuck = false;
  }

  public Arc2D getBounds() {
    return bounds;
  }

  public boolean isStuck() {
    return isStuck;
  }

  public Arc2D getNextBounds() {
    return nextBounds;
  }

  public double getX() {
    return point.getX();
  }

  public double getY() {
    return  point.getY();
  }

  public double getNextX() {
    return getX() + velX;
  }

  public double getNextY() {
    return getY() + velY;
  }

  public void deflect(double diffX, double reference) {
    double angle = 3*Math.PI/8;
    velX = totalVel * Math.sin(diffX/reference * angle);
    velY = -totalVel * Math.cos(diffX/reference * angle);
  }

  public void flipRandomlyVelY() {
    velY = -velY + (0.1)*(Math.random() - 0.5); // FiXME make me better
  }

  public void flipRandomlyVelX() {
    velX = -velX + (0.1)*(Math.random() - 0.5); // FiXME make me better
  }

  public void flipVelX() {
    velX = -velX;
  }

  public void flipVelY() {
    velY = -velY;
  }

  public InOutFadeAnimatorFX getAnim() {
    return anim;
  }

  public BallStates getBallState() {
    return ballState;
  }

  public void setBallState(BallStates ballState) {
    powerUpCounter = 0;
    this.ballState = ballState;
  }

  public double getVelX() {
    return velX;
  }

  public double getVelY() {
    return velY;
  }

  public double getSecondLastX() {
    return getX() - 2*velX;
  }

  public double getSecondLastY() {
    return getY() - 2*velY;
  }

  public double getThirdLastX() {
    return getX() - 3*velX;
  }

  public double getThirdLastY() {
    return getY() - 3*velY;
  }

  public double getCurrentY() {
    return currentY;
  }

  public boolean isDone() {
    return currentY == 0;
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

  public void animateOut() {
    if(physics.getCounter() > 35) {
      physics.animate();
      shouldDrawTrail = false;
    }
    physics.incrementCounter();

    if(velX > 0) {
      velX -= 0.5;
      if(velX < 0) {
        velX = 0;
      }
    } else if(velX < 0) {
      velX += 0.5;
      if(velX > 0) {
        velX = 0;
      }
    }
    if(velY > 0) {
      velY -= 0.5;
      if(velY < 0) {
        velY = 0;
      }
    } else if(velY < 0) {
      velY += 0.5;
      if(velY > 0) {
        velY = 0;
      }
    }
  }

  public void startAnimateOut() {
    isAnimatingOut = true;
  }

  public boolean isFinishedExiting() {
    return bounds.getY() + physics.getY() > BreakoutRunFX.upperCanvas.getHeight() + 200;
  }

  public PhysicsAnimatorFX getPhysics() {
    return physics;
  }
}

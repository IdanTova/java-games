import animators.InOutFadeAnimator;
import animators.RainbowAnimator2;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by PetruscaFamily on 2/18/2016.
 */
public class Ball {
  private int radius = 7;
  private double velX = 0;
  private double velY = 20;
  private double totalVel;
  private double velXSave = -1;
  private double velYSave = -1;
  private int powerUpCounter = -1;
  private Point2D point;
  private Arc2D bounds;
  private Arc2D nextBounds;
  private Arc2D lastBounds;
  private InOutFadeAnimator anim;
  private RainbowAnimator2 colorAnim1;
  private RainbowAnimator2 colorAnim2;
  private BallStates ballState;
  private boolean isStuck;
  private boolean doSpawnAnimation;
  private Paddle paddle;
  private double diffX = 0;
  private ArrayList<Line2D> lines = new ArrayList<Line2D>();
  private int tailLength = 60;


  public static enum BallStates {
    NORMAL,
    POWER_BALL,
    DRUNK_BALL,
    DRUNK_POWER_BALL
  }

  public Ball(double x, double y, boolean doSpawnAnimation) {
    point = new Point2D.Double(x, y);
    totalVel = Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));
    bounds = new Arc2D.Double(x - radius/2.0, y - radius/2.0, radius * 2, radius * 2, 0, 360, Arc2D.CHORD);
    nextBounds = new Arc2D.Double(point.getX() - radius/2.0 + velX, point.getY() - radius/2.0 + velY, radius * 2, radius * 2, 0, 360, Arc2D.CHORD);
    anim = new InOutFadeAnimator(1.0f, 0.0499f, InOutFadeAnimator.Fade.FADE_OUT, 100);
    colorAnim1 = new RainbowAnimator2(0.05f);
    colorAnim2 = new RainbowAnimator2(0.0002f);
    ballState = BallStates.NORMAL;
    this.doSpawnAnimation = doSpawnAnimation;
  }

  public Ball(double x, double y, double vX, double vY, boolean doSpawnAnimation) {
    velX = vX;
    velY = vY;
    point = new Point2D.Double(x, y);
    totalVel = Math.sqrt(Math.pow(vX, 2) + Math.pow(vY, 2));
    bounds = new Arc2D.Double(x - radius/2.0, y - radius/2.0, radius * 2, radius * 2, 0, 360, Arc2D.CHORD);
    nextBounds = new Arc2D.Double(point.getX() - radius/2.0 + velX, point.getY() - radius/2.0 + velY, radius * 2, radius * 2, 0, 360, Arc2D.CHORD);
    anim = new InOutFadeAnimator(1.0f, 0.0499f, InOutFadeAnimator.Fade.FADE_OUT, 100);
    colorAnim1 = new RainbowAnimator2(0.05f);
    colorAnim2 = new RainbowAnimator2(0.0002f);
    ballState = BallStates.NORMAL;
    this.doSpawnAnimation = doSpawnAnimation;
  }

  public void draw(Graphics2D g) {
    if(!anim.isAnimating()) {
      paintTrail(g);
    }
    if(doSpawnAnimation) {
      anim.animate(g);
    } else {
      anim.setAnimating(false);
    }
    g.setColor(new Color(0, 156, 209));
    if(ballState == BallStates.POWER_BALL || ballState == BallStates.DRUNK_POWER_BALL) {
      colorAnim1.animate(g);
    }
    g.fill(bounds);
    g.setColor(Color.BLACK);
    g.draw(bounds);
  }

  private void paintTrail(Graphics2D g) {
    colorAnim2.animate(g);
    float thickness = 2 * radius - 1;
    float finalThickness = 0;
    float alpha = 0.6f;
    for (int i = 0; i < lines.size(); i++) {
      g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
      Color color = new Color(colorAnim2.getColor().getRed()/255.0f, colorAnim2.getColor().getGreen()/255.0f, colorAnim2.getColor().getBlue()/255.0f, alpha);
      g.setPaint(color);
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
      g.draw(lines.get(i));
      thickness -= (radius - finalThickness) / tailLength;
      alpha -= 0.6f / tailLength;
    }
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    g.setStroke(new BasicStroke());
  }

  public void updateBounds() {
    if(ballState == BallStates.NORMAL) {
      radius = 7;
    } else if(ballState == BallStates.POWER_BALL || ballState == BallStates.DRUNK_POWER_BALL) {
      radius = 10;
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

  public void updateWithPaddle(Paddle paddle) {
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
    double xLength = BreakoutRun.mouseDisplacement.getX();
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

  public void flipVelX() {
    velX = -velX;
  }

  public void flipVelY() {
    velY = -velY;
  }

  public InOutFadeAnimator getAnim() {
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
}

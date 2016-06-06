package animationsFX;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.*;

/**
 * Created by PetruscaFamily on 4/10/2016.
 */
public class TransformationAnimatorFX extends AnimatorFX {
  private double x, y;
  private double scaleX, scaleY;
  private double anchorX, anchorY;
  private double theta;
  private double dScaleX, dScaleY;
  private double dx, dy;
  private double dTheta;
  private int counter;
  private int counterLimit;
  private Affine t;

  public TransformationAnimatorFX(double x, double y, double scaleX, double scaleY, double anchorX, double anchorY, double theta, double dScaleX, double dScaleY, double dx, double dy, double dTheta, int counterLimit) {
    this.x = x;
    this.y = y;
    this.scaleX = scaleX;
    this.scaleY = scaleY;
    this.anchorX = anchorX;
    this.anchorY = anchorY;
    this.theta = theta;
    this.dScaleX = dScaleX;
    this.dScaleY = dScaleY;
    this.dx = dx;
    this.dy = dy;
    this.dTheta = dTheta;
    this.counterLimit = counterLimit;
  }

  public static TransformationAnimatorFX getScaleInstance(double sx, double sy, double dSX, double dSY, int counterLimit) {
    return new TransformationAnimatorFX(0, 0, sx, sy, 0, 0, 0, dSX, dSY, 0, 0, 0, counterLimit);
  }

  public static TransformationAnimatorFX getScaleInstance(double sx, double sy, double dSX, double dSY, double anchorX, double anchorY, int counterLimit) {
    return new TransformationAnimatorFX(0, 0, sx, sy, anchorX, anchorY, 0, dSX, dSY, 0, 0, 0, counterLimit);
  }

  public static TransformationAnimatorFX getTranslateInstance(double x, double y, double dX, double dY, int counterLimit) {
    return new TransformationAnimatorFX(0, 0, x, y, 0, 0, 0, dX, dY, 0, 0, 0, counterLimit);
  }

  public static TransformationAnimatorFX getRotationInstance(double theta, double anchorX, double anchorY, double dTheta, int counterLimit) {
    return new TransformationAnimatorFX(0, 0, 0, 0, 0, 0, theta, anchorX, anchorY, 0, 0, dTheta, counterLimit);
  }

  public static TransformationAnimatorFX getRotationInstance(double theta, double dTheta, int counterLimit) {
    return new TransformationAnimatorFX(0, 0, 0, 0, 0, 0, theta, 0, 0, 0, 0, dTheta, counterLimit);
  }

  public void animate(GraphicsContext g) {
    scaleX += dScaleX;
    scaleY += dScaleY;
    theta += dTheta;
    x += dx;
    y += dy;
    if(scaleX < 0) {
      scaleX = 0;
    }
    if(scaleY < 0) {
      scaleY = 0;
    }

    t = new Affine();
    t.appendTranslation(x, y);
    t.appendRotation(theta, anchorX, anchorY);
    t.appendScale(scaleX, scaleY);
    g.save();
    g.setTransform(t);

    if(counter > counterLimit) {
      isAnimating = false;
    }
    counter++;
  }

  public Affine getTransform() {
    return t;
  }

  public void applyTransform(GraphicsContext g) {
    g.setTransform(t.getMxx(), t.getMyx(), t.getMxy(), t.getMyy(), t.getTx(), t.getTy());
  }

  public void setAnchor(double anchorX, double anchorY) {
    this.anchorY = anchorY;
    this.anchorX = anchorX;
  }

  public void setDTheta(double dv) {
    dTheta = dv;
  }

  public void setDX(double dv) {
    dx = dv;
  }

  public void setDY(double dv) {
    dy = dv;
  }

  public void setDScaleX(double dv) {
    dScaleX = dv;
  }

  public void setDScaleY(double dv) {
    dScaleY = dv;
  }
}

package animators;

import org.age.util.String2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

/**
 * Created by PetruscaFamily on 3/13/2016.
 */
public class TextParticle extends Animator {
  private ArrayList<Animator> anims = new ArrayList<Animator>();
  private String2D string;
  private double x, y;
  private double scaleX, scaleY;
  private double anchorX, anchorY;
  private double theta;
  private double dScaleX, dScaleY;
  private double dx, dy;
  private double dTheta;
  private int counter;
  private int counterLimit;

  public TextParticle(String2D string, double x, double y, double scaleX, double scaleY, double anchorX, double anchorY, double theta, int counterLimit) {
    this.string = string;
    this.x = x;
    this.y = y;
    this.scaleX = scaleX;
    this.scaleY = scaleY;
    this.anchorX = anchorX;
    this.anchorY = anchorY;
    this.theta = theta;
    this.counterLimit = counterLimit;
  }

  public void animate(Graphics2D g) {
    scaleX += dScaleX;
    scaleY += dScaleY;
    theta += dTheta;
    x += dx;
    y += dy;

    AffineTransform scaleTrans = AffineTransform.getScaleInstance(scaleX, scaleY);
    AffineTransform rotateTrans = AffineTransform.getRotateInstance(theta, anchorX, anchorY);
    AffineTransform translateTrans = AffineTransform.getTranslateInstance(x, y);
    for(Animator anim : anims) {
      anim.animate(g);
    }
    string.paint(g, scaleTrans, rotateTrans, translateTrans);
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    counter++;
    if(counter >= counterLimit) {
      isAnimating = false;
    }
  }

  public void setAnchorX(double anchorX, double anchorY) {
    this.anchorY = anchorY;
    this.anchorX = anchorX;
  }

  public void add(Animator anim) {
    anims.add(anim);
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

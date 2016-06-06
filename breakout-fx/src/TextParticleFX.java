import animationsFX.AnimatorFX;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import java.util.ArrayList;

public class TextParticleFX extends AnimatorFX {
  private ArrayList<AnimatorFX> anims = new ArrayList<AnimatorFX>();
  private String string;
  private double x, y;
  private double scaleX, scaleY;
  private double anchorX, anchorY;
  private double theta;
  private double dScaleX, dScaleY;
  private double dx, dy;
  private double dTheta;
  private int counter;
  private int counterLimit;
  private Font font;

  public TextParticleFX(String string, Font font, double x, double y, double scaleX, double scaleY, double anchorX, double anchorY, double theta, int counterLimit) {
    this.string = string;
    this.x = x;
    this.y = y;
    this.scaleX = scaleX;
    this.scaleY = scaleY;
    this.anchorX = anchorX;
    this.anchorY = anchorY;
    this.theta = theta;
    this.counterLimit = counterLimit;
    this.font = font;
  }

  public void animate(GraphicsContext g) {
    scaleX += dScaleX;
    scaleY += dScaleY;
    theta += dTheta;
    x += dx;
    y += dy;

    for (AnimatorFX anim : anims) {
      anim.animate(g);
    }

    g.save();
    Affine affine = new Affine();
    affine.appendTranslation(x,y);
    affine.appendRotation(theta, anchorX, anchorY);
    affine.appendScale(scaleX, scaleY);
    g.setTransform(affine);
    g.setFont(font);
    g.fillText(string, 0, 0);
    g.restore();

    g.setGlobalAlpha(1.0f);
    counter++;
    if (counter >= counterLimit) {
      isAnimating = false;
    }
  }

  public void setAnchor(double anchorX, double anchorY) {
    this.anchorY = anchorY;
    this.anchorX = anchorX;
  }

  public void add(AnimatorFX anim) {
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

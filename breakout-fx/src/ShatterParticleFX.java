import animationsFX.AnimatorFX;
import animationsFX.OutFadeAnimatorFX;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.MotionBlur;
import javafx.scene.paint.Color;
import org.age.math.PhysicsVector;

import static java.lang.Math.*;

public class ShatterParticleFX extends AnimatorFX {
  private double x, y;
  private PhysicsVector position;
  private PhysicsVector acceleration;
  private PhysicsVector velocity;
  private double length;
  private double width;
  private Color color;
  private double timeCounter;
  private OutFadeAnimatorFX anim;

  public ShatterParticleFX(BreakBoxFX box) {
    color = box.getColor();
    position = new PhysicsVector(box.getBounds().getCenterX(), box.getBounds().getCenterY());
//    velocity = new PhysicsVector(random() * 5 + 5, (float)(random() * (-(3*PI/2 + PI/4)))); // FIXME to get rid of the
//    velocity = new PhysicsVector(random() * 5 + 5, (float)(random() * 3*PI/2 + PI/4));
    velocity = new PhysicsVector(random() * 5 + 5, (float)(random() * 7*PI/4 + PI/8));
    acceleration = new PhysicsVector(0.75, (float)(PI));
    length = random() * 55 + 5;
    width = random() * 2 + 3;
    anim = new OutFadeAnimatorFX(0.01f, 0.75f);
  }

  public void animate(GraphicsContext g) {
    anim.animate(g);
    g.setFill(color);
    double currentWidth = width * (g.getGlobalAlpha() + 0.3);
    double currentLength = 2*length * (1 - g.getGlobalAlpha());

    x = 0.5 * acceleration.getXComp() * pow(timeCounter, 2) + velocity.getXComp() * timeCounter + position.getXComp();
    y = 0.5 * acceleration.getYComp() * pow(timeCounter, 2) + velocity.getYComp() * timeCounter + position.getYComp();
    double nextX = 0.5 * acceleration.getXComp() * pow(timeCounter + 1, 2) + velocity.getXComp() * (timeCounter + 1) + position.getXComp();
    double nextY = 0.5 * acceleration.getYComp() * pow(timeCounter + 1, 2) + velocity.getYComp() * (timeCounter + 1) + position.getYComp();
    double t = -atan2((nextY - y), (nextX - x)) + PI/2;
    double[] xArr = { x, x + currentWidth * cos(t), x + currentWidth * cos(t) + currentLength * sin(t), x + currentLength * sin(t) };
    double[] yArr = { y, y - currentWidth * sin(t), y - currentWidth * sin(t) + currentLength * cos(t), y + currentLength * cos(t) };
    g.fillPolygon(xArr, yArr, 4);
    g.setGlobalAlpha(1);

    timeCounter ++;
  }

  public boolean isAnimating() {
    return anim.isAnimating();
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }
}

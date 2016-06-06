import animationsFX.AnimatorFX;
import animationsFX.OutFadeAnimatorFX;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import org.age.math.PhysicsVector;

import java.util.ArrayList;

public class FireFlyFX extends AnimatorFX {
  private Color color;
  private double lifeSpan;
  private double x, y;
  private double radius;
  private PhysicsVector velocity;
  private PhysicsVector acceleration;
  private OutFadeAnimatorFX out;
  private ArrayList<AnimatorFX> anims;
  private int counter;

  FireFlyFX(Color color, double x, double y, double radius, PhysicsVector velocity, PhysicsVector acceleration, double lifeSpan) {
    this.color = color;
    this.lifeSpan = lifeSpan;
    this.x = x;
    this.y = y;
    this.velocity = velocity;
    this.acceleration = acceleration;
    this.radius = radius;
    out = new OutFadeAnimatorFX(0.05f);
    anims = new ArrayList<AnimatorFX>();
  }

  FireFlyFX(Color color, double x, double y, PhysicsVector velocity, PhysicsVector acceleration, double dAlpha, double lifeSpan) {
    this.color = color;
    this.lifeSpan = lifeSpan;
    this.x = x;
    this.y = y;
    this.velocity = velocity;
    this.acceleration = acceleration;
    radius = 1 * Math.random() + 1;
    out = new OutFadeAnimatorFX((float)dAlpha);
    anims = new ArrayList<AnimatorFX>();
  }

  FireFlyFX(Color color, double x, double y, PhysicsVector velocity, PhysicsVector acceleration, double lifeSpan) {
    this.color = color;
    this.lifeSpan = lifeSpan;
    this.x = x;
    this.y = y;
    this.velocity = velocity;
    radius = 1 * Math.random() + 1;
    this.acceleration = acceleration;
    out = new OutFadeAnimatorFX(0.05f);
    anims = new ArrayList<AnimatorFX>();
  }

  FireFlyFX(Color color, double x, double y, double radius, PhysicsVector velocity, PhysicsVector acceleration, double dAlpha, double lifeSpan) {
    this.color = color;
    this.lifeSpan = lifeSpan;
    this.x = x;
    this.y = y;
    this.velocity = velocity;
    this.radius = radius;
    this.acceleration = acceleration;
    out = new OutFadeAnimatorFX((float)dAlpha);
    anims = new ArrayList<AnimatorFX>();
  }

  public void animate(GraphicsContext g) {
    for (AnimatorFX anim : anims) {
      anim.animate(g);
    }
    if(counter > lifeSpan/4) {
      out.animate(g);
    }

    g.setFill(color);
    g.fillArc(x - radius, y - radius, radius*2 , radius*2, 0, 360, ArcType.CHORD);

    update();
    counter ++;
  }

  private void update() {
    x += velocity.getXComp();
    y += velocity.getYComp();
    velocity.incrementXComp(acceleration.getXComp());
    velocity.incrementYComp(acceleration.getYComp());
  }

  public void addAnimator(AnimatorFX anim) {
    anims.add(anim);
  }

  public boolean isAnimating() {
    return out.isAnimating();
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }
}
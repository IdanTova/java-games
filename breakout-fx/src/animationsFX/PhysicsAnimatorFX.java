package animationsFX;

import org.age.math.PhysicsVector;

public class PhysicsAnimatorFX {
  private boolean isAnimating = true;
  private PhysicsVector pos;
  private PhysicsVector vel;
  private PhysicsVector acc;
  private PhysicsVector jer;
  private int counter = 0;

  public PhysicsAnimatorFX(PhysicsVector pos, PhysicsVector vel, PhysicsVector acc, PhysicsVector jer) {
    this.pos = pos;
    this.vel = vel;
    this.acc = acc;
    this.jer = jer;
  }

  public PhysicsAnimatorFX(PhysicsVector vel, PhysicsVector acc, PhysicsVector jer) {
    pos = new PhysicsVector(0, 0);
    this.vel = vel;
    this.acc = acc;
    this.jer = jer;
  }

  public void animate() {
    if(isAnimating) {
      pos.increment(vel);
      vel.increment(acc);
      acc.increment(jer);
      counter++;
    }
  }

  public double getX() {
    return pos.getXComp();
  }

  public double getY() {
    return pos.getYComp();
  }

  public PhysicsVector getPos() {
    return pos;
  }

  public void setPos(PhysicsVector pos) {
    this.pos = pos;
  }

  public PhysicsVector getVel() {
    return vel;
  }

  public void setVel(PhysicsVector vel) {
    this.vel = vel;
  }

  public PhysicsVector getAcc() {
    return acc;
  }

  public void setAcc(PhysicsVector acc) {
    this.acc = acc;
  }

  public PhysicsVector getJer() {
    return jer;
  }

  public void setJer(PhysicsVector jer) {
    this.jer = jer;
  }

  public void stop() {
    isAnimating = false;
  }

  public void start() {
    isAnimating = true;
  }

  public boolean isAnimating() {
    return isAnimating;
  }

  public int getCounter() {
    return counter;
  }

  public void incrementCounter() {
    counter++;
  }
}

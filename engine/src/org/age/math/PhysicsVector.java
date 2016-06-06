package org.age.math;

/**
 * Created by PetruscaFamily on 4/9/2016.
 */
public class PhysicsVector {
  private double xComp;
  private double yComp;

  public PhysicsVector (double magnitude, float theta) {
    xComp = magnitude * Math.sin(theta);
    yComp = magnitude * Math.cos(theta) * -1;
  }

  public PhysicsVector(double xComp, double yComp) {
    this.xComp = xComp;
    this.yComp = yComp;
  }

  public double magnitude() {
    return Math.sqrt(xComp * xComp + yComp * yComp);
  } 
  
  public double theta() {
    return Math.atan2(yComp, xComp);
  }

  public double basicTheta() {
    return Math.atan(yComp / xComp);
  }

  public double getXComp() {
    return xComp;
  }

  public double getYComp() {
    return yComp;
  }

  public void setXComp(double xComp) {
    this.xComp = xComp;
  }

  public void setYComp(double yComp) {
    this.yComp = yComp;
  }

  public void incrementXComp(double xComp) {
    this.xComp = this.xComp + xComp;
  }

  public void incrementYComp(double yComp) {
    this.yComp = this.yComp + yComp;
  }

  public void increment(PhysicsVector p) {
    xComp += p.getXComp();
    yComp += p.getYComp();
  }
}

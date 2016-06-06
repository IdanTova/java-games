package org.age.math;

import static java.lang.Math.*;

/**
 * Created by PetruscaFamily on 6/26/2015.
 */
public class PolarVector {
  public double a;
  public double m;

  public PolarVector(double magnitude, double angle) {
    this.a = angle;
    this.m = magnitude;
  }

  public PolarVector(PolarVector v) {
    a = v.a;
    m = v.m;
  }

  public double getDegreeAngle() {
    return a *  360/ PI;
  }

  public void add(PolarVector v2) {
    PolarVector v1 = this;
    double x = v1.m * cos(v1.a) + v2.m * cos(v2.a);
    double y = v1.m * sin(v1.a) + v2.m * sin(v2.a);

    a = atan2(y, x);
    m = sqrt(x*x + y*y);
  }

  public PolarVector scale(double scale) {
    m *= scale;
    return this;
  }
}

package org.age.math;

import java.awt.*;

public class Vector {
  public double x1;
  public double y1;
  public double x2;
  public double y2;

  public Vector(double x1, double y1, double x2, double y2) {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
  }

  public Vector(Point p1, Point p2) {
    this(p1.x, p1.y, p2.x, p2.y);
  }

  public Vector(double length, double angle) {
    this(0, 0, length*Math.sin(angle), length*Math.cos(angle));
  }

  public double magnitude() {
    return Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1) * (y2 - y1));
  }
}

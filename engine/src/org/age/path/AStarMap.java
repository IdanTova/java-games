package org.age.path;

import java.awt.*;

public interface AStarMap {
  int getWidth();
  int getHeight();
  boolean isObstacle(Point p);
}

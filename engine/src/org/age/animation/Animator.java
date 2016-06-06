package org.age.animation;

import java.awt.*;

public interface Animator {
  void paint(Graphics2D g);
  void animate();
  void start();
  boolean isAnimating();
}

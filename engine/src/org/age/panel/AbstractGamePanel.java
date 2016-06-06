package org.age.panel;
import org.age.animation.Animator;
import java.awt.*;

public abstract class AbstractGamePanel implements Animator {
  public abstract void processInput(boolean[] keys);

  public void start() {

  }

  public boolean isAnimating() {
    return true;
  }
}

package org.age.animation;

import java.awt.*;

public class ParallelAnimator implements Animator {
  private Animator[] animators;

  public ParallelAnimator(Animator... animators) {
    this.animators = animators;
  }

  @Override
  public void paint(Graphics2D g) {
    for (Animator animator : animators) {
      animator.paint(g);
    }
  }

  @Override
  public void animate() {
    for (Animator animator : animators) {
      animator.animate();
    }
  }

  @Override
  public void start() {
    for (Animator animator : animators) {
      animator.start();
    }
  }

  @Override
  public boolean isAnimating() {
    for (Animator animator : animators) {
      if (animator.isAnimating()) {
        return true;
      }
    }
    return false;
  }
}

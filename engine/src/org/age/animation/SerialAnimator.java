package org.age.animation;

import java.awt.*;

public class SerialAnimator implements Animator {
  private Animator[] animator;
  private AnimationListener listener;
  private int currentAnimator = -1;

  public SerialAnimator(Animator... animators) {
    this(null, animators);
  }

  public SerialAnimator(AnimationListener listener, Animator... animators) {
    this.animator = animators;
    this.listener = listener;
  }

  @Override
  public void paint(Graphics2D g) {
    if (isAnimating()) {
      animator[currentAnimator].paint(g);
    }
  }

  @Override
  public void animate() {
    if (isAnimating()) {
      animator[currentAnimator].animate();
      if (!animator[currentAnimator].isAnimating()) {
        currentAnimator++;
        if (currentAnimator == animator.length) {
          currentAnimator = -1;
          if (listener != null) {
            listener.ended(this);
          }
        } else {
          animator[currentAnimator].start();
        }
      }
    }
  }

  @Override
  public void start() {
    currentAnimator = 0;
    animator[currentAnimator].start();
    if (listener != null) {
      listener.started(this);
    }
  }

  @Override
  public boolean isAnimating() {
    return currentAnimator != -1;
  }
}

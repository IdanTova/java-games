package org.age.animation;

import java.awt.*;

public class DelayAnimator implements Animator {
  public int desiredFrames;
  public int frameCounter;

  public DelayAnimator(int frames) {
    desiredFrames = frames;
    frameCounter = -1;
  }

  @Override
  public void paint(Graphics2D g) {
  }

  @Override
  public void animate() {
    if(isAnimating()) {
      frameCounter++;
      if(frameCounter == desiredFrames) {
        frameCounter = -1;
      }
    }
  }

  @Override
  public void start() {
    frameCounter = 0;
  }

  @Override
  public boolean isAnimating() {
    return frameCounter != -1;
  }
}

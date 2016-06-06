package animators;

import java.awt.*;

/**
 * Created by PetruscaFamily on 2/20/2016.
 */
public abstract class Animator {
  public static enum Fade {FADE_IN, FADE_OUT};
  protected boolean isAnimating = true;
  protected float alpha;
  protected float dAlpha;
  protected Fade fadeState;
  protected int animateCounter;

  public abstract void animate(Graphics2D g);

  public boolean isAnimating() {
    return isAnimating;
  }

  public void setAnimating(boolean isAnimating) {
    this.isAnimating = isAnimating;
  }
}

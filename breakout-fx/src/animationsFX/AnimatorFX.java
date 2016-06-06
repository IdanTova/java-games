package animationsFX;

import javafx.scene.canvas.GraphicsContext;

/**
 * Created by PetruscaFamily on 2/20/2016.
 */
public abstract class AnimatorFX {
  public static enum Fade {FADE_IN, FADE_OUT};
  protected boolean isAnimating = true;
  protected float alpha;
  protected float dAlpha;
  protected Fade fadeState;
  protected int animateCounter;

  public abstract void animate(GraphicsContext g);

  public boolean isAnimating() {
    return isAnimating;
  }

  public void setAnimating(boolean isAnimating) {
    this.isAnimating = isAnimating;
  }

  public float getAlpha() {
    return alpha;
  }
}

package animators;

import java.awt.*;

/**
 * Created by PetruscaFamily on 2/20/2016.
 */
public class InOutFadeAnimator extends Animator {
  private int endCondition;

  public InOutFadeAnimator(float startAlpha, float dAlpha, Fade startFade, int endCondition) {
    animateCounter = 0;
    isAnimating = true;
    alpha = startAlpha;
    fadeState = startFade;
    this.endCondition = endCondition;
    this.dAlpha = dAlpha;
  }

  public void animate(Graphics2D g) {
    if(animateCounter < endCondition) {
      if (alpha >= 1.00f - dAlpha + 0.01) {
        fadeState = Fade.FADE_OUT;
      } else if (alpha <= dAlpha + 0.01) {
        fadeState = Fade.FADE_IN;
      }
      if (fadeState == Fade.FADE_OUT) {
        alpha -= dAlpha;
      } else if (fadeState == Fade.FADE_IN) {
        alpha += dAlpha;
      }
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
      animateCounter++;
    } else {
      isAnimating = false;
    }
  }
}

package animators;

import java.awt.*;

/**
 * Created by PetruscaFamily on 2/21/2016.
 */
public class InFadeAnimator extends Animator {
  public InFadeAnimator(float dAlpha) {
    this.dAlpha = dAlpha;
    alpha = 0.00f;
  }

  public void animate(Graphics2D g) {
    if (alpha < 1.00f - dAlpha + 0.01) {
      alpha += dAlpha;
      if(alpha >= 0.0f && alpha <= 1.0f) {
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
      }
    } else {
      isAnimating = false;
    }
  }
}

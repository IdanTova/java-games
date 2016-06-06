package animators;

import java.awt.*;

/**
 * Created by PetruscaFamily on 2/21/2016.
 */
public class OutFadeAnimator extends Animator {
  public OutFadeAnimator(float dAlpha) {
    this.dAlpha = dAlpha;
    alpha = 1.00f;
  }

  public void animate(Graphics2D g) {
    if (alpha > dAlpha + 0.005) {
      alpha -= dAlpha;
    } else {
      isAnimating = false;
    }
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
  }
}

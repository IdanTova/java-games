package animationsFX;

import javafx.scene.canvas.GraphicsContext;

public class OutFadeAnimatorFX extends AnimatorFX {
  public OutFadeAnimatorFX(float dAlpha) {
    this.dAlpha = dAlpha;
    alpha = 1.00f;
  }

  public OutFadeAnimatorFX(float dAlpha, float alpha) {
    this.dAlpha = dAlpha;
    this.alpha = alpha;
  }


  public void animate(GraphicsContext g) {
    if (alpha > dAlpha + 0.005) {
      alpha -= dAlpha;
    } else {
      isAnimating = false;
    }
    g.setGlobalAlpha(alpha);
  }
}

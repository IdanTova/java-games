package animationsFX;

import javafx.scene.canvas.GraphicsContext;

public class InFadeAnimatorFX extends AnimatorFX {
  public InFadeAnimatorFX(float dAlpha) {
    this.dAlpha = dAlpha;
    alpha = 0.00f;
  }

  public void animate(GraphicsContext g) {
    if (alpha < 1.00f - dAlpha + 0.01) {
      alpha += dAlpha;
      if(alpha >= 0.0f && alpha <= 1.0f) {
        g.setGlobalAlpha(alpha);
      }
    } else {
      isAnimating = false;
    }
  }
}

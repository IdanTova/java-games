package animationsFX;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by PetruscaFamily on 2/20/2016.
 */
public class InOutFadeAnimatorFX extends AnimatorFX {
  private int endCondition;

  public InOutFadeAnimatorFX(float startAlpha, float dAlpha, Fade startFade, int endCondition) {
    animateCounter = 0;
    isAnimating = true;
    alpha = startAlpha;
    fadeState = startFade;
    this.endCondition = endCondition;
    this.dAlpha = dAlpha;
  }

  public void animate(GraphicsContext g) {
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
      g.setGlobalAlpha(alpha);
      animateCounter++;
    } else {
      isAnimating = false;
    }
  }

  public void animate(GraphicsContext g, Color color) {
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
      g.setFill(new Color(color.getRed()/255.0f, color.getGreen()/255.0f, color.getBlue()/255.0f, alpha));
      g.setStroke(new Color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, alpha));
      animateCounter++;
    } else {
      isAnimating = false;
    }
  }
}

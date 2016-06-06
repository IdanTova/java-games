package animationsFX;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RainbowAnimatorFX extends AnimatorFX {
  private float color = 0.0f;
  private int redCounter = 0;
  private float deltaColor;
  private boolean halfDone = false;

  public RainbowAnimatorFX(float deltaColor) {
    this.deltaColor = deltaColor;
    alpha = 1.00f;
  }

  public void animate(GraphicsContext g) {
    if(redCounter > 100) {
      if (color > .98f) {
        halfDone = true;
      }
      if (halfDone && color < 0.2f) {
        halfDone = false;
      }
      if (halfDone) {
        color -= deltaColor;
      } else {
        color += deltaColor;
      }
    } else {
      redCounter++;
    }
    g.setStroke(Color.hsb(color * 360, 1.0f, 1.0f));
    g.setFill(Color.hsb(color * 360, 1.0f, 1.0f));
    g.setGlobalAlpha(1.0f);
  }

  public Color getColor() {
    return Color.hsb(color * 360, 1.0f, 1.0f);
  }
}

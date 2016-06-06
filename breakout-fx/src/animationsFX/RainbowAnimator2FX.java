package animationsFX;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by PetruscaFamily on 2/24/2016.
 */
public class RainbowAnimator2FX extends AnimatorFX {
  private float color = 0.0f;
  private float deltaColor;
  private boolean halfDone = false;

  public RainbowAnimator2FX(float deltaColor) {
    this.deltaColor = deltaColor;
    alpha = 1.00f;
  }

  public void animate(GraphicsContext g) {
    if (color > 0.98f) {
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
    g.setFill(Color.hsb(color*360, 1.0f, 1.0f, alpha));
  }

  public Color getColor() {
    return Color.hsb(color*360, 1.0f, 1.0f, alpha);
  }
}

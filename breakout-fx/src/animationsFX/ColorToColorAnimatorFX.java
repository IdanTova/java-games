package animationsFX;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by PetruscaFamily on 3/13/2016.
 */
public class ColorToColorAnimatorFX extends AnimatorFX {
  Color startColor;
  Color endColor;
  Color color;
  double endTime;
  double timeCounter;
  float alpha = 1.0f;

  public ColorToColorAnimatorFX(Color startColor, Color endColor, double endTime) {
    this.startColor = startColor;
    this.endColor = endColor;
    this.endTime = endTime;
  }
  public ColorToColorAnimatorFX(Color startColor, Color endColor, double endTime, float alpha) {
    this.startColor = startColor;
    this.endColor = endColor;
    this.endTime = endTime;
    this.alpha = alpha;
  }

  @Override
  public void animate(GraphicsContext g) {
    double currentRed = startColor.getRed() + (timeCounter/endTime)*(endColor.getRed() - startColor.getRed());
    double currentGreen = startColor.getGreen() + (timeCounter/endTime)*(endColor.getGreen() - startColor.getGreen());
    double currentBlue = startColor.getBlue() + (timeCounter/endTime)*(endColor.getBlue() - startColor.getBlue());
    if(currentBlue < 0 || currentBlue > 1 || currentGreen < 0 || currentGreen > 1 || currentRed < 0 || currentRed > 1) {
      color = new Color(endColor.getRed(), endColor.getGreen(), endColor.getBlue(), alpha);
    } else {
      color = new Color(currentRed, currentGreen, currentBlue, alpha);
    }
    g.setFill(color);
    g.setStroke(color);
    if(timeCounter >= endTime) {
      isAnimating = false;
    }
    timeCounter++;
  }

  public Color getColor() {
    return color;
  }
}

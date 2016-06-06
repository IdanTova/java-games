package animators;

import java.awt.*;

/**
 * Created by PetruscaFamily on 3/13/2016.
 */
public class ColorToColorAnimator extends Animator {
  Color startColor;
  Color endColor;
  Color color;
  double endTime;
  double timeCounter;

  public ColorToColorAnimator(Color startColor, Color endColor, double endTime) {
    this.startColor = startColor;
    this.endColor = endColor;
    this.endTime = endTime;
  }

  @Override
  public void animate(Graphics2D g) {
    int currentRed = (int)(startColor.getRed() + (timeCounter/endTime)*(endColor.getRed() - startColor.getRed()));
    int currentGreen = (int)(startColor.getGreen() + (timeCounter/endTime)*(endColor.getGreen() - startColor.getGreen()));
    int currentBlue = (int)(startColor.getBlue() + (timeCounter/endTime)*(endColor.getBlue() - startColor.getBlue()));
    if(currentBlue < 0 || currentBlue > 255 || currentGreen < 0 || currentGreen > 255 || currentRed < 0 || currentRed > 255) {
      color = endColor;
    } else {
      color = new Color(currentRed, currentGreen, currentBlue);
    }
    g.setColor(color);
    if(timeCounter >= endTime) {
      isAnimating = false;
    }
    timeCounter++;
  }
}

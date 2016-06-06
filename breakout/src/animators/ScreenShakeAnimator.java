package animators;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by PetruscaFamily on 3/15/2016.
 */
public class ScreenShakeAnimator extends Animator {
  private double xLim, yLim;
  private double dx, dy;
  private double x, y;
  private int counter;
  private int counterLimit;

  public ScreenShakeAnimator(double x, double y, double xLim, double yLim, double dx, double dy, int numLoops) {
    this.dx = dx;
    this.dy = dy;
    this.x = x;
    this.y = y;
    this.xLim = xLim;
    this.yLim = yLim;
    counterLimit = numLoops * 4;
  }

  public void animate(Graphics2D g) {
    if(isAnimating) {
      g.setTransform(AffineTransform.getTranslateInstance(x, y));
      if (x > xLim || x < -xLim) {
        dx = -dx;
        counter++;
      } else if (y > yLim || y < -yLim) {
        dy = -dy;
        counter++;
      }

      x += dx;
      y += dy;
      if (counter == counterLimit) {
        isAnimating = false;
      }
    }
  }
}

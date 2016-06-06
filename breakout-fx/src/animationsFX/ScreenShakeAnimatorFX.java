package animationsFX;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Transform;

public class ScreenShakeAnimatorFX extends AnimatorFX {
  private double xLim, yLim;
  private double dx, dy;
  private double x, y;
  private int counter;
  private int counterLimit;
  private Transform t;

  public ScreenShakeAnimatorFX(double x, double y, double xLim, double yLim, double dx, double dy, int numLoops) {
    this.dx = dx;
    this.dy = dy;
    this.x = x;
    this.y = y;
    this.xLim = xLim;
    this.yLim = yLim;
    counterLimit = numLoops * 4;
  }

  public void animate(GraphicsContext g) {
    if(isAnimating) {
      t = Transform.translate(x, y);
      g.setTransform(t.getMxx(), t.getMyx(), t.getMxy(), t.getMyy(), t.getTx(), t.getTy());
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

  public void applyTransform(GraphicsContext g) {
    g.setTransform(t.getMxx(), t.getMyx(), t.getMxy(), t.getMyy(), t.getTx(), t.getTy());
  }
}

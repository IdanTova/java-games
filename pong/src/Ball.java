import java.awt.*;

/**
 * Created by PetruscaFamily on 5/22/2015.
 */
public class Ball {
  int x;
  int y;
  int velocity;
  double angle;

  int xVelocity;
  int yVelocity;
  Rectangle rect;
  Rectangle nextRect;

  public Ball(int x, int y, int velocity, double angle) {
    this.x = x;
    this.y = y;
    this.velocity = velocity;
    this.angle = angle;

    double radianAngle = angle * Math.PI / 180;
    xVelocity = (int) (Math.cos(radianAngle) * velocity);
    yVelocity = (int) (Math.sin(radianAngle) * velocity);
  }

  public void paint(Graphics2D g) {
    calculateVelocity();
    calculateRect();
    calculateNextRect();
    g.fillRect(rect.x, rect.y, rect.width, rect.height);
  }

  private void calculateVelocity() {
    if(angle >= 360) {
      angle = angle - 360;
    }
    double radianAngle = -angle * Math.PI / 180;
    xVelocity = (int) (Math.cos(radianAngle) * velocity);
    yVelocity = (int) (Math.sin(radianAngle) * velocity);
  }

  private void calculateNextRect() {
    nextRect = new Rectangle(x + xVelocity, y + yVelocity, 10, 10);
  }

  private void calculateRect() {
    rect = new Rectangle(x, y, 10, 10);
  }
}

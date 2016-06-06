import java.awt.*;

/**
 * Created by PetruscaFamily on 5/22/2015.
 */
public abstract class Bat {
  int x;
  int y;
  int velocity;
  int batLength;
  int width;
  Rectangle rect;

  public Bat(int x, int y, int width, int batLength, int velocity) {
    this.x = x;
    this.y = y;
    this.velocity = velocity;
    this.batLength = batLength;
    this.width = width;
  }

  public void paint (Graphics2D g) {
    calculateRect();
    g.fillRect(rect.x, rect.y, rect.width, rect.height);
  }

  private void calculateRect() {
    rect = new Rectangle(x, y, width, batLength);
  }
}

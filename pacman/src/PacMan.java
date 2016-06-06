import java.awt.*;

/**
 * Created by PetruscaFamily on 6/17/2015.
 */
public class PacMan extends PacCharacter {
  public Rectangle rightRect;
  public Rectangle leftRect;
  public Rectangle upRect;
  public Rectangle downRect;
  public Rectangle nextRect;

  public PacMan(int x, int y) {
    super(x, y);
  }

  public void updateRects() {
    int w = PacManRun.BOX_WIDTH;
    int h = PacManRun.BOX_HEIGHT;

    rightRect = new Rectangle(rect.x + w, rect.y, w, h);
    leftRect = new Rectangle(rect.x - w, rect.y, w, h);
    upRect = new Rectangle(rect.x, rect.y - w, w, h);
    downRect = new Rectangle(rect.x, rect.y + w, w, h);
    if(direction == 0) {
      nextRect = new Rectangle(rect.x + speed, rect.y, w, h);
    }if(direction == 1) {
      nextRect = new Rectangle(rect.x, rect.y - speed, w, h);
    }if(direction == 2) {
      nextRect = new Rectangle(rect.x - speed, rect.y, w, h);
    }if(direction == 3) {
      nextRect = new Rectangle(rect.x, rect.y + speed, w, h);
    }
  }
}

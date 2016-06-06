import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by PetruscaFamily on 6/18/2015.
 */
public class Ghost extends PacCharacter {
  Color color;
  Image selected;
  Image right;
  Image left;
  Image up;
  Image down;

  int w = AStarMain.BOX_WIDTH;
  int h = AStarMain.BOX_HEIGHT;
  Rectangle nextRect;

  public Ghost(int x, int y, Color color) {
    super(x, y);
    this.color = color;

    Toolkit kit = Toolkit.getDefaultToolkit();
    if(color.equals(Color.GREEN)) {
      right = kit.getImage("D:\\Java Projects\\Game\\a-star\\src\\green_right.png");
      left = kit.getImage("D:\\Java Projects\\Game\\a-star\\src\\green_left.png");
      up = kit.getImage("D:\\Java Projects\\Game\\a-star\\src\\green_up.png");
      down = kit.getImage("D:\\Java Projects\\Game\\a-star\\src\\green_down.png");
    } else if (color.equals(Color.MAGENTA)) {
      right = kit.getImage("D:\\Java Projects\\Game\\a-star\\src\\red_right.png");
      left = kit.getImage("D:\\Java Projects\\Game\\a-star\\src\\red_left.png");
      up = kit.getImage("D:\\Java Projects\\Game\\a-star\\src\\red_up.png");
      down = kit.getImage("D:\\Java Projects\\Game\\a-star\\src\\red_down.png");
    }
    selected = left;
    direction = 2;
    speed = 2;
  }

  public void paint(Graphics2D g) {
    g.setColor(color);
    if(direction == 0) {
      selected = right;
    }
    if(direction == 1) {
      selected = up;
    }
    if(direction == 2) {
      selected = left;
    }
    if(direction == 3) {
      selected = down;
    }
    g.drawImage(selected ,position.x + 2, position.y + 2, w - 2, h - 2, null);
  }

  public void updateRects() {
    rect = new Rectangle(position.x, position.y, w, h);
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

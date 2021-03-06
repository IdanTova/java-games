import org.age.panel.AbstractGamePanel;
import org.age.path.AStarPathFinder;
import org.age.util.UIUtil;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class AStarScreen extends AbstractGamePanel implements org.age.path.AStarMap {
  AStarMain run;
  final static int NX = 21;
  final static int NY = 21;
  final int h = AStarMain.BOX_HEIGHT;
  final int w = AStarMain.BOX_WIDTH;
  PacBox[][] boxes = new PacBox[NX][NY];
  int timeCounter;
  PacMan man;
  int angle = 0;
  Ghost green;
  Ghost red;
  java.util.List<Point> path;
  java.util.List<Point> path1;
  java.util.List<Point> path2;

  public AStarScreen(AStarMain run) {
    this.run = run;
    for(int y = 0; y < boxes.length; y++) {
      for(int x = 0; x < boxes[y].length; x++) {
        boxes[x][y] = new PacBox(x, y, PacBox.BoxType.EMPTY);
      }
    }
    setScreen();
  }

  private void setScreen() {
    BufferedImage img = UIUtil.loadImage("/map.png");
    Raster data = img.getData();

    for(int y = 0; y < boxes.length; y++) {
      for(int x = 0; x < boxes[y].length; x++) {
        int[] p = data.getPixel(x, y, (int[]) null);
        Color pixelColor = new Color(p[0], p[1], p[2]);
        if (pixelColor.equals(Color.YELLOW)) {
          boxes[x][y].setType(PacBox.BoxType.DOT);
        }else if (pixelColor.equals(Color.BLUE)) {
          boxes[x][y].setType(PacBox.BoxType.POWER);
        }else if (pixelColor.equals(Color.WHITE)) {
          boxes[x][y].setType(PacBox.BoxType.EMPTY);
        }else if (pixelColor.equals(Color.BLACK)) {
          boxes[x][y].setType(PacBox.BoxType.OBSTACLE);
        }else if (pixelColor.equals(Color.RED)) {
          man = new PacMan(x, y);
          man.position = new Point(man.position.x*w, man.position.y*h);
        }else if (pixelColor.equals(Color.GREEN)) {
          green = new Ghost(x, y, Color.GREEN);
          green.position = new Point(green.position.x * w, green.position.y * h);
        }else if (pixelColor.equals(Color.MAGENTA)) {
          red = new Ghost(x, y, Color.MAGENTA);
          red.position = new Point(red.position.x*w, red.position.y*h);
        }
      }
    }

    man.rect = new Rectangle(man.position.x, man.position.y, w, h);
    man.updateRects();
  }

  public void paint(Graphics2D g) {
    g.setColor(Color.black);
    g.fillRect(0, 0, run.getWidth(), run.getHeight());

    for(int x = 0; x <= NX; x++) {
      g.drawLine(x*h, 0, x*h, 900);
      for(int y = 0; y <= NY; y++) {
        g.drawLine(0, y*w, 900, y*w);
      }
    }

    for(int y = 0; y < boxes.length; y++) {
      for(int x = 0; x < boxes[y].length; x++) {
        if (boxes[x][y].type == PacBox.BoxType.DOT) {
          g.setColor(Color.YELLOW);
          g.fillOval(x * w + 8, y * h + 8, w/4, h/4);
        }
        if (boxes[x][y].type == PacBox.BoxType.EMPTY) {
          g.setColor(Color.BLACK);
          g.fillRect(x * w + 1, y * h + 1, w - 1, h - 1);
        }
        else if (boxes[x][y].type == PacBox.BoxType.POWER) {
          g.setColor(Color.YELLOW);
          g.fillOval(x * w + 5, y * h + 5, w / 2, h / 2);
        }
      else if (boxes[x][y].type == PacBox.BoxType.OBSTACLE) {
          g.setColor(new Color(61, 103, 209));
          g.fillRect(x * w + 1, y * h + 1, w - 1, h - 1);
        }
      }
    }

    /*
     Draws the  path on the board
    */

//    if (path != null) {
//      for (Point point : path) {
//        g.setColor(Color.YELLOW);
//        g.fillOval(point.x * w + 8, point.y * h + 8, w / 4, h / 4);
//      }
//    }

    g.setColor(Color.YELLOW);
    if(man.direction == 0) {
      g.fillArc(man.position.x + 1, man.position.y + 1, w - 1, h - 1, 30 - angle, 300 + 2 * angle);
    }
    if(man.direction == 1) {
      g.fillArc(man.position.x + 1, man.position.y + 1, w - 1, h - 1, 120 - angle, 300 + 2 * angle);
    }
    if(man.direction == 2) {
      g.fillArc(man.position.x + 1, man.position.y + 1, w - 1, h - 1, 210 - angle, 300 + 2 * angle);
    }
    if(man.direction == 3) {
      g.fillArc(man.position.x + 1, man.position.y + 1, w - 1, h - 1, 300 - angle, 300 + 2 * angle);
    }

    green.paint(g);
    red.paint(g);
  }

  public void processInput(boolean[] keys) {
    man.rect = new Rectangle(man.position.x, man.position.y, w, h);
    man.updateRects();

    if(keys[KeyEvent.VK_RIGHT]) {
      if(man.direction != 0 && canMoveTo(man.rightRect)) {
        man.speed = 2;
        man.direction2 = man.direction;
        man.direction = 0;
      }
    }
    if(keys[KeyEvent.VK_UP]) {
      if(man.direction != 1 && canMoveTo(man.upRect)) {
        man.speed = 2;
        man.direction2 = man.direction;
        man.direction = 1;
      }
    }
    if(keys[KeyEvent.VK_LEFT]) {
      if(man.direction != 2 && canMoveTo(man.leftRect)) {
        man.speed = 2;
        man.direction2 = man.direction;
        man.direction = 2;
      }
    }
    if(keys[KeyEvent.VK_DOWN]) {
      if(man.direction != 3 && canMoveTo(man.downRect)) {
        man.speed = 2;
        man.direction2 = man.direction;
        man.direction = 3;
      }
    }
  }

  private boolean canMoveTo(Rectangle rect) {
    for(int y = 0; y < boxes.length; y++) {
      for (int x = 0; x < boxes[y].length; x++) {
        if (boxes[x][y].type == PacBox.BoxType.OBSTACLE) {
          Rectangle obstacleRect = new Rectangle(x * w, y * h, w, h);
          if (rect.intersects(obstacleRect)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  public void animate() {
    timeCounter++;
    man.rect = new Rectangle(man.position.x, man.position.y, w, h);

    Point start = new Point(green.position.x/w, green.position.y/h);
    Point target = new Point(man.position.x/w, man.position.y/h);
    AStarPathFinder finder = new AStarPathFinder(this, start, target);
    path = finder.computeShortestPath();

    Point target1 = new Point(green.position.x/w, green.position.y/h);
    Point start1 = new Point(man.position.x/w, man.position.y/h);
    AStarPathFinder finder1 = new AStarPathFinder(this, start1, target1);
    path1 = finder1.computeLongestPath();

    Point start2 = new Point(red.position.x/w, red.position.y/h);
    Point target2 = new Point(man.position.x/w + 4, man.position.y/h);
    AStarPathFinder finder2 = new AStarPathFinder(this, start2, target2);
    path2 = finder2.computeShortestPath();

    angle += 2;
    if (angle == 30) {
      angle = 0;
    }
    if (path != null) {
      green.chooseDirection(path);
    }
    green.updateRects();
    if (timeCounter == 1) {
      if (green.direction == 0) {
        if (!canMoveTo(green.nextRect)) {
          green.speed = 0;
        } else {
          green.position.x += green.speed;
        }
      } else if (green.direction == 1) {
        if (!canMoveTo(green.nextRect)) {
          green.speed = 0;
        } else {
          green.position.y -= green.speed;
        }
      } else if (green.direction == 2) {
        if (!canMoveTo(green.nextRect)) {
          green.speed = 0;
        } else {
          green.position.x -= green.speed;
        }
      } else if (green.direction == 3) {
        if (!canMoveTo(green.nextRect)) {
          green.speed = 0;
        } else {
          green.position.y += green.speed;
        }
      }

      if (path2 != null) {
        red.chooseDirection(path2);
      }
      red.updateRects();
      if (timeCounter == 1) {
        if (red.direction == 0) {
          if (!canMoveTo(red.nextRect)) {
            red.speed = 0;
          } else {
            red.position.x += red.speed;
          }
        } else if (red.direction == 1) {
          if (!canMoveTo(red.nextRect)) {
            red.speed = 0;
          } else {
            red.position.y -= red.speed;
          }
        } else if (red.direction == 2) {
          if (!canMoveTo(red.nextRect)) {
            red.speed = 0;
          } else {
            red.position.x -= red.speed;
          }
        } else if (red.direction == 3) {
          if (!canMoveTo(red.nextRect)) {
            red.speed = 0;
          } else {
            red.position.y += red.speed;
          }
        }
      }

//      if (path1 != null) {
//        man.chooseDirection(path1);
//      }
      man.updateRects();
      if (timeCounter == 1) {
        timeCounter = 0;
        if (man.direction == 0) {
          if (!canMoveTo(man.nextRect)) {
            man.speed = 0;
          } else {
            man.position.x += man.speed;
          }
        } else if (man.direction == 1) {
          if (!canMoveTo(man.nextRect)) {
            man.speed = 0;
          } else {
            man.position.y -= man.speed;
          }
        } else if (man.direction == 2) {
          if (!canMoveTo(man.nextRect)) {
            man.speed = 0;
          } else {
            man.position.x -= man.speed;
          }
        } else if (man.direction == 3) {
          if (!canMoveTo(man.nextRect)) {
            man.speed = 0;
          } else {
            man.position.y += man.speed;
          }
        }

        if (man.position.x > 420) {
          man.position.x = -20;
          man.direction = 0;
        }
        if (man.position.x < -20) {
          man.position.x = 420;
          man.direction = 2;
        }
        checkCollision(man);
        checkCollision(green);
        checkCollision(red);
      }
    }
  }

  private void checkCollision(PacCharacter character) {
    for(int y = 0; y < boxes.length; y++) {
      for (int x = 0; x < boxes[y].length; x++) {
        if(boxes[x][y].type == PacBox.BoxType.DOT) {
          Rectangle dotRect = new Rectangle(x * w + 8, y * h + 8, w/4, h/4);
          if(character.rect.intersects(dotRect) && character instanceof PacMan) {
            boxes[x][y].type = PacBox.BoxType.EMPTY;
          }
        }
        if(boxes[x][y].type == PacBox.BoxType.POWER) {
          Rectangle powerRect = new Rectangle(x * w + 5, y * h + 5, w/2, h/2);
          if(character.rect.intersects(powerRect)) {
            boxes[x][y].type = PacBox.BoxType.EMPTY;
            activatePower();
          }
        }
      }
    }
  }

  private void activatePower() {

  }

  @Override
  public int getWidth() {
    return NX;
  }

  @Override
  public int getHeight() {
    return NY;
  }

  @Override
  public boolean isObstacle(Point p) {
    if(p.x < 0 || p.y < 0 || p.x > 20 || p.y > 20){
      return true;
    }
    return boxes[p.x][p.y].type == PacBox.BoxType.OBSTACLE;
  }
}

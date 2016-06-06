package fiveweek;

import org.age.util.UIUtil;

import java.awt.*;

/**
 * Created by PetruscaFamily on 5/18/2015.
 */
public class MapUnit {
  public static enum Direction {
    UP,
    DOWN,
    RIGHT,
    LEFT
  }

  public Point cameraScreenPos;
  private GeoMap map;
  public Image unit;
  public Direction dir;
  public int x;
  public int y;
  public boolean isDead;

  public MapUnit(Direction dir, int x, int y, Point cameraScreenPos, GeoMap map) {
    this.dir = dir;
    this.x = x;
    this.y = y;
    this.cameraScreenPos = cameraScreenPos;
    this.map = map;
    String direction = dir.name().toLowerCase();
    unit = UIUtil.loadImage("/fiveweek/images/" + direction + ".png");
  }

  public void paint(Graphics2D g) {
    g.drawImage(unit, x - cameraScreenPos.x, y - cameraScreenPos.y, map.getBoxWidth(), map.getBoxHeight(),null);
  }

  public void animate(double dt) {

  }

  public Point getPosition () {
    return new Point(x,y);
  }
}

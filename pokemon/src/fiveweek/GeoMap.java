package fiveweek;

import org.age.panel.AbstractGamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.*;

import static org.age.util.UIUtil.loadImage;
import static org.age.util.UIUtil.loadToolkitImage;

/**
 * Displays the game mapPane
 */
public class GeoMap extends AbstractGamePanel {
  private final static double encounterRate = 0.01;
  private final static int CAMERA_H = 12;
  private final static int CAMERA_W = CAMERA_H;
  public final static int SCROLL_DISTANCE = 150;
  public static final int FIELD_OF_VIEW = 20;
  private final static int SPEED = 3;
  public final int boxWidth;
  public final int boxHeight;
  public final int RANGE;

  public int mapHeight;
  public int mapWidth;
  public MapBox[][] mapBoxes;
  public ArrayList<MapUnit> units = new ArrayList<MapUnit>();
  Image character = loadToolkitImage("/fiveweek/images/Character.gif");
  Image grass = loadImage("/fiveweek/images/Grass.png");
  Image rock = loadImage("/fiveweek/images/Rock.png");
  Image dirt = loadImage("/fiveweek/images/Dirt.png");
  PokemonMain game;

  public Point characterPos;
  public Point cameraScreenPos = new Point(0, 0);
  private int dirX;
  private int dirY;
  int level;

  //instantiations
  public GeoMap(PokemonMain game, int level) {
    this.game = game;
    this.level = level;

    boxWidth = game.getWidth() / CAMERA_W;
    boxHeight = game.getHeight() / CAMERA_H;
    RANGE = 2 * boxHeight;

    loadMap();
  }

  private void loadMap() {
    BufferedImage img = loadImage("/fiveweek/images/map/" + this.level + ".png");
    Raster data = img.getData();

    mapHeight = img.getHeight();
    mapWidth = img.getWidth();

    mapBoxes = new MapBox[mapWidth][mapHeight];

    for (int y = 0; y < mapHeight; y++) {
      for (int x = 0; x < mapWidth; x++) {
        mapBoxes[x][y] = new TerrainBox(x, y, this);
      }
    }

    characterPos = new Point(1, 1);
    for (int y = 0; y < mapHeight; y++) {
      for (int x = 0; x < mapWidth; x++) {
        int[] p = data.getPixel(x, y, (int[]) null);
        Color pixelColor = new Color(p[0], p[1], p[2]);
        if (pixelColor.equals(Color.GREEN)) {
          mapBoxes[x][y] = new GrassBox(x, y, this);
        } else if (pixelColor.equals(Color.BLACK)) {
          mapBoxes[x][y] = new RockBox(x, y, this);
        } else if (pixelColor.equals(Color.WHITE)) {
          mapBoxes[x][y] = new TerrainBox(x, y, this);
        } else if (pixelColor.equals(Color.RED)) {
          characterPos = new Point(x * boxWidth + 1, y * boxWidth + 1);
        } else if (pixelColor.equals(new Color(0, 0, 255))) {
          units.add(new MapUnit(MapUnit.Direction.LEFT, x * boxWidth + 1, y * boxHeight + 1, cameraScreenPos, this));
        } else if (pixelColor.equals(new Color(0, 255, 255))) {
          units.add(new MapUnit(MapUnit.Direction.RIGHT, x * boxWidth + 1, y * boxHeight + 1, cameraScreenPos, this));
        } else if (pixelColor.equals(new Color(0, 0, 191))) {
          units.add(new MapUnit(MapUnit.Direction.DOWN, x * boxWidth + 1, y * boxHeight + 1, cameraScreenPos, this));
        } else if (pixelColor.equals(new Color(0, 127, 255))) {
          units.add(new MapUnit(MapUnit.Direction.UP, x * boxWidth + 1, y * boxHeight + 1, cameraScreenPos, this));
        }
      }
    }
  }

  // graphics
  public void paint(Graphics2D g) {
    // draw board
    for (int y = 0; y < mapHeight; y++) {
      for (int x = 0; x < mapWidth; x++) {
        g.drawImage(getBoxTexture(x, y), x * boxWidth - cameraScreenPos.x, y * boxHeight - cameraScreenPos.y, boxWidth, boxHeight, null);
      }
    }

    // draw enemy units on map
    for (MapUnit unit : units) {
      if (unit != null) {
        unit.paint(g);
      }
    }

    // draw character on board
    g.drawImage(character, characterPos.x - cameraScreenPos.x, characterPos.y - cameraScreenPos.y, boxWidth, boxHeight, null);
  }

  // utility methods
  private Image getBoxTexture(int x, int y) {
    MapBox box = getBox(x, y);
    if (box instanceof RockBox) {
      return rock;
    } else if (box instanceof GrassBox) {
      return grass;
    } else {
      return dirt;
    }
  }

  @Override
  public void animate() {
    // animates character motion
    Point newPos = new Point(characterPos.x + dirX * SPEED, characterPos.y + dirY * SPEED);
    if (!isColliding(newPos, RockBox.class)) {
      characterPos = newPos;
    }
    dirX = 0;
    dirY = 0;


    if (isColliding(characterPos, GrassBox.class)) {
      checkForFight();
    }

    // distance between the character and the camera screen box
    int dL = characterPos.x - cameraScreenPos.x;
    int dR = (cameraScreenPos.x + game.getWidth()) - (characterPos.x + boxWidth);
    int dT = characterPos.y - cameraScreenPos.y;
    int dB = (cameraScreenPos.y + game.getHeight()) - (characterPos.y + boxHeight);

    // if the distance between the camera bound and the player become smaller than
    // a certain value, move the screen over by how much it was pushed.
    if (dL < SCROLL_DISTANCE) {
      cameraScreenPos.x -= SCROLL_DISTANCE - dL;
    }
    if (dR < SCROLL_DISTANCE) {
      cameraScreenPos.x += SCROLL_DISTANCE - dR;
    }
    if (dT < SCROLL_DISTANCE) {
      cameraScreenPos.y -= SCROLL_DISTANCE - dT;
    }
    if (dB < SCROLL_DISTANCE) {
      cameraScreenPos.y += SCROLL_DISTANCE - dB;
    }

    // Camera bound detection so that you don't see white space outside the map
    if (cameraScreenPos.x < 0) {
      cameraScreenPos.x = 0;
    }
    if (cameraScreenPos.x > (mapWidth - CAMERA_W) * boxWidth) {
      cameraScreenPos.x = (mapWidth - CAMERA_W) * boxWidth;
    }
    if (cameraScreenPos.y < 0) {
      cameraScreenPos.y = 0;
    }
    if (cameraScreenPos.y > (mapHeight - CAMERA_H) * boxHeight) {
      cameraScreenPos.y = (mapHeight - CAMERA_H) * boxHeight;
    }


    Point characterCenter = getCenter(characterPos);

    for (MapUnit unit : new ArrayList<MapUnit>(units)) {
      Point unitCenter = getCenter(unit.getPosition());
      int dx = characterCenter.x - unitCenter.x;
      int dy = characterCenter.y - unitCenter.y;

      if ((Math.abs(dx) < RANGE && Math.abs(dy) < RANGE)) {
        switch (unit.dir) {
          case RIGHT:
            if (dx > 0 && Math.abs(dy) < FIELD_OF_VIEW) {
              startFight(unit);
            }
            break;
          case LEFT:
            if (dx < 0 && Math.abs(dy) < FIELD_OF_VIEW) {
              startFight(unit);
            }
            break;
          case UP:
            if (Math.abs(dx) < FIELD_OF_VIEW && dy < 0) {
              startFight(unit);
            }
            break;
          case DOWN:
            if (Math.abs(dx) < FIELD_OF_VIEW && dy > 0) {
              startFight(unit);
            }
            break;
        }
      }

      if(unit.isDead) {
        units.remove(unit);
      }

      Rectangle unitRect = new Rectangle(unit.x, unit.y, boxWidth - 1, boxHeight - 1);
      Rectangle characterRect = new Rectangle(newPos.x + 20, newPos.y + 15, boxWidth - 20 - 1, boxHeight - 15 - 1);
      if (unitRect.intersects(characterRect)) {
        startFight(unit);
      }

      boolean gameIsFinished = true;
      for (MapUnit mapUnit : units) {
        if(mapUnit != null){
          gameIsFinished = false;
        }
      }

      if(gameIsFinished && level != 4) {
        GeoLevelComplete complete = new GeoLevelComplete(game, level);
        game.switchTo(complete);
      }
    }
  }

  private Point getCenter(Point p) {
    return new Point(p.x + boxWidth / 2, p.y + boxHeight / 2);
  }

  private boolean isColliding(Point newPos, Class boxType) {
    Rectangle characterRect = new Rectangle(newPos.x, newPos.y, boxWidth - 1, boxHeight - 1);
    for (MapBox[] mapBox : mapBoxes) {
      for (MapBox box : mapBox) {
        if (box.getClass().equals(boxType) && box.intersects(characterRect)) {
          return true;
        }
      }
    }
    return false;
  }

  private void checkForFight() {
    if (dirX != 0 || dirY != 0) {
      int num = (int) (Math.random() * (1 / encounterRate));
      if (num == 1) {
        startFight();
      }
    }
  }

  private void startFight(MapUnit unit) {
    GeoFight geoFight = new GeoFight(this, unit);
    game.switchTo(geoFight);
  }

  private void startFight() {
    GeoFight geoFight = new GeoFight(this);
    game.switchTo(geoFight);
  }

  private void startAnimation(int xDirection, int yDirection) {
    dirX += xDirection;
    dirY += yDirection;

    if (dirX != 0) {
      dirX /= Math.abs(dirX);
    }
    if (dirY != 0) {
      dirY /= Math.abs(dirY);
    }
  }

  @Override
  public void processInput(boolean[] keys) {
    if (keys[KeyEvent.VK_UP]) {
      startAnimation(0, -1);
    }
    if (keys[KeyEvent.VK_DOWN]) {
      startAnimation(0, +1);
    }
    if (keys[KeyEvent.VK_RIGHT]) {
      startAnimation(1, 0);
    }
    if (keys[KeyEvent.VK_LEFT]) {
      startAnimation(-1, 0);
    }
  }

  // utilities

  private MapBox getBox(int x, int y) {
    return mapBoxes[x][y];
  }

  private MapBox getBox(Point p) {
    return mapBoxes[p.x][p.y];
  }

  public int getBoxHeight() {
    return boxHeight;
  }

  public int getBoxWidth() {
    return boxWidth;
  }
}

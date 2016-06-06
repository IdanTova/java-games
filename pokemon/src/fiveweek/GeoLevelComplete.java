package fiveweek;

import org.age.panel.AbstractGamePanel;
import org.age.util.UIUtil;

import java.awt.*;

/**
 * Created by PetruscaFamily on 5/20/2015.
 */
public class GeoLevelComplete extends AbstractGamePanel {
  PokemonMain game;
  int frameCounter = 0;
  Image image = UIUtil.loadImage("LevelClear.png");
  int level;

  public GeoLevelComplete(PokemonMain game, int level) {
    this.game = game;
    this.level = level;
  }

  @Override
  public void processInput(boolean[] keys) {

  }

  @Override
  public void paint(Graphics2D g) {
    g.drawImage(image, 0, 0, 500, 500, null);
  }

  @Override
  public void animate() {
    frameCounter++;
    if(frameCounter == 30) {
      GeoMap map = new GeoMap(game, level + 1);
      game.switchTo(map);
    }
  }
}

package fiveweek;

import org.age.panel.AbstractGamePanel;
import org.age.ui.loop.GameLoopPanel;
import org.age.util.UIUtil;

import java.awt.*;

/**
 * Created by PetruscaFamily on 5/20/2015.
 */
public class GeoGameOver extends AbstractGamePanel {
  Image image = UIUtil.loadImage("GameOver.png");
  int frameCounter = 0;
  GameLoopPanel game;

  public GeoGameOver(GameLoopPanel game) {
    this.game = game;
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
      game.switchToMenu();
    }
  }
}

package fiveweek;

import java.awt.*;

/**
 * Created by PetruscaFamily on 5/9/2015.
 */
public class PlayerUnit extends BattleUnit {
  public PlayerUnit(int health, int damage, String type, GeoFight fight) {
    super(health, damage, type, fight);
  }

  @Override
  public void paint(Graphics2D g) {
    Color playerColor = Color.BLUE.darker();

    drawHealthBar(75, 147, g);

    if (getType().equals("circle")) {
      g.setColor(playerColor);
      g.fillOval(92, 175, 110, 110);
      g.setColor(Color.BLACK);
      g.drawOval(92, 175, 110, 110);
    }
  }

}

package fiveweek;

import java.awt.*;

import static org.age.util.UIUtil.makeComposite;

/**
 * Created by PetruscaFamily on 5/9/2015.
 */
public class EnemyUnit extends BattleUnit {

  public EnemyUnit(int health, int damage, String type, GeoFight fight) {
    super(health, damage, type, fight);
  }

  @Override
  public void paint(Graphics2D g) {

    Color c = Color.RED.darker();
    drawHealthBar(291, 57, g);

    if (getType().equals("square")) {
      g.setColor(c);
      if (isBlinded())
        g.setColor(new Color(c.getRed() + 70, 140, 140));
      if (isPierced()) {
        g.setComposite(makeComposite(0.6f));
      }
      g.fillRect(325, 85, 75, 75);
      g.setColor(Color.BLACK);
      g.drawRect(325, 85, 75, 75);

      g.setComposite(makeComposite(1f));
    }
  }

}

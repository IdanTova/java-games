package fiveweek;

import org.age.animation.AnimationListener;
import org.age.animation.Animator;
import org.age.animation.DelayAnimator;
import org.age.animation.SerialAnimator;
import org.age.sound.Sound;

import java.awt.*;

/**
 * Created by PetruscaFamily on 4/24/2015.
 */
public abstract class BattleUnit implements Animator {
  private int maxHealth;
  private double health;
  private int damage;
  private String type;

  private double defense;
  private double critChance;
  private double missChance;
  private boolean poisoned;
  private int poisonCounter = 0;
  private boolean isPierced;
  private boolean isBlinded;

  final int NUM_FRAMES = 25;
  GeoFight fight;

  public BattleUnit(int health, int damage, String type, GeoFight fight) {
    this.fight = fight;
    this.health = health;
    this.maxHealth = health;
    this.damage = damage;
    this.type = type;

    defense = 0.25;
    critChance = 0.05;
    missChance = 0.1;
    poisoned = false;
    isPierced = false;
  }

  public void resetUnit() {
    health = maxHealth;

    defense = 0.25;
    critChance = 0.05;
    missChance = 0.1;
    poisoned = false;
    isPierced = false;
    isBlinded = false;
  }

  public void animate() {
  }

  // draws the health bars
  protected void drawHealthBar(int x, int y, Graphics g) {
    int maxHealth = getMaxHealth();

    double healthRatio = health / maxHealth;
    int healthLength = (int) (healthRatio * 140.0);
    if (healthLength <= 0) {
      healthLength = 0;
    }

    Color color = Color.blue;
    if (healthRatio >= .5) {
      color = new Color((int) (0 + 255 * 2 * (1 - healthRatio)), 255, 0);
    } else if (healthRatio > 0) {
      color = new Color(255, (int) (255 - 255 * (1 - 2 * healthRatio)), 0);
    } else if (healthRatio == 0) {
      color = Color.BLACK;
    }

    g.setColor(color);

    if (isPoisoned()) {
      g.setColor(new Color(140, 8, 203));
    }

    g.fillRect(x, y, healthLength, 10);

    g.setFont(new Font(" ", Font.BOLD, g.getFont().getSize()));
    g.drawString((int) Math.round(getHealth()) + "/" + (int) Math.round(getMaxHealth()), x + 55, y + 23);

    g.setColor(Color.BLACK);
    g.drawRect(x, y, 140, 10);

//  paints black interval lines on Health Bar

//    for (int i = 1; i <= 8; i++)
//      g.drawLine(x + (int) (17.5 * i), y, x + (int) (17.5 * i), y + 10);
  }

  public boolean isBlinded() {
    return isBlinded;
  }


  public void setBlinded(boolean isBlinded) {
    this.isBlinded = isBlinded;
  }

  public boolean isPierced() {
    return isPierced;
  }


  public void setPierced(boolean isPierced) {
    this.isPierced = isPierced;
  }

  public double getCritChance() {
    return critChance;
  }


  public void setCritChance(double critChance) {
    this.critChance = critChance;
  }

  public double getMissChance() {
    return missChance;
  }


  public void setMissChance(double missChance) {
    this.missChance = missChance;
  }

  public boolean isPoisoned() {
    return poisoned;
  }


  public void setPoisoned(boolean poisoned) {
    this.poisoned = poisoned;
  }

  public double getDefense() {
    return defense;
  }


  public void setDefense(double defense) {
    this.defense = defense;
  }

  public int getDamage() {
    return damage;
  }


  public void setDamage(int damage) {
    this.damage = damage;
  }

  public int getHealth() {
    return (int)Math.round(health);
  }

  public Animator dealDamage(int dmg) {
    int damage = (int) (dmg * (1 - getDefense()));

    Animator animator = new Damage(damage);
    if (isPoisoned()) {
      AnimationListener listener = new AnimationListener() {
        public void started(Animator animator) {
        }

        public void ended(Animator animator) {
          if(poisonCounter == 2) {
            setPoisoned(false);
          }
        }
      };
      animator = new SerialAnimator(listener, new Damage(3), new DelayAnimator(15), animator);
      poisonCounter ++;
    }
    return animator;
  }

  private class Damage implements Animator {
    int frameCounter = -1;
    double step;

    private Damage(int damage) {
      step = (double) damage / NUM_FRAMES;
    }

    public void paint(Graphics2D g) {
    }

    public void animate() {
      if (isAnimating()) {
        health -= step;
        frameCounter++;
        if (frameCounter == NUM_FRAMES) {
          frameCounter = -1;
        }
      }
    }

    public void start() {
      Sound.playSound("splat");
      frameCounter = 0;
    }

    public boolean isAnimating() {
      return frameCounter >= 0;
    }
  }

  @Override
  public void start() {
  }

  public int getMaxHealth() {
    return maxHealth;
  }

  public void setMaxHealth(int maxHealth) {
    this.maxHealth = maxHealth;
  }


  public String getType() {
    return type;
  }

  public boolean isDead() {
    return getHealth() == 0;
  }

  public boolean isAnimating() {
    return false;
  }
}

package fiveweek;

import org.age.animation.Animator;
import org.age.animation.DelayAnimator;
import org.age.animation.EmptyAnimator;
import org.age.animation.SerialAnimator;
import org.age.panel.AbstractGamePanel;
import org.age.sound.Sound;
import org.age.ui.GameButton;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.age.util.UIUtil.*;

/**
 * Displays a fightPane scenario
 */

public class GeoFight extends AbstractGamePanel {
  BattleUnit enemy;
  Image back = loadImage("/fiveweek/images/FightBackground.png");
  Image back2 = loadImage("/fiveweek/images/foo.jpg");
  PokemonMain game;
  Boolean isCorrectlyAnswered;
  GameButton[][] buttons = new GameButton[2][2];
  MathProblem [][] askedProblems = new MathProblem[4][10];
  Point selectedButton = new Point(0, 0);
  BattleUnit player;
  Animator animator;
  MapUnit unit;
  Sound sounds = new Sound();

  public enum Ability {
    ATTACK("Attack"), // attack:  Single damage
    POISON("Poison"), // poison:  Damage over time
    BLIND("Blind"), // blind:  increases miss chance on enemy, no damage
    PIERCE("Piercing Shot"); // piercing shot:  attack that breaks defense

    Ability(String name) {
      this.name = name;
    }

    public String name;
  }

  public GeoMap map;

  public GeoFight(GeoMap map) {
    setFight(map);
    this.unit = null;
  }

  public GeoFight(GeoMap map, MapUnit unit) {
    setFight(map);
    this.unit = unit;
  }

  public void setFight(GeoMap map){
    this.map = map;
    this.game = map.game;
    enemy = generateEnemyUnit();
    player = new PlayerUnit(20, 5, "circle", this);

    int gap = 28;

    int imageHeight = (int) (game.getWidth() / PokemonMain.ASPECT_RATIO);
    int buttonHeight = ((game.getHeight() - imageHeight) - 3 * gap) / 2;
    int buttonWidth = game.getWidth() / 2 - 2 * gap;

    buttons[0][0] = new GameButton(Ability.ATTACK.name, 20, imageHeight + 20, buttonWidth, buttonHeight, 15);
    buttons[0][1] = new GameButton(Ability.BLIND.name, 20, imageHeight + buttonHeight + 40, buttonWidth, buttonHeight, 15);
    buttons[1][0] = new GameButton(Ability.POISON.name, 276, imageHeight + 20, buttonWidth, buttonHeight, 15);
    buttons[1][1] = new GameButton(Ability.PIERCE.name, 276, imageHeight + buttonHeight + 40, buttonWidth, buttonHeight, 15);

    buttons[0][0].setSelected(true);
  }

  private EnemyUnit generateEnemyUnit() {
    return new EnemyUnit(20, 7, "square", this);
  }

  //draw backgrounds and the whole screen
  public void paint(Graphics2D g) {
    int h = (int) (game.getWidth() / PokemonMain.ASPECT_RATIO);
    g.drawImage(back, 0, 0, game.getWidth(), h, null);
    g.drawImage(back2, 0, h, null);

    player.paint(g);
    enemy.paint(g);

    for (GameButton[] button : buttons) {
      for (GameButton b : button) {
        b.paint(g);
      }
    }
//
//    // paint animators
//    // animation queue
//    if (playerAnimator != null) {
//      playerAnimator.paint(g);
//    }
//    if (enemyAnimator != null) {
//      enemyAnimator.paint(g);
//    }
  }

  // does the enemy turn
  private Animator doEnemyTurn() {
    Animator animator;

    if (!attackIsMiss(enemy)) {
      animator = player.dealDamage(enemy.getDamage());
    } else {
      animator = player.dealDamage(0);
    }

    return animator;
  }

  // does the player turn
  private Animator doPlayerTurn(Ability ability) {

    if (ability == Ability.ATTACK) {
      if (!attackIsMiss(player)) {
        if (attackIsCrit(player)) {
          return enemy.dealDamage(2 * player.getDamage());
        } else {
          return enemy.dealDamage(player.getDamage());
        }
      } else {
        return player.dealDamage(0);
      }
    } else if (ability == Ability.BLIND) {
      enemy.setMissChance(enemy.getMissChance() + 0.4);
      enemy.setBlinded(true);
      return enemy.dealDamage(0);
    } else if (ability == Ability.POISON) {
      enemy.setPoisoned(true);
      return enemy.dealDamage((int) (0.5 * player.getDamage()));
    } else if (ability == Ability.PIERCE) {
      Animator animator = enemy.dealDamage((int) (0.75 * player.getDamage()));
      enemy.setDefense(enemy.getDefense() - .1);
      enemy.setPierced(true);
      return animator;
    }
    throw new IllegalStateException("Should not have ended up here.");
  }

  private boolean attackIsCrit(BattleUnit unit) {
    double random = Math.random();
    return unit.getCritChance() > random;
  }

  private boolean attackIsMiss(BattleUnit unit) {
    double random = Math.random();
    if(unit.equals(enemy)) {
      return unit.getMissChance() > random;
    } else
      return false;
  }

  @Override
  public void animate() {
    if (enemy.isDead()) {
      game.switchTo(map);
      askedProblems = new MathProblem[4][10];
      if(unit != null) {
        unit.isDead = true;
      }
    } else if (player.isDead()) {
      GeoGameOver gameOver = new GeoGameOver(game);
      game.switchTo(gameOver);
      askedProblems = new MathProblem[4][10];
    }

    if (isCorrectlyAnswered != null) {
      Animator playerAnimator;
      if (isCorrectlyAnswered == Boolean.TRUE) {
        Ability ability = getAbility(buttons[selectedButton.x][selectedButton.y].getText());
        playerAnimator = doPlayerTurn(ability);
      } else {
        playerAnimator = new EmptyAnimator();
        if(enemy.isPoisoned()) {
          enemy.dealDamage(3);
        }
      }
      Animator enemyAnimator = doEnemyTurn();
      animator = new SerialAnimator(playerAnimator, new DelayAnimator(30), enemyAnimator);
      animator.start();
      isCorrectlyAnswered = null;
    }

    // animation queue
    if (animator != null) {
      animator.animate();
    }
  }

  private Ability getAbility(String text) {
    for (Ability value : Ability.values()) {
      if (value.name.equals(text)) {
        return value;
      }
    }
    return null;
  }

  @Override
  public void processInput(boolean[] keys) {
    if ((!enemy.isAnimating()) && (!player.isAnimating())) {
      if (keys[KeyEvent.VK_DOWN]) {
        selectedButton.y++;
        if (selectedButton.y == buttons.length) {
          selectedButton.y = 0;
        }
        keys[KeyEvent.VK_DOWN] = false;
        updateSelection();
      }
      if (keys[KeyEvent.VK_UP]) {
        selectedButton.y--;
        if (selectedButton.y < 0) {
          selectedButton.y = buttons.length - 1;
        }
        keys[KeyEvent.VK_UP] = false;
        updateSelection();
      }
      if (keys[KeyEvent.VK_RIGHT]) {
        selectedButton.x--;
        if (selectedButton.x < 0) {
          selectedButton.x = buttons.length - 1;
        }
        keys[KeyEvent.VK_RIGHT] = false;
        updateSelection();
      }
      if (keys[KeyEvent.VK_LEFT]) {
        selectedButton.x--;
        if (selectedButton.x < 0) {
          selectedButton.x = buttons.length - 1;
        }
        keys[KeyEvent.VK_LEFT] = false;
        updateSelection();
      }

      // take actions
      if (keys[KeyEvent.VK_ENTER]) {
        GeoQuestion geoQuestion = new GeoQuestion(this);
        game.switchTo(geoQuestion);
      }
    }
  }

  private void updateSelection() {
    for (GameButton[] button : buttons) {
      for (GameButton b : button) {
        b.setSelected(false);
      }
    }
    buttons[selectedButton.x][selectedButton.y].setSelected(true);
  }
}

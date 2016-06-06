package org.age.ui.menu;

import org.age.panel.AbstractGamePanel;
import org.age.ui.GameButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

import static org.age.util.UIUtil.loadImage;

/**
 * Displays the game menu screen
 */
public class GameMenu extends AbstractGamePanel {
  private static final int WIDTH = 140;
  private static final int HEIGHT = 30;
  private static final int FONT_SIZE = 20;
  private GameButton[] buttons;
  private Image back = loadImage("/org/age/images/BackGround.jpg");
  private int selectedIndex = 0;
  private GameMenuListener listener;
  private String name;
  int width;
  int height;
  int xPosition;
  int fontSize;

  public GameMenu(int width, int height, String name, int fontSize) {
    this.name = name;
    this.width = width;
    this.height = height;
    this.fontSize = fontSize;
    xPosition = (width - WIDTH)/2;

    buttons = new GameButton[4];
    int y = 100;
    buttons[0] = new GameButton("New Game", xPosition, y, WIDTH, HEIGHT, FONT_SIZE);
    y += 90;
    buttons[1] = new GameButton("Options", xPosition, y, WIDTH, HEIGHT, FONT_SIZE);
    y += 90;
    buttons[2] = new GameButton("Help", xPosition, y, WIDTH, HEIGHT, FONT_SIZE);
    y += 90;
    buttons[3] = new GameButton("Exit", xPosition, y, WIDTH, HEIGHT, FONT_SIZE);

    updateSelection();
  }

  public void setListener(GameMenuListener listener) {
    this.listener = listener;
  }

  private void updateSelection() {
    for (GameButton button : buttons) {
      button.setSelected(false);
    }
    buttons[selectedIndex].setSelected(true);
  }

  public void paint(Graphics2D g) {
    g.drawImage(back, 0, 0, null);

    // draw label
    Font font = new Font("Plain", Font.BOLD, fontSize);
    g.setFont(font);
    g.setColor(Color.RED.darker());
    Rectangle textRect = new Rectangle();
    SwingUtilities.layoutCompoundLabel(g.getFontMetrics(font), name, null,
        SwingConstants.CENTER, SwingConstants.CENTER,
        SwingConstants.CENTER, SwingConstants.CENTER,
        new Rectangle(0, 0, width, 54), new Rectangle(), textRect, 0);
    g.drawString(name, textRect.x, textRect.y + textRect.height - 5);

    for (GameButton button : buttons) {
      button.paint(g);
    }
  }

  @Override
  public void processInput(boolean[] keys) {
    if(keys[KeyEvent.VK_DOWN]) {
      selectedIndex ++;
      if (selectedIndex == buttons.length) {
        selectedIndex = 0;
      }
      keys[KeyEvent.VK_DOWN] = false;
      updateSelection();
    }
    if(keys[KeyEvent.VK_UP]) {
      selectedIndex --;
      if (selectedIndex < 0) {
        selectedIndex = buttons.length - 1;
      }
      keys[KeyEvent.VK_UP] = false;
      updateSelection();
    }
    if (keys[KeyEvent.VK_ENTER]) {
      if(listener != null) {
        listener.activated(buttons[selectedIndex]);
      }
    }
  }

  @Override
  public void animate() {

  }
}

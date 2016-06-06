package org.age.ui.loop;

import org.age.panel.AbstractGamePanel;
import org.age.ui.menu.GameMenu;
import org.age.ui.menu.GameMenuListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Switchboard program that practically holds the game
 */
public class GameLoopPanel implements KeyListener {
  private static final int MS_PER_FRAME = 20;
  private static final double NUM_FRAMES = 30.0;

  private int width;
  private int height;

  private enum FadeState {
    NO_FADE,
    IN_FADE,
    OUT_FADE
  }
  private FadeState fadeState = FadeState.NO_FADE;
  private AbstractGamePanel toFadePane;
  private int alpha;
  private GameMenu menu;
  private AbstractGamePanel activatedPane;
  private boolean[] keys = new boolean[65536];
  private JPanel panel = new JPanel() {
    @Override
    public void paint(Graphics g) {
      super.paint(g);
      Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      activatedPane.paint(g2d);

      if (fadeState != FadeState.NO_FADE) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (alpha / NUM_FRAMES)));
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, width, height);
      }
    }
  };

  public GameLoopPanel(int x, int y, int width, int height, String name) {
    this.width = width;
    this.height = height;
    menu = new GameMenu(this.width, this.height, name , 33);
    activatedPane = menu;

    JFrame f = new JFrame(name);
    f.setBounds(x, y, this.width - 3, this.height + 20);
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    f.addWindowListener(new WindowAdapter() {
      public void windowClosed(WindowEvent e) {
        super.windowClosed(e);
        System.exit(0);
      }
    });
    f.setResizable(false);
    f.setContentPane(panel);
    f.addKeyListener(this);
    f.requestFocus();
    f.setVisible(true);
  }

  /**
   * GAME LOOP
   */
  public void start() {
    while (true) {
      long lastTime = System.currentTimeMillis();

      processInput(keys);
      animate();
      panel.repaint();

      try {
        long dt= MS_PER_FRAME - (System.currentTimeMillis() - lastTime);
        if(dt > 0) {
          Thread.sleep(dt);
        }
      } catch (InterruptedException e) {
      }
    }
  }

  private void processInput(boolean[] keys) {
    if(fadeState == FadeState.NO_FADE) {
      activatedPane.processInput(keys);
    }
  }

  private void animate() {
    if(fadeState == FadeState.NO_FADE) {
      activatedPane.animate();
    }

    if(fadeState == FadeState.OUT_FADE) {
      alpha += 1;
      if(alpha == (int)(NUM_FRAMES)) {
        fadeState = FadeState.IN_FADE;
        activatedPane = toFadePane;
      }
    }

    if(fadeState == FadeState.IN_FADE) {
      alpha -= 1;
      if(alpha == 0) {
        fadeState = FadeState.NO_FADE;
      }
    }
  }

  public void switchTo(final AbstractGamePanel pane) {
    fadeState = FadeState.OUT_FADE;
    alpha = 0;
    toFadePane = pane;
  }

  public void switchToMenu() {
    switchTo(menu);
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    keys[e.getKeyCode()] = true;
  }

  @Override
  public void keyReleased(KeyEvent e) {
    keys[e.getKeyCode()] = false;
  }

  public void setMenuListener(GameMenuListener listener) {
    menu.setListener(listener);
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }
}

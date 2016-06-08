import org.age.panel.AbstractGamePanel;
import org.age.ui.menu.GameMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by PetruscaFamily on 5/22/2015.
 */
public class PongRun extends JPanel implements KeyListener {
  public static final int FRAME_WIDTH = 1000;
  public static final int FRAME_HEIGHT = 750;
  private static final int SPEED = 8;
  final int MS_PER_FRAME = 20;
  static int batSize = 0;
  public static boolean[] keys = new boolean[65536];
  GameMenu menu = new GameMenu(FRAME_WIDTH, FRAME_HEIGHT, "Pong", 70);
  AbstractGamePanel activePane;
  AbstractGamePanel fadePane;
  boolean quit = false;
  public enum FadeState {
    FADE_NULL,
    FADE_OUT,
    FADE_IN
  }
  int fadeCounter = -1;
  FadeState fadeState = FadeState.FADE_NULL;

  public PongRun() {
    activePane = menu;
    menu.setListener(button -> {
      if (button.getText().equals("New Game")) {
        PongGame pong = new PongGame(this);
        this.switchTo(pong);
      }
      if(button.getText().equals("Exit")) {
        System.exit(0);
      }
    });

    JFrame f = new JFrame("Pong v1.0");
    f.setBounds(200, 200, FRAME_WIDTH, FRAME_HEIGHT);
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    f.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        quit = true;
      }
    });
    f.setContentPane(this);
    f.setVisible(true);

    addKeyListener(this);
    requestFocus();
    gameLoop();
  }

  /**
   * GAME LOOP
   */
  private void gameLoop() {
    while (!quit) {
      long lastTime = System.currentTimeMillis();

      processInput(keys);
      animate(MS_PER_FRAME);
      repaint();

      try {
        long dt= MS_PER_FRAME - (System.currentTimeMillis() - lastTime);
        if(dt > 0) {
          Thread.sleep(dt);
        }
      } catch (InterruptedException e) {
      }
    }
  }

  public void switchTo(AbstractGamePanel panel) {

    fadeState = FadeState.FADE_OUT;
    fadeCounter = 0;
    fadePane = panel;
  }

  public void processInput(boolean[] keys) {
    activePane.processInput(keys);
  }

  public void animate(double dt) {
    activePane.animate();

    if(fadeState == FadeState.FADE_OUT) {
      if(fadeCounter >= 255) {
        fadeState = FadeState.FADE_IN;
        activePane = fadePane;
      }
      fadeCounter+= SPEED;
    }

    if(fadeState == FadeState.FADE_IN) {
      if(fadeCounter <= 0) {
        fadeCounter = -1;
        fadeState = FadeState.FADE_NULL;
      }
      fadeCounter -= SPEED;
    }
  }

  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    activePane.paint(g2d);

    if(fadeState != FadeState.FADE_NULL) {
      if(fadeCounter < 0) {
        fadeCounter = 0;
      } else if (fadeCounter > 255){
        fadeCounter = 255;
      }

      float alpha = fadeCounter / 255f;
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
      g2d.setColor(Color.BLACK);
      g2d.fillRect(0, 0, 1000, 1000);
    }
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

  public static void main(String[] args) {
    PongRun x = new PongRun();
  }
}

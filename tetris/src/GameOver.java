import org.age.panel.AbstractGamePanel;
import org.age.ui.GameButton;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by PetruscaFamily on 6/11/2015.
 */
public class GameOver extends AbstractGamePanel {
  GameButton[] buttons = new GameButton[3];
  TetrisRun tetrisRun;
  int index = 0;
  boolean darkMode;

  public GameOver(TetrisRun tetrisRun, boolean darkMode) {
    this.tetrisRun = tetrisRun;
    this.darkMode = darkMode;
    buttons[0] = new GameButton("Retry", 50, 200, 250, 125, 30);
    buttons[1] = new GameButton("Quit", 50, 360, 250, 125, 30);
  }

  public void animate() {
    if(darkMode) {
      buttons[2] = new GameButton("Change to Light", 50, 520, 250, 125, 30);
    } else {
      buttons[2] = new GameButton("Change to Dark", 50, 520, 250, 125, 30);
    }

    buttons[index].setSelected(true);
  }

  public void processInput(boolean[] keys) {
    if (keys[KeyEvent.VK_UP]) {
      buttons[index].setSelected(false);

      if (index == 0) {
        index = 2;
      } else {
        index--;
      }
      keys[KeyEvent.VK_UP] = false;
    }
    if (keys[KeyEvent.VK_DOWN]) {
      buttons[index].setSelected(false);

      if (index == 2) {
        index = 0;
      } else {
        index++;
      }
      keys[KeyEvent.VK_DOWN] = false;
    }
    if (keys[KeyEvent.VK_ENTER] && index == 0) {
      tetrisRun.selectedPanel = new TetrisScreen(tetrisRun, darkMode);
    }
    if (keys[KeyEvent.VK_ENTER] && index == 1) {
      System.exit(0);
    }
    if (keys[KeyEvent.VK_ENTER] && index == 2) {
      darkMode = !darkMode;
      keys[KeyEvent.VK_ENTER] = false;
    }
  }

  public void paint(Graphics2D g) {
    Image back = Toolkit.getDefaultToolkit().getImage(getClass().getResource("back.jpg"));
    g.drawImage(back, 0, 0, 351, 871, null);
    g.setColor(Color.GREEN.darker());
    g.setFont(new Font("", Font.BOLD, 75));
    g.drawString("GAME", 55, 80);
    g.drawString("OVER", 62,145);
    for (GameButton button : buttons) {
      if(button != null) {
        button.paint(g);
      }
    }
  }
}

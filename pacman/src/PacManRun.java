import org.age.panel.AbstractGamePanel;
import org.age.ui.loop.GameLoopPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by PetruscaFamily on 6/5/2015.
 */
public class PacManRun extends GameLoopPanel {
  public static final int BOX_WIDTH = 20;
  public static final int BOX_HEIGHT = 20;

  public PacManRun(int x, int y, int width, int height, String name) {
    super(x, y, width, height, name);

    setMenuListener(button -> {
      if (button.getText().equals("New Game")) {
        PacManScreen screen = new PacManScreen(this);
        this.switchTo(screen);
      }
      if (button.getText().equals("Exit")) {
        System.exit(0);
      }
    });
  }

  public static void main(String[] args) {
    PacManRun x = new PacManRun(300, 100, 430, 429, "Pac Man -- Alex Petrusca");
    x.start();
  }
}

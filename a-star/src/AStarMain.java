import org.age.ui.loop.GameLoopPanel;

/**
 * Created by PetruscaFamily on 6/5/2015.
 */
public class AStarMain extends GameLoopPanel {
  public static final int BOX_WIDTH = 20;
  public static final int BOX_HEIGHT = 20;

  public AStarMain(int x, int y, int width, int height, String name) {
    super(x, y, width, height, name);

    setMenuListener(button -> {
      if (button.getText().equals("New Game")) {
        AStarScreen screen = new AStarScreen(this);
        this.switchTo(screen);
      }
      if (button.getText().equals("Exit")) {
        System.exit(0);
      }
    });
  }

  public static void main(String[] args) {
    AStarMain x = new AStarMain(300, 100, 430, 429, "Pac Man");
    x.start();
  }
}

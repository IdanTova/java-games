import org.age.ui.loop.GameLoopPanel;

/**
 * Created by PetruscaFamily on 6/26/2015.
 */
public class AsteroidRun extends GameLoopPanel {
  public AsteroidRun(int x, int y, int width, int height, String name) {
    super(x, y, width, height, name);

    setMenuListener(button -> {
      if (button.getText().equals("New Game")) {
        AsteroidScreen screen = new AsteroidScreen(this);
        this.switchTo(screen);
      }
      if (button.getText().equals("Exit")) {
        System.exit(0);
      }
    });
  }

  public static void main(String[] args) {
    AsteroidRun x = new AsteroidRun(300, 100, 1024, 768, "Asteroids -- Alex Petrusca");
    x.start();
  }
}

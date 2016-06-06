package fiveweek;

import org.age.ui.loop.GameLoopPanel;

public class PokemonMain extends GameLoopPanel {
  public static final double ASPECT_RATIO = 1.5;

  public PokemonMain(int width, int height, String name) {
    super(400, 200, width, height, name);

    setMenuListener(button -> {
      if (button.getText().equals("New Game")) {
        GeoMap geoMap = new GeoMap(PokemonMain.this, 1);
        PokemonMain.this.switchTo(geoMap);
      }
      if (button.getText().equals("Exit")) {
        System.exit(0);
      }
    });
  }

  public static void main(String[] args) {
    new PokemonMain(500, 500, "Geometry Battle Monsters").start();
  }
}

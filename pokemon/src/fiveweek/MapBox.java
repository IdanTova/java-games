package fiveweek;

import java.awt.*;

import static fiveweek.GeoMap.*;

/**
 * Created by PetruscaFamily on 4/21/2015.
 */
public abstract class MapBox extends Rectangle {

  public MapBox(int x, int y, GeoMap map) {
    super(x * map.getBoxWidth(), y * map.getBoxHeight(), map.getBoxWidth(), map.getBoxHeight());
  }

}

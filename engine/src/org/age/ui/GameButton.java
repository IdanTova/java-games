package org.age.ui;

import javax.swing.*;
import java.awt.*;

public class GameButton {
  private String text;

  private int x;
  private int y;
  private int w;
  private int h;
  private int fontSize;
  private boolean selected;

  public GameButton(String text, int x, int y, int w, int h, int fontSize) {
    this.text = text;
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.fontSize = fontSize;
  }

  public void paint(Graphics2D g) {
    Font font = new Font(" ", Font.ITALIC | Font.BOLD, fontSize);
    g.setFont(font);

    int d = selected ? 15 : 0;
    Color color = selected ? Color.darkGray : Color.gray;

    g.setColor(color);
    g.fillRect(x - d, y, w + 2 * d, h);

    g.setColor(Color.white);
    g.drawRect(x - d, y, w + 2 * d, h);

    g.setColor(Color.white);
    Rectangle textRect = new Rectangle();
    SwingUtilities.layoutCompoundLabel(g.getFontMetrics(font), text, null,
        SwingConstants.CENTER, SwingConstants.CENTER,
        SwingConstants.CENTER, SwingConstants.CENTER,
        new Rectangle(x, y, w, h), new Rectangle(), textRect, 0);
    g.drawString(text, textRect.x, textRect.y + textRect.height - 5);
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public String getText() {
    return text;
  }
}

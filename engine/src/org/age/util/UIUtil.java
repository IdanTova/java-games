package org.age.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by PetruscaFamily on 4/24/2015.
 */
public class UIUtil {
  public static JButton createGameButton(String text, int fontSize) {
    JButton btn = new JButton(text);
    btn.setBackground(Color.GRAY);
    btn.setForeground(Color.WHITE);
    btn.setFont(new Font(" ", Font.BOLD | Font.ITALIC, fontSize));
    return btn;
  }

  public static JRadioButton createGameRadioButton(String answer, int fontSize) {
    JRadioButton btn = new JRadioButton(answer);
    btn.setBackground(Color.GRAY);
    btn.setForeground(Color.WHITE);
    btn.setFont(new Font(" ", Font.BOLD, fontSize));
    return btn;
  }

  public static AlphaComposite makeComposite(float alpha) {
    int type = AlphaComposite.SRC_OVER;
    return (AlphaComposite.getInstance(type, alpha));
  }

  public static void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static BufferedImage loadImage(String relativePath) {
    try {
      URL resource = UIUtil.class.getResource(relativePath);
      return ImageIO.read(resource);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static Image loadToolkitImage(String relativePath) {
    URL resource = UIUtil.class.getResource(relativePath);
    return Toolkit.getDefaultToolkit().getImage(resource);
  }
}

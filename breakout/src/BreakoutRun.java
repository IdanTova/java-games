import org.age.panel.AbstractGamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Created by PetruscaFamily on 6/5/2015.
 */
public class BreakoutRun extends JPanel implements KeyListener, MouseMotionListener, MouseListener {
  public final int pixelScale = 25;
  public final int height = pixelScale*35;
  public final int width = (4*pixelScale)*13;
  final int MS_PER_FRAME = 20;
  public static boolean[] keys = new boolean[65536];
  public static Point mouseDisplacement = new Point(0, 0);
  boolean quit = false;
  AbstractGamePanel selectedPanel;
  BreakoutScreen screen = new BreakoutScreen(this);

  public BreakoutRun() {
    JFrame f = new JFrame("Breakout -- Alex Petrusca");
    f.setBounds(125, 50, width + 17, height + 47);
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    f.addWindowListener(new WindowAdapter() {
      public void windowClosed(WindowEvent e) {
        quit = true;
      }
    });
    f.setVisible(true);
    f.setResizable(true);
    f.setContentPane(this);
    selectedPanel = screen;

    BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursors");
    setCursor(blankCursor);

    addKeyListener(this);
    addMouseListener(this);
    requestFocus();
    revalidate();
    startGameLoop();
  }

  private void startGameLoop() {
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
        e.printStackTrace();
      }
    }
  }

  private void animate(int ms_per_frame) {
    selectedPanel.animate();
  }

  private void processInput(boolean[] keys) {
    selectedPanel.processInput(keys);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D)(g);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    selectedPanel.paint(g2);
  }

  // listener methods for keys

  public void keyTyped(KeyEvent e) {  }

  public void keyPressed(KeyEvent e) {
    keys[e.getKeyCode()] = true;
  }

  public void keyReleased(KeyEvent e) {
    keys[e.getKeyCode()] = false;
  }

  // listener methods for mouse events

  public void mouseClicked(MouseEvent e) {  }
  public void mousePressed(MouseEvent e) {
    BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursors");
    setCursor(blankCursor);
  }
  public void mouseReleased(MouseEvent e) {  }
  public void mouseEntered(MouseEvent e) {  }
  public void mouseExited(MouseEvent e) {  }

  //listener methods for mouse motion events

  public void mouseDragged(MouseEvent e) {
    int x = e.getX();
    requestFocus();
    mouseDisplacement = new Point(x - 642, 407);
  }

  public void mouseMoved(MouseEvent e) {
    int x = e.getX();
    requestFocus();
    mouseDisplacement = new Point(x - 642, 407);
  }

  public static void main(String[] args) {
    new BreakoutRun();
  }
}
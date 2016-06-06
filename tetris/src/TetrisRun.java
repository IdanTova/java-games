import org.age.panel.AbstractGamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by PetruscaFamily on 6/5/2015.
 */
public class TetrisRun extends JPanel implements KeyListener {
  public static final int BOX_WIDTH = 35;
  public static final int BOX_HEIGHT = 35;
  final int MS_PER_FRAME = 20;
  public static boolean[] keys = new boolean[65536];
  boolean quit = false;
  AbstractGamePanel selectedPanel;
  TetrisScreen screen = new TetrisScreen(this, false);

  public TetrisRun(int width, int height) {
    JFrame f = new JFrame("Tetris -- Alex Petrusca");
    f.setBounds(300, 100, 367, 865);
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

    addKeyListener(this);
    addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        System.out.println(e.getX() + "    ");
        System.out.println(e.getY() + "    ");
      }
    });
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
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    selectedPanel.paint(g2);
  }

  // listener methods and main method

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
    TetrisRun x = new TetrisRun(10, 20);
  }
}
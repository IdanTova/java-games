import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CookieBot extends JFrame implements KeyListener {
  static boolean keepGoing = true;

  public CookieBot() {
    super("cookie");
  }

  public static void main(String[] args) throws AWTException, InterruptedException {
    Robot robot = new Robot();
    CookieBot cookie = new CookieBot();
    cookie.addKeyListener(cookie);
    cookie.requestFocus();
    robot.mouseMove(1120, 520);
    while(keepGoing) {
      robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
      robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
      cookie.requestFocus();
      Thread.sleep(10);
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
      System.exit(0);
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {

  }
}

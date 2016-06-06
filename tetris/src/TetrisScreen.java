import org.age.panel.AbstractGamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class TetrisScreen extends AbstractGamePanel {
  public static final int NX = 10;
  public static final int NY = 20;
  public int normalSpeed = 35;
  public int fastSpeed = 2;
  Color[][] board = new Color[NX][NY];
  TetrisRun tetrisRun;
  TetrisPiece piece;
  int timeCounter = 0;
  int interval = 35;
  boolean darkMode;
  int score = 0;
  int level = 0;
  int lineTotal = 0;
  int lineCounter = 0;

  public TetrisScreen(TetrisRun tetrisRun, boolean darkMode) {
    this.tetrisRun = tetrisRun;
    this.darkMode = darkMode;
    generatePiece();
  }

  public void animate() {
    if(timeCounter <= interval) {
      timeCounter++;
      if(timeCounter >= interval) {
        piece.moveDown();
        if (intersects(piece)) {
          piece.y --;
          consume(piece);
          checkGameOver();
          for(int x = 0; x < NY; x++) {
            checkIfLine(x);
          }
          addPoints();
          generatePiece();
        }
        timeCounter = 0;
      }
    }

    level = lineTotal/10;
  }

  private void addPoints() {
    score += 5;

    if(lineCounter == 0) {
      score += 0;
    }
    if(lineCounter == 1) {
      score += 40 * (level + 1);
    }
    if(lineCounter == 2) {
      score += 100 * (level + 1);
    }
    if(lineCounter == 3) {
      score += 300 * (level + 1);
    }
    if(lineCounter == 4) {
      score += 1200 * (level + 1);
    }

    lineCounter = 0;
  }

  private void checkGameOver() {
    boolean gameOver = false;

    for(int x = 0; x < NX; x++) {
      if(board[x][0] != null) {
        gameOver = true;
      }
    }

    if(gameOver) {
      tetrisRun.selectedPanel = new GameOver(tetrisRun, darkMode);
    }
  }

  private void checkIfLine(int x) {
    boolean isLine = true;

    for(int y = 0; y < NX; y++) {
      if(board[y][x] == null) {
        isLine = false;
      }
    }

    if(isLine) {
      lineCounter++;
      lineTotal++;
      deleteLines(x);
    }
  }

  private void deleteLines(int x) {
    for(int y = 0; y < NX; y++) {
      board[y][x] = board[y][x - 1];
    }
    if(x == 1) {
      return;
    } else {
      deleteLines(x - 1);
    }
  }

  private boolean intersects(TetrisPiece piece) {
    for (Point point : piece.points) {
      if(piece.y + point.y >= NY || !isInBounds(piece) || board[piece.x + point.x][piece.y + point.y] != null) {
        return true;
      }
    }
    return false;
  }

  private void consume(TetrisPiece piece) {
    for (Point point : piece.points) {
      board[piece.x + point.x][piece.y + point.y] = piece.color;
    }
  }

  private void generatePiece() {
    int num = (int)(Math.random()* 7 + 1);

    if(num == 1) {
      piece = new O_Piece(4, 0);
    }
    if(num == 2) {
      piece = new I_Piece(5, 0);
    }
    if(num == 3) {
      piece = new S_Piece(5, 0);
    }
    if(num == 4) {
      piece = new Z_Piece(5, 0);
    }
    if(num == 5) {
      piece = new L_Piece(5, 0);
    }
    if(num == 6) {
      piece = new J_Piece(5, 0);
    }
    if(num == 7) {
      piece = new T_Piece(5, 0);
    }

    piece.state = (int)(Math.random()*4 + 1);
    piece.updateState();
  }

  public void processInput(boolean[] keys) {
    if(keys[KeyEvent.VK_RIGHT]) {
      piece.x++;
      if (intersects(piece)) {
        piece.x--;
      }
      if(!isInBounds(piece)) {
        piece.x--;
      }
      keys[KeyEvent.VK_RIGHT] = false;
    }

    if(keys[KeyEvent.VK_LEFT]) {
      piece.x--;
      if (intersects(piece)) {
        piece.x++;
      }
      if(!isInBounds(piece)) {
        piece.x++;
      }
      keys[KeyEvent.VK_LEFT] = false;
    }

    if(keys[KeyEvent.VK_UP]) {
      piece.changeState();
      keys[KeyEvent.VK_UP] = false;
    }

    if(keys[KeyEvent.VK_DOWN]) {
      if (interval != fastSpeed) {
        interval = fastSpeed;
        timeCounter = 0;
      }
    } else {
      if (interval != normalSpeed) {
        interval = normalSpeed;
        timeCounter = 0;
      }
    }
  }

  private boolean isInBounds(TetrisPiece piece) {
    ArrayList<Point> points = piece.points;
    for (Point point : points) {
      if (piece.x + point.x > board.length - 1 || piece.x + point.x < 0) {
        return false;
      }
    }
    return true;
  }

  public void paint(Graphics2D g) {
    int boxWidth = TetrisRun.BOX_WIDTH;
    int boxHeight = TetrisRun.BOX_HEIGHT;

    if(darkMode) {
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, 1000, 1000);
    }

    if(darkMode) {
      g.setColor(Color.WHITE.darker());
    } else {
      g.setColor(Color.BLACK);
    }
    for(int x = 0; x <= NX; x++) {
      g.drawLine(x*boxHeight, 0, x*boxHeight, 900);
      for(int y = 0; y <= NY; y++) {
        g.drawLine(0, y*boxWidth, 900, y*boxWidth);
      }
    }

    if(darkMode){
      piece.color = piece.color.brighter();
    }
    piece.paint(g);

    for(int x = 0; x < NX; x++) {
      for(int y = 0; y < NY; y++) {
        if(board[x][y] != null){
          g.setColor(board[x][y]);
          g.fillRect(x * boxWidth + 1, y * boxHeight + 1, boxWidth - 1, boxHeight - 1);
        }
      }
    }

    if(darkMode) {
      g.setColor(new Color(0, 0, 0));
    } else {
      g.setColor(new Color(238, 233, 240));
    }
    g.fillRect(0, 701, 351, 130);

    if(darkMode) {
      g.setColor(Color.WHITE.darker());
    } else {
      g.setColor(Color.BLACK);
    }
    g.setFont(new Font("", Font.BOLD, 25));
    g.drawString("Level:", 15, 733);
    g.drawString("" + (level + 1), 135, 733);
    g.drawLine(0, 744, 227, 744);
    g.drawString("Score:", 15, 775);
    g.drawString("" +(score), 135, 775);
    g.drawLine(0, 786, 227, 786);
    g.drawString("Lines:", 15, 817);
    g.drawString("" + (lineTotal), 135, 817);
    g.drawLine(227, 701, 227, 836);
  }
}

package fiveweek;

import org.age.panel.AbstractGamePanel;
import org.age.ui.GameButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GeoQuestion extends AbstractGamePanel implements ActionListener {
  GeoFight fightPane;
  PokemonMain game;
  MathProblem problem;
  GameButton[][] buttons = new GameButton[2][2];
  Point select = new Point(0, 0);
  MathProblem[][] problems = new MathProblem[4][10];
  int imageHeight;
  int level;

  public GeoQuestion(GeoFight fightPane) {
    this.fightPane = fightPane;
    game = fightPane.game;
    level = fightPane.map.level;
    imageHeight = (int) (game.getWidth() / PokemonMain.ASPECT_RATIO);

    loadAllProblems();
    problem = generateNewProblem();
    int gap = 28;

    int buttonHeight = ((game.getHeight() - imageHeight) - 3 * gap) / 2;
    int buttonWidth = game.getWidth() / 2 - 2 * gap;

    buttons[0][0] = new GameButton(problem.answer1, 20, imageHeight + 20, buttonWidth, buttonHeight, 15);
    buttons[0][1] = new GameButton(problem.answer2, 20, imageHeight + buttonHeight + 40, buttonWidth, buttonHeight, 15);
    buttons[1][0] = new GameButton(problem.answer3, 276, imageHeight + 20, buttonWidth, buttonHeight, 15);
    buttons[1][1] = new GameButton(problem.answer4, 276, imageHeight + buttonHeight + 40, buttonWidth, buttonHeight, 15);

    buttons[0][0].setSelected(true);
  }

  private void loadAllProblems() {
    problems[0][0] = new MathProblem("Quadrant I", "Quadrant II", "Quadrant III", "Quadrant IV", "Quadrant III", 1);
    problems[0][1] = new MathProblem("line", "line segment", "ray", "angle", "ray", 2);
    problems[0][2] = new MathProblem("1", "3", "4", "5", "3", 3);
    problems[0][3] = new MathProblem("yes", "no", "none of these", " not enough info", "yes", 4);
    problems[0][4] = new MathProblem("46√(2)", "23√(2)", "54", "58√(2)", "58√(2)", 5);
    problems[0][5] = new MathProblem("17", "23", "16", "15", "17", 6);
    problems[0][6] = new MathProblem("11", "10", "9", "12", "10", 7);
    problems[0][7] = new MathProblem("X", "V", "W", "none of these", "W", 8);
    problems[0][8] = new MathProblem("104", "96", "76", "70", "76", 9);
    problems[0][9] = new MathProblem("90", "24", "45", "66", "66", 10);

    problems[1][0] = new MathProblem("I", "II", "III", "IV", "II", 11);
    problems[1][1] = new MathProblem("57", "45", "135", "145", "135", 12);
    problems[1][2] = new MathProblem("15", "12", "8", "6", "12", 13);
    problems[1][3] = new MathProblem("trapezoid", "rectangle", "parallelogram", "rhombus", "parallelogram", 14);
    problems[1][4] = new MathProblem("1", "2", "3", "4", "2", 15);
    problems[1][5] = new MathProblem("58", "48", "22", "20", "22", 16);
    problems[1][6] = new MathProblem("23", "45", "135", "43", "43", 17);
    problems[1][7] = new MathProblem("360", "180", "135", "90", "180", 18);
    problems[1][8] = new MathProblem("ASA", "SAA", "SSS", "AAA", "ASA", 19);
    problems[1][9] = new MathProblem("54 & 18", "36 & 36", "46 & 18", "18 & 46", "46 & 18", 20);

    problems[2][0] = new MathProblem("45 degree rotation", "36 degree rotation", "90 degree rotation", "72 degree rotation", "72 degree rotation", 21);
    problems[2][1] = new MathProblem("54", "39", "46", "78", "39", 22);
    problems[2][2] = new MathProblem("100", "90", "145", "105", "100", 23);
    problems[2][3] = new MathProblem("perpendicular bisector", "altitude", "angle bisector", "median", "angle bisector", 24);
    problems[2][4] = new MathProblem("C", "D", "E", "None", "E", 25);
    problems[2][5] = new MathProblem("yes", "no", "maybe", "not enough information", "yes", 26);
    problems[2][6] = new MathProblem("35", "20", "38", "19", "19", 27);
    problems[2][7] = new MathProblem("64", "72", "68", "112", "68", 28);
    problems[2][8] = new MathProblem("43", "59", "40", "52", "43", 29);
    problems[2][9] = new MathProblem("77", "46", "71", "69", "71", 30);

    problems[3][0] = new MathProblem("73/48", "48/73", "55/48", "55/73", "48/73", 31);
    problems[3][1] = new MathProblem("12/5", "5/13", "12/13", "5/12", "5/12", 32);
    problems[3][2] = new MathProblem("8/15", "17/15", "15/8", "17/8", "8/15", 33);
    problems[3][3] = new MathProblem("0.5", "√(3)/2", "-0.5", "-√(3)/2", "-0.5", 34);
    problems[3][4] = new MathProblem("2π/3", "π/4", "π/3", "-π/4", "π/4", 35);
    problems[3][5] = new MathProblem("52", "37", "46", "48", "48", 36);
    problems[3][6] = new MathProblem("π/6", "-π/6", "π/3", "-π/3", "π/3", 37);
    problems[3][7] = new MathProblem("PQ=PR=2√(26) Q=45", "PQ=PR=√(13) Q=45", "PQ=PR=2√(13) Q=45", "PQ=PR=2√(13) Q=35", "PQ=PR=2√(13) Q=45", 38);
    problems[3][8] = new MathProblem("13.971", "14.564", "14.789", "16.654", "14.654", 39);
    problems[3][9] = new MathProblem("5", "6", "7", "8", "7", 40);
  }

  private MathProblem generateNewProblem() {
    int x = level - 1;
    int y = (int) (Math.random() * 10);

    if (fightPane.askedProblems[x][y] == null) {
      fightPane.askedProblems[x][y] = problems[x][y];
      return problems[x][y];
    } else {
      return generateNewProblem();
    }
  }

  public void paint(Graphics2D g) {
    g.drawImage(fightPane.back2, 0, 0, game.getWidth(), game.getHeight(), null);
    Image mathProb = problem.getImage();
    g.drawImage(mathProb, (game.getWidth() - mathProb.getWidth(null))/2, (imageHeight - mathProb.getHeight(null))/2, null);

    for (GameButton[] button : buttons) {
      for (GameButton gameButton : button) {
        gameButton.paint(g);
      }
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    JRadioButton jrb = (JRadioButton) e.getSource();
    fightPane.isCorrectlyAnswered = jrb.getText().equals(problem.correctAnswer);
    game.switchTo(fightPane);
  }

  @Override
  public void animate() {

  }

  @Override
  public void processInput(boolean[] keys) {
    // update selection
    if (keys[KeyEvent.VK_DOWN]) {
      select.y++;
      if (select.y == buttons.length) {
        select.y = 0;
      }
      keys[KeyEvent.VK_DOWN] = false;
      updateSelection();
    }
    if (keys[KeyEvent.VK_UP]) {
      select.y--;
      if (select.y < 0) {
        select.y = buttons.length - 1;
      }
      keys[KeyEvent.VK_UP] = false;
      updateSelection();
    }
    if (keys[KeyEvent.VK_RIGHT]) {
      select.x--;
      if (select.x < 0) {
        select.x = buttons.length - 1;
      }
      keys[KeyEvent.VK_RIGHT] = false;
      updateSelection();
    }
    if (keys[KeyEvent.VK_LEFT]) {
      select.x--;
      if (select.x < 0) {
        select.x = buttons.length - 1;
      }
      keys[KeyEvent.VK_LEFT] = false;
      updateSelection();
    }

    // take action
    if (keys[KeyEvent.VK_ENTER]) {
      fightPane.isCorrectlyAnswered = buttons[select.x][select.y].getText().equals(problem.correctAnswer);
      game.switchTo(fightPane);
    }
  }

  private void updateSelection() {
    for (GameButton[] button : buttons) {
      for (GameButton b : button) {
        b.setSelected(false);
      }
    }

    buttons[select.x][select.y].setSelected(true);
  }
}

package fiveweek;

import java.awt.*;

import static org.age.util.UIUtil.loadImage;

public class MathProblem {
  String answer1;
  String answer2;
  String answer3;
  String answer4;
  String correctAnswer;
  int problemNum;

  public MathProblem(String answer1, String answer2, String answer3, String answer4, String correctAnswer, int num) {
    this.answer1 = answer1;
    this.answer2 = answer2;
    this.answer3 = answer3;
    this.answer4 = answer4;
    this.correctAnswer = correctAnswer;
    problemNum = num;
  }

  public Image getImage() {
    return loadImage("/fiveweek/images/math/prob" + problemNum + ".png");
  }
}

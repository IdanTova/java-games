import animationsFX.PhysicsAnimatorFX;
import animationsFX.RainbowAnimator2FX;
import animationsFX.ScreenShakeAnimatorFX;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import org.age.math.PhysicsVector;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuScreenFX {
  private BreakoutScreenFX screenFX;
  private double scaleX = 1;
  private double scaleY = 1;
  private Bounds bounds1;
  private Bounds bounds2;
  private Bounds bounds3;
  private BreakoutRunFX runFX;
  private ImageCursor cursor = new ImageCursor(BreakoutScreenFX.loadFXImage("assets/cursors/cursor.png"));
  private Image image = BreakoutScreenFX.loadFXImage("assets/background/background1.jpg");
  private RainbowAnimator2FX anim;
  private final ArrayList<FireFlyFX> fires;
  private final ArrayList<FireFlyFX> fires2;
  public final ArrayList<FireFlyFX> fires3;
  private ArrayList<LevelCaption> captions;
  public static double blur = 0.0;
  public static double glow = 0.25;
  private double glow1 = 0.25f;
  private double glow2 = 0.25f;
  private double glow3 = 0.25f;
  private boolean isDone1 = false;
  private boolean isDone2 = false;
  private int counter;
  private boolean shouldSwitch;
  private int switchCounter;
  private int switchLimit = 30;
  public static boolean oneSelected;
  ScreenShakeAnimatorFX shake = new ScreenShakeAnimatorFX(0.0, 0.0, 5.0, 5.0, 3.0, 3.0, 1);
  private boolean willBigScreenShake;
  private boolean isFull = false;
  private boolean isFilling = false;
  private boolean butSelected2;
  public static int lvlsUnlocked;
  public static enum ScreenMove {
    RIGHT,
    LEFT,
    NONE
  }
  private enum Status {
    MAIN_MENU,
    OPTIONS,
    LEVEL_SELECT
  }
  private int numLvls = 12;
  public static int screenNum;
  private Status state = Status.MAIN_MENU;
  public static ScreenMove screen = ScreenMove.NONE;
  private ArrayList<Bounds> bounds;
  private boolean changedStatus;
  public static boolean isDoneTransitioning;
  private int selectedIndex;

  public MenuScreenFX(BreakoutRunFX runFX, BreakoutScreenFX screenFX) {
    this.runFX = runFX;
    this.screenFX = screenFX;
    bounds = new ArrayList<Bounds>();
    Font font = Font.loadFont(getClass().getResource("fonts/AndroidInsomnia.ttf").toExternalForm(), 25);
    anim = new RainbowAnimator2FX(0.001f);
    Text text1 = new Text(BreakoutRunFX.width - 300, 5 * BreakoutRunFX.height / 12 + 27.5, "Start Game");
    Text text2 = new Text(BreakoutRunFX.width - 278, 6 * BreakoutRunFX.height / 12 + 27.5, "Options");
    Text text3 = new Text(BreakoutRunFX.width - 255, 7 * BreakoutRunFX.height / 12 + 27.5, "Exit");
    BreakoutScreenFX.mouseClickedPos = new Point2D.Double(0, 0);
    text1.setFont(font);
    text2.setFont(font);
    text3.setFont(font);
    while (bounds1 == null || bounds2 == null || bounds3 == null) {
      bounds1 = text1.getBoundsInLocal();
      bounds2 = text2.getBoundsInLocal();
      bounds3 = text3.getBoundsInLocal();
    }
    fires = new ArrayList<FireFlyFX>();
    fires2 = new ArrayList<FireFlyFX>();
    fires3 = new ArrayList<FireFlyFX>();
    captions = new ArrayList<LevelCaption>();
    for (int i = 1; i <= numLvls; i++) {
      captions.add(new LevelCaption("assets/levels/level" + i + ".png", i));
    }
    for (int i = 1; i <= numLvls; i++) {
      bounds.add(captions.get(i - 1).getBounds());
    }

    File file = getSaveFile();
    if (file.exists()) {
      try (Scanner scanner = new Scanner(file)) {
        lvlsUnlocked = scanner.nextInt();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      lvlsUnlocked = 1;
      saveLevelState();
    }
  }

  public void saveLevelState() {
    File file = getSaveFile();
    try (PrintWriter writer = new PrintWriter(file)) {
      writer.print(MenuScreenFX.lvlsUnlocked);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private File getSaveFile() {
    return new File(System.getProperty("java.io.tmpdir") + "levels.txt");
  }

  public void animate(double t) {
    if (willBigScreenShake) {
      shake.animate(runFX.getLowerG());
      shake.applyTransform(runFX.getLowerG());
      shake.applyTransform(runFX.getUIG());
      shake.applyTransform(runFX.getGlassG());
      shake.applyTransform(runFX.getUpperG());
    }
    if (!shake.isAnimating()) {
      willBigScreenShake = false;
      shake = new ScreenShakeAnimatorFX(0.0, 0.0, 5.0, 5.0, 3.0, 3.0, 1);
    }

    if (screenNum < 0) {
      screenNum = 0;
    } else if (screenNum > (numLvls - 1) / 9) {
      screenNum = (numLvls - 1) / 9;
    }

    oneSelected = false;
    for (LevelCaption caption : captions) {
      if (caption.shouldBeDrawnOnUpperLayer()) {
        oneSelected = true;
        selectedIndex = caption.getIndex();
      }
    }

    if (shouldSwitch) {
      runFX.getScene().setCursor(Cursor.NONE);
    } else {
      runFX.getScene().setCursor(cursor);
    }
    addFires();

    int random = (int) (Math.random() * (50 - counter / 10));
    double x = BreakoutRunFX.width + 100;
    double y = BreakoutRunFX.height / 2;
    PhysicsVector vel = new PhysicsVector(8 * Math.random() + 2, (float) (Math.random() * 2 * Math.PI));
    PhysicsVector acc = new PhysicsVector(0.4, (float) (3 * Math.PI / 2));
    if (random < 0) {
      random = 0;
    }
    if (random == 0) {
      Color col = Color.GOLD;
      col = new Color(col.getRed(), col.getGreen(), col.getBlue(), 0.75f);
      fires.add(new FireFlyFX(col, x, y, 2 * Math.random() + 1, vel, acc, 0.0f, 10));
    }

    while (fires2.size() < 10) {
      random = (int) (Math.random() * 40);
      int rand = (int) (Math.random() * 2);
      int rand1 = (int) (Math.random() * 2);
      if (rand == 0) {
        x = BreakoutRunFX.width * Math.random();
        if (rand1 == 0) {
          y = -100;
        } else {
          y = BreakoutRunFX.height + 100;
        }
      } else {
        y = BreakoutRunFX.height * Math.random();
        if (rand1 == 0) {
          x = -100;
        } else {
          x = BreakoutRunFX.width + 100;
        }
      }
      vel = new PhysicsVector(2, (float) (Math.random() * 2 * Math.PI));
      acc = new PhysicsVector(0.0, (float) (Math.PI));
      if (random == 10) {
        Color col = Color.TEAL;
        col = new Color(col.getRed(), col.getGreen(), col.getBlue(), 0.03f);
        fires2.add(new FireFlyFX(col, x, y, 2 * Math.random() + 95, vel, acc, 0.0f, 10));
      }
    }
    cleanParticles();
    if (!isDone1) {
      glow += .01;
      if (glow > 1) {
        isDone1 = true;
      }
    } else if (!isDone2 && isDone1) {
      glow -= .01;
      if (glow < 0.25) {
        isDone2 = true;
        glow = 0.25;
      }
    }
    counter++;
    if (shouldSwitch) {
      switchCounter++;
      if (!bool) {
        scaleX += 0.03;
        scaleY += 0.03;
      }
    }
  }

  private void addFires() {
    if (counter < 5) {
      int random = (int) (Math.random() * 2);
      if (random == 0) {
        for (int i = 5; i < 8; i++) {
          double offset = 4;
          PhysicsVector velocity = new PhysicsVector(1 * Math.random() + 1, (float) (Math.random() * Math.PI / 2 + 5 * Math.PI / 4));
          PhysicsVector acceleration = new PhysicsVector(0.4, (float) (3 * Math.PI / 2));
          fires3.add(new FireFlyFX(Color.TEAL, BreakoutRunFX.width, i * BreakoutRunFX.height / 12 + 28 - offset, velocity, acceleration, 0.025, 12.5));
        }
      }
    } else if (counter < 10) {
      int random = (int) (Math.random() * 2);
      if (random == 0) {
        for (int i = 5; i < 8; i++) {
          double offset = 4;
          PhysicsVector velocity = new PhysicsVector(2 * Math.random() + 1, (float) (Math.random() * Math.PI / 2 + 5 * Math.PI / 4));
          PhysicsVector acceleration = new PhysicsVector(0.2, (float) (3 * Math.PI / 2));
          fires3.add(new FireFlyFX(Color.TEAL, BreakoutRunFX.width, i * BreakoutRunFX.height / 12 + 28 - offset, velocity, acceleration, 0.02, 15));
        }
      }
    }
  }

  private void cleanParticles() {
    for (int i = 0; i < fires.size(); i++) {
      if (fires.get(i).getX() < -5 || fires.get(i).getY() < -5 || fires.get(i).getY() > BreakoutRunFX.height + 5) {
        fires.remove(i);
      }
    }
    for (int i = 0; i < fires2.size(); i++) {
      if (fires2.get(i).getX() < -100 || fires2.get(i).getY() < -100 || fires2.get(i).getY() > BreakoutRunFX.height + 100 || fires2.get(i).getY() < -100) {
        fires2.remove(i);
      }
    }
    for (int i = 0; i < fires3.size(); i++) {
      if (fires3.get(i).getX() < -100 || fires3.get(i).getY() < -100 || fires3.get(i).getY() > BreakoutRunFX.height + 100 || fires3.get(i).getY() < -100) {
        fires3.remove(i);
      }
    }
  }

  int transitionEndCounter;
  boolean firstTime = true;
  PhysicsAnimatorFX physLeft = new PhysicsAnimatorFX(new PhysicsVector(5, (float) (3 * Math.PI / 2)), new PhysicsVector(1, (float) (Math.PI / 2)), new PhysicsVector(1, (float) (Math.PI / 2)));
  PhysicsAnimatorFX physRight = new PhysicsAnimatorFX(new PhysicsVector(5, (float) (Math.PI / 2)), new PhysicsVector(1, (float) (3 * Math.PI / 2)), new PhysicsVector(1, (float) (3 * Math.PI / 2)));
  double buttonAlpha = 0;
  double alpha = 1;
  double butGlow2 = 0;

  public void paint(GraphicsContext g) {
    runFX.getLowerG().drawImage(image, -200, -200, BreakoutRunFX.width + 400, BreakoutRunFX.height + 400);
    if (screen == ScreenMove.RIGHT) {
      if ((screenNum + 1) * 9 > numLvls) {
        screen = ScreenMove.NONE;
      }
    } else if (screen == ScreenMove.LEFT) {
      if ((screenNum + 1 - 1) * 9 <= 0) {
        screen = ScreenMove.NONE;
      }
    }
    Canvas canvas1 = null;
    Canvas canvas2 = null;
    if (state == Status.MAIN_MENU) {
      if (shouldSwitch) {
        blur += 5;
        canvas1 = new Canvas(BreakoutRunFX.width, BreakoutRunFX.height);
        canvas1.setEffect(new BoxBlur(blur, blur, (int) blur));
        g = canvas1.getGraphicsContext2D();
      }
      g.drawImage(image, -200, -200, BreakoutRunFX.width + 400, BreakoutRunFX.height + 400);
      g.setFont(Font.loadFont(getClass().getResource("fonts/AndroidInsomnia.ttf").toExternalForm(), 100));
      g.setStroke(anim.getColor());
      g.setEffect(new Glow(1));
      g.strokeText("Neon \nBreakout", 5, BreakoutRunFX.height / 2);
      g.setFont(Font.loadFont(getClass().getResource("fonts/AndroidInsomnia.ttf").toExternalForm(), 25));
      g.setStroke(Color.TEAL);
      g.setEffect(new Glow(glow));
      g.setLineWidth(1 + 2 * glow);

      drawMenuButtons(g);

      g.setGlobalAlpha(1);
      g.setLineWidth(4);
      g.setEffect(new Glow(glow));
      g.strokeLine(0, 5 * BreakoutRunFX.height / 12 - 11, BreakoutRunFX.width, 5 * BreakoutRunFX.height / 12 - 11);
      g.strokeLine(0, 7 * BreakoutRunFX.height / 12 + 51, BreakoutRunFX.width, 7 * BreakoutRunFX.height / 12 + 51);
      g.strokeLine(BreakoutRunFX.width, 5 * BreakoutRunFX.height / 12 - 22, 0, 5 * BreakoutRunFX.height / 12 - 22);
      g.strokeLine(BreakoutRunFX.width, 7 * BreakoutRunFX.height / 12 + 62, 0, 7 * BreakoutRunFX.height / 12 + 62);
      g.setFill(new Color(Color.TEAL.getRed(), Color.TEAL.getGreen(), Color.TEAL.getBlue(), 0.1));
      g.fillRect(0, 5 * BreakoutRunFX.height / 12 - 23, BreakoutRunFX.width, 12);
      g.fillRect(0, 7 * BreakoutRunFX.height / 12 + 51, BreakoutRunFX.width, 12);
      anim.animate(g);
      g.setEffect(new Glow(1));
      for (FireFlyFX fire : fires) {
        fire.animate(g);
      }
      g.setEffect(new Glow(glow));
      for (FireFlyFX fire : fires3) {
        fire.animate(g);
      }
      g.setEffect(null);
      for (FireFlyFX fire : fires2) {
        fire.animate(g);
      }

      if (shouldSwitch) {
        WritableImage image = new WritableImage(BreakoutRunFX.width, BreakoutRunFX.height);
        canvas1.snapshot(new SnapshotParameters(), image);
        Affine affine = new Affine();
        affine.appendTranslation(0, 0);
        affine.appendRotation(0, 0, 0);
        affine.appendScale(scaleX, scaleY, BreakoutRunFX.width, BreakoutRunFX.height);
        runFX.getUpperG().save();
        runFX.getUpperG().setTransform(affine);
        runFX.getUpperG().drawImage(image, 0, 0, BreakoutRunFX.width, BreakoutRunFX.height);
        runFX.getUpperG().restore();
        if (switchCounter > switchLimit) {
          BreakoutScreenFX.transferImg = BreakoutRunFX.upperCanvas.snapshot(new SnapshotParameters(), image);
        }
      }
    } else if (state == Status.LEVEL_SELECT) {
      PhysicsAnimatorFX physics = null;
      if (screen == ScreenMove.RIGHT) {
        physics = physRight;
        physics.animate();
      } else if (screen == ScreenMove.LEFT) {
        physics = physLeft;
        physics.animate();
      }
      if ((!shouldSwitch || switchCounter > 0) && transitionEndCounter < 20) {
        switchCounter = 0;
        shouldSwitch = false;
        transitionEndCounter++;
      }
      canvas1 = new Canvas(BreakoutRunFX.width, BreakoutRunFX.height);
      g = canvas1.getGraphicsContext2D();

      g.drawImage(image, -800, -800, BreakoutRunFX.width + 1600, BreakoutRunFX.height + 1600);
      g.setFont(Font.loadFont(getClass().getResource("fonts/AndroidInsomnia.ttf").toExternalForm(), 100));
      g.setStroke(Color.TEAL);
      Color col = Color.TEAL;
      g.setFill(new Color(col.getRed(), col.getGreen(), col.getBlue(), 0.05));
      g.setEffect(new Glow(1));
      for (LevelCaption caption : captions) {
        if (caption.getIndex() >= 1 + screenNum * 9 && caption.getIndex() <= 9 + screenNum * 9) {
          if (!caption.shouldBeDrawnOnUpperLayer()) {
            caption.paint(g);
          }
        }
      }
      for (LevelCaption caption : captions) {
        if (caption.getIndex() >= 1 + screenNum * 9 && caption.getIndex() <= 9 + screenNum * 9) {
          if (caption.shouldBeDrawnOnUpperLayer() && isDoneTransitioning) {
            caption.paint(runFX.getGlassG());
          }
        }
      }
      g.setEffect(null);
      g.setLineWidth(1.5);
      g.setFill(new Color(col.getRed(), col.getGreen(), col.getBlue(), 0.1));
      g.strokeRect(0, BreakoutRunFX.height * 10 / 11, BreakoutRunFX.width, BreakoutRunFX.height / 11 + 1);
      g.fillRect(0, BreakoutRunFX.height * 10 / 11, BreakoutRunFX.width, BreakoutRunFX.height / 11 + 1);
      g.setFont(Font.loadFont(getClass().getResource("fonts/AndroidInsomnia.ttf").toExternalForm(), 75));
      int screen = screenNum + 1;
      Text text = new Text("World " + screen + "-3");
      g.strokeText(text.getText(), BreakoutRunFX.width / 2 - 175, BreakoutRunFX.height - 12);

      for (int i = 0; i < captions.size(); i++) {
        if (captions.get(i).isFillingScreen()) {
          isFilling = true;
        }
      }
      if (!isFull && !isFilling) {
        if (anyCaptionWantsToShrink()) {
          if (buttonAlpha - 0.05d > 0) {
            buttonAlpha -= 0.05d;
          } else {
            buttonAlpha = 0;
          }
        } else if (oneSelected) {
          if (buttonAlpha + 0.05d < 1) {
            buttonAlpha += 0.05d;
          } else {
            buttonAlpha = 1;
          }
        } else {
          buttonAlpha = 0;
        }
      }

      if (oneSelected) {
        runFX.getGlassG().setGlobalAlpha(buttonAlpha);
        runFX.getGlassG().setLineWidth(2.5);
        runFX.getGlassG().setFill(new Color(Color.TEAL.getRed(), Color.TEAL.getGreen(), Color.TEAL.getBlue(), 0.1));
        runFX.getGlassG().fillRect(BreakoutRunFX.width / 2 - 150, BreakoutRunFX.height * 14 / 16, 300, BreakoutRunFX.height / 12);

        runFX.getGlassG().save();
        runFX.getGlassG().setEffect(new Glow(butGlow2));
        runFX.getGlassG().strokeRect(BreakoutRunFX.width / 2 - 150, BreakoutRunFX.height * 14 / 16, 300, BreakoutRunFX.height / 12);
        runFX.getGlassG().setFont(Font.loadFont(getClass().getResource("fonts/AndroidInsomnia.ttf").toExternalForm(), 60));
        runFX.getGlassG().strokeText("Select", BreakoutRunFX.width / 2 - 90, BreakoutRunFX.height * 15 / 16 + 2);
        runFX.getGlassG().restore();

        runFX.getGlassG().setLineWidth(1);
        runFX.getGlassG().setFill(Color.TEAL);
        if (butSelected2) {
          if (butGlow2 < 1) {
            butGlow2 += 0.05d;
          } else {
            butGlow2 = 1;
          }
        } else {
          if (butGlow2 > 0) {
            butGlow2 -= 0.05d;
          } else {
            butGlow2 = 0;
          }
        }
        if (isFilling) {
          if (buttonAlpha - 0.05d > 0) {
            buttonAlpha -= 0.05d;
          } else {
            buttonAlpha = 0;
          }
        }
      }

      WritableImage image = new WritableImage(BreakoutRunFX.width, BreakoutRunFX.height);
      canvas1.snapshot(new SnapshotParameters(), image);
      Affine affine = new Affine();
      affine.appendTranslation(0, 0);
      affine.appendRotation(0, 0, 0);
      if (bool) {
        affine.appendScale(scaleX, scaleY, BreakoutRunFX.width, BreakoutRunFX.height);
      } else {
        affine.appendScale(scaleX, scaleY, BreakoutRunFX.width / 2, BreakoutRunFX.height / 2);
      }
      runFX.getLowerG().save();
      Effect effect = new BoxBlur(blur, blur, (int) blur);
      runFX.getLowerG().setEffect(effect);
      runFX.getLowerG().setTransform(affine);
      if (physics == null) {
        runFX.getLowerG().drawImage(image, 0, 0, BreakoutRunFX.width, BreakoutRunFX.height);
      } else {
        runFX.getLowerG().drawImage(image, physics.getX(), 0, BreakoutRunFX.width, BreakoutRunFX.height);
      }
      runFX.getLowerG().restore();
      if (scaleX > 1) {
        scaleX -= 0.03;
      } else {
        scaleX = 1;
      }
      if (scaleY > 1) {
        scaleY -= 0.03;
      } else {
        scaleY = 1;
      }
      if (!isDoneTransitioning) {
        if (blur > 0) {
          blur -= 5;
        } else {
          blur = 0;
          isDoneTransitioning = true;
        }
      }
      if (firstTime) {
        for (int i = 0; i < captions.size(); i++) {
          if (captions.get(i).isFullScreen()) {
            isFull = true;
          }
        }
      }
      if (isFull && firstTime) {
        enableBigScreenShake(g);
        firstTime = false;
      }
      if (alpha > 0) {
        BreakoutRunFX.upperCanvas.setEffect(effect);
        runFX.getUpperG().setGlobalAlpha(alpha);
        runFX.getUpperG().drawImage(BreakoutScreenFX.transferImg, 0, 0, BreakoutRunFX.width, BreakoutRunFX.height);
        runFX.getUpperG().setGlobalAlpha(1);
        if (alpha > 0) {
          if (!isFull) {
            alpha -= 0.02;
          } else {
            alpha -= 0.05;
          }
        } else {
          alpha = 0;
        }
      }
    }
    if (screen != ScreenMove.NONE) {
      canvas2 = new Canvas(BreakoutRunFX.width, BreakoutRunFX.height);
      GraphicsContext g2 = canvas2.getGraphicsContext2D();

      g2.drawImage(image, -800, -800, BreakoutRunFX.width + 1600, BreakoutRunFX.height + 1600);
      g2.setFont(Font.loadFont(getClass().getResource("fonts/AndroidInsomnia.ttf").toExternalForm(), 100));
      g2.setStroke(Color.TEAL);
      Color col = Color.TEAL;
      g2.setFill(new Color(col.getRed(), col.getGreen(), col.getBlue(), 0.05));
      g2.setEffect(new Glow(1));
      for (LevelCaption caption : captions) {
        if (screen == ScreenMove.RIGHT) {
          if (caption.getIndex() >= 1 + (screenNum + 1) * 9 && caption.getIndex() <= 9 + (screenNum + 1) * 9) {
            if (!caption.shouldBeDrawnOnUpperLayer()) {
              caption.paint(g2);
            }
          }
        } else if (screen == ScreenMove.LEFT) {
          if (caption.getIndex() >= 1 + (screenNum - 1) * 9 && caption.getIndex() <= 9 + (screenNum - 1) * 9) {
            if (!caption.shouldBeDrawnOnUpperLayer()) {
              caption.paint(g2);
            }
          }
        }
      }
      g2.setEffect(null);
      g2.setLineWidth(1.5);
      g2.setFill(new Color(col.getRed(), col.getGreen(), col.getBlue(), 0.1));
      g2.strokeRect(0, BreakoutRunFX.height * 10 / 11, BreakoutRunFX.width, BreakoutRunFX.height / 11 + 1);
      g2.fillRect(0, BreakoutRunFX.height * 10 / 11, BreakoutRunFX.width, BreakoutRunFX.height / 11 + 1);
      g2.setFont(Font.loadFont(getClass().getResource("fonts/AndroidInsomnia.ttf").toExternalForm(), 75));
      int correctScreen = screenNum + 1;
      Text text = new Text("World " + correctScreen + "-3");
      g2.strokeText(text.getText(), BreakoutRunFX.width / 2 - 176, BreakoutRunFX.height - 12);

      WritableImage image = new WritableImage(BreakoutRunFX.width, BreakoutRunFX.height);
      canvas2.snapshot(new SnapshotParameters(), image);
      Affine affine = new Affine();
      affine.appendTranslation(0, 0);
      affine.appendRotation(0, 0, 0);
      affine.appendScale(scaleX, scaleY, BreakoutRunFX.width / 2, BreakoutRunFX.height / 2);
      runFX.getLowerG().save();
      Effect effect = new BoxBlur(blur, blur, (int) blur);
      runFX.getLowerG().setEffect(effect);
      runFX.getLowerG().setTransform(affine);
      if (screen == ScreenMove.RIGHT) {
        runFX.getLowerG().drawImage(image, BreakoutRunFX.width + physRight.getX(), 0, BreakoutRunFX.width, BreakoutRunFX.height);
        if (BreakoutRunFX.width + physRight.getX() - 200 < 0) {
          screen = ScreenMove.NONE;
          restorePhysics();
          screenNum++;
        }
      } else if (screen == ScreenMove.LEFT) {
        runFX.getLowerG().drawImage(image, -BreakoutRunFX.width + physLeft.getX(), 0, BreakoutRunFX.width, BreakoutRunFX.height);
        if (-BreakoutRunFX.width + physLeft.getX() + 200 > 0) {
          screen = ScreenMove.NONE;
          restorePhysics();
          screenNum--;
        }
      }
      runFX.getLowerG().restore();
    }
    checkButtonPress();
  }

  private void enableBigScreenShake(GraphicsContext g) {
    willBigScreenShake = true;
  }

  private void restorePhysics() {
    physLeft = new PhysicsAnimatorFX(new PhysicsVector(5, (float) (3 * Math.PI / 2)), new PhysicsVector(1, (float) (Math.PI / 2)), new PhysicsVector(1, (float) (Math.PI / 2)));
    physRight = new PhysicsAnimatorFX(new PhysicsVector(5, (float) (Math.PI / 2)), new PhysicsVector(1, (float) (3 * Math.PI / 2)), new PhysicsVector(1, (float) (3 * Math.PI / 2)));
  }

  private boolean anyCaptionWantsToShrink() {
    for (LevelCaption caption : captions) {
      if (caption.shouldShrink()) {
        return true;
      }
    }
    return false;
  }

  private void drawMenuButtons(GraphicsContext g) {
    g.setEffect(new Glow(glow1));
    g.strokeText("Start Game", BreakoutRunFX.width - 300, 5 * BreakoutRunFX.height / 12 + 27.5);
    if (bounds1.contains(BreakoutScreenFX.mousePosition.getX(), BreakoutScreenFX.mousePosition.getY())) {
      glow1 += .125;
      if (glow1 > 1) {
        glow1 = 1;
      }
    } else {
      glow1 -= .125;
      if (glow1 < .25) {
        glow1 = .25;
      }
    }
    g.setEffect(new Glow(glow2));
    g.strokeText("Options", BreakoutRunFX.width - 278, 6 * BreakoutRunFX.height / 12 + 27.5);
    if (bounds2.contains(BreakoutScreenFX.mousePosition.getX(), BreakoutScreenFX.mousePosition.getY())) {
      glow2 += .125;
      if (glow2 > 1) {
        glow2 = 1;
      }
    } else {
      glow2 -= .125;
      if (glow2 < .25) {
        glow2 = .25;
      }
    }
    g.setEffect(new Glow(glow3));
    g.strokeText("Exit", BreakoutRunFX.width - 255, 7 * BreakoutRunFX.height / 12 + 27.5);
    if (bounds3.contains(BreakoutScreenFX.mousePosition.getX(), BreakoutScreenFX.mousePosition.getY())) {
      glow3 += .1;
      if (glow3 > 1) {
        glow3 = 1;
      }
    } else {
      glow3 -= .1;
      if (glow3 < .25) {
        glow3 = .25;
      }
    }
  }

  public static Bounds b = new BoundingBox(0, 0, 1, 1);
  boolean bool = false;

  private void checkButtonPress() {
    if (state == Status.MAIN_MENU) {
      if (bounds1.contains(BreakoutScreenFX.mouseClickedPos.getX(), BreakoutScreenFX.mouseClickedPos.getY())) {
        shouldSwitch = true;
        if (switchCounter > switchLimit) {
          state = Status.LEVEL_SELECT;
          changedStatus = true;
          BreakoutScreenFX.mouseClickedPos = new Point2D.Double(0, 0);
        }
      }
      if (bounds2.contains(BreakoutScreenFX.mouseClickedPos.getX(), BreakoutScreenFX.mouseClickedPos.getY())) {
        shouldSwitch = true;
        if (switchCounter > switchLimit) {
          state = Status.OPTIONS;
          changedStatus = true;
          BreakoutScreenFX.mouseClickedPos = new Point2D.Double(0, 0);
        }
      }
      if (bounds3.contains(BreakoutScreenFX.mouseClickedPos.getX(), BreakoutScreenFX.mouseClickedPos.getY())) {
        shouldSwitch = true;
        if (switchCounter > switchLimit) {
          runFX.getStage().close();
          BreakoutScreenFX.mouseClickedPos = new Point2D.Double(0, 0);
        }
      }
    } else if (state == Status.LEVEL_SELECT) {
      for (int i = 1 + 9 * screenNum; i <= 9 + 9 * screenNum; i++) {
        if (i < numLvls) {
          if (bounds.get(i - 1).contains(BreakoutScreenFX.mousePosition.getX(), BreakoutScreenFX.mousePosition.getY()) && !oneSelected) {
            captions.get(i - 1).makeGlow();
          } else {
            captions.get(i - 1).stopGlow();
          }

          if (bounds.get(i - 1).contains(BreakoutScreenFX.mouseClickedPos.getX(), BreakoutScreenFX.mouseClickedPos.getY())) {
            if (!oneSelected) {
              captions.get(i - 1).makeExpand();
            }
          } else {
            if (captions.get(i - 1).shouldBeDrawnOnUpperLayer()) {
              captions.get(i - 1).makeShrink();
            }
            captions.get(i - 1).stopExpand();
          }
        }
      }
      if (oneSelected) {
        Rectangle rect = new Rectangle(BreakoutRunFX.width / 2 - 150, BreakoutRunFX.height * 14 / 16, 300, BreakoutRunFX.height / 12);
        b = rect.getBoundsInLocal();
        if (b.contains(BreakoutScreenFX.mouseClickedPos.getX(), BreakoutScreenFX.mouseClickedPos.getY())) {
          shouldSwitch = true;
          if (!bool) {
            for (LevelCaption caption : captions) {
              if (caption.shouldBeDrawnOnUpperLayer()) {
                caption.makeGoFull();
              }
            }
            bool = true;
          }
          for (LevelCaption caption : captions) {
            caption.stopShrink();
          }
          if (switchCounter > switchLimit) {
            screenFX.setLevel(selectedIndex);
            runFX.state = BreakoutRunFX.BreakoutState.GAME;
          }
        }
      }
      if (b.contains(BreakoutScreenFX.mousePosition.getX(), BreakoutScreenFX.mousePosition.getY()) && oneSelected) {
        butSelected2 = true;
      } else {
        butSelected2 = false;
      }
    }
  }
}


//fixme LevelCaption
class LevelCaption {
  private double xIncr = 0;
  private double yIncr = 0;
  private boolean isFullScreen;
  private boolean isGoingFull;
  private PhysicsAnimatorFX zoomOutVec = new PhysicsAnimatorFX(new PhysicsVector(10, (float) (3 * Math.PI / 2)), new PhysicsVector(-1, (float) (Math.PI / 2)), new PhysicsVector(0.5, (float) (Math.PI / 2)));
  final double finalX = 100;
  final double finalY = 50;
  final double finalW = BreakoutRunFX.width - 200;
  final double finalH = BreakoutRunFX.height - 200;
  final double startX;
  final double startY;
  final double startW;
  final double startH;
  private int lifespan = 20;
  private double dx, dy, dWidth, dHeight;
  public static double w = 100 * 13 / 18.0;
  public static double h = 25 * 35 / 18.0;
  double width, height;
  double gapX, gapY;
  BufferedImage image;
  Image img;
  double x, y;
  int index;
  BreakBoxFX[][] boxes = null;
  Image bGround = BreakoutScreenFX.loadFXImage("assets/background/background1.jpg");
  Image lockImg1 = BreakoutScreenFX.loadFXImage("assets/lock.png");
  Image lockImg2 = BreakoutScreenFX.loadFXImage("assets/lock1.png");
  private boolean shouldGlow;
  private boolean shouldExpand;
  private boolean shouldShrink;

  public LevelCaption(String URL, int index) {
    image = BreakoutScreenFX.loadBufferedImage(URL);
    this.index = index;
    int row = ((index - 1) / 3) % 3;
    int col = ((index - 1) - row * 3) % 3;
    gapX = 5.0 / 8 * w;
    gapY = 0.5 * h;
    width = 4.75 * w;
    height = 4.75 * h;
    x = gapX * (col + 1) + width * col + 45;
    y = gapY * (row + 1) + height * row;
    startW = 4.75 * w;
    startH = 4.75 * h;
    startX = gapX * (col + 1) + width * col + 45;
    startY = gapY * (row + 1) + height * row;
    if (image != null) {
      boxes = new BreakBoxFX[image.getHeight()][image.getWidth()];
      for (int y = 0; y < image.getHeight(); y++) {
        for (int x = 0; x < image.getWidth(); x++) {
          int[] rgb = image.getData().getPixel(x, y, (int[]) null);
          Color newColor = new Color(rgb[0] / 255.0, rgb[1] / 255.0, rgb[2] / 255.0, 1.0f);
          if (rgb[0] == 0 || rgb[1] == 0 || rgb[2] == 0) {
            boxes[y][x] = BreakBoxFX.getStationaryInstance(null, x * 4 * 25, y * 25 + 77);
            boxes[y][x].setUnbreakable();
          } else if (rgb[0] < 250 || rgb[1] < 250 || rgb[2] < 250) {
            boxes[y][x] = BreakBoxFX.getStationaryInstance(newColor, x * 4 * 25, y * 25 + 77);
          }
        }
      }
    }
    Canvas canvas = new Canvas(BreakoutRunFX.width, BreakoutRunFX.height);
    GraphicsContext g2 = canvas.getGraphicsContext2D();
    g2.clearRect(0, 0, BreakoutRunFX.width, BreakoutRunFX.height);
    g2.drawImage(bGround, 0, 0, BreakoutRunFX.width, BreakoutRunFX.height);
    for (BreakBoxFX[] box : boxes) {
      for (BreakBoxFX breakBoxFX : box) {
        if (breakBoxFX != null) {
          breakBoxFX.draw(g2);
        }
      }
    }
    WritableImage img = new WritableImage(BreakoutRunFX.width, BreakoutRunFX.height);
    canvas.snapshot(new SnapshotParameters(), img);
    this.img = img;
    dx = (finalX - startX) / lifespan;
    dy = (finalY - startY) / lifespan;
    dWidth = (finalW - startW) / lifespan;
    dHeight = (finalH - startH) / lifespan;
  }

  double lineWidth = 1.5;
  double glow = 0;
  public void paint(GraphicsContext g) {
    double yExtra = 50 * (2 * yIncr) / (BreakoutRunFX.height - finalH);
    double curX = x - xIncr;
    double curY = y - yIncr + yExtra;
    double curW = width + 2 * xIncr;
    double curH = height + 2 * yIncr;
    g.setEffect(null);
    {
      if (index != MenuScreenFX.lvlsUnlocked) {
        if (index > MenuScreenFX.lvlsUnlocked) {
          g.setGlobalAlpha(0.5d);
        } else {
          g.setGlobalAlpha(1d);
          if (glow != 0) {
            g.setEffect(new Glow(glow));
          }
        }
      } else {
        if (glow != 0) {
          g.setEffect(new Glow(glow));
          g.setGlobalAlpha(1.0d);
        }
      }
      g.drawImage(img, curX, curY, curW, curH);
    }

    Color col = Color.TEAL;
    g.setFill(new Color(col.getRed(), col.getGreen(), col.getBlue(), 0.1));
    g.setStroke(Color.TEAL);
    g.setLineWidth(lineWidth);
    double[] xs = {curX + curW - 50, curX + curW, curX + curW};
    double[] ys = {curY + curH - 10, curY + curH - 60, curY + curH - 10.5};
    g.fillRect(curX, curY + curH - 11, curW, 10);
    g.strokeRect(curX, curY, curW, curH);
    g.fillPolygon(xs, ys, 3);
    g.strokeLine(curX, curY + curH - 10, curX + curW - 50, curY + curH - 10);
    g.strokeLine(xs[0], ys[0], xs[1], ys[1]);
    g.setFont(Font.loadFont(getClass().getResource("fonts/AndroidInsomnia.ttf").toExternalForm(), 25));
    g.setFill(Color.TEAL);
    if (index < 10) {
      g.fillText(index + "", curX + curW - 20, curY + curH - 10);
    } else {
      g.fillText(index + "", curX + curW - 31, curY + curH - 10);
    }
    if (MenuScreenFX.isDoneTransitioning) {
      animate();
    }
    if (index > MenuScreenFX.lvlsUnlocked) {
      double x = curX + width / 2 - 90;
      double y = curY + height / 2 - 90;
      g.setGlobalAlpha(0.2d);
      g.drawImage(lockImg2, x, y, 180, 180);
    }
    if (isFullScreen) {
      Canvas c = new Canvas(BreakoutRunFX.width, BreakoutRunFX.height);
      GraphicsContext g2 = c.getGraphicsContext2D();
      {
        g2.setEffect(null);
        g2.drawImage(img, curX, curY, curW, curH);
        Color cole = Color.TEAL;
        g2.setFill(new Color(cole.getRed(), cole.getGreen(), cole.getBlue(), 0.1));
        g2.setStroke(Color.TEAL);
        g2.setLineWidth(lineWidth);
        double[] xss = {curX + curW - 50, curX + curW, curX + curW};
        double[] yss = {curY + curH - 10, curY + curH - 60, curY + curH - 10.5};
        g2.fillRect(curX, curY + curH - 11, curW, 10);
        g2.strokeRect(curX, curY, curW, curH);
        g2.fillPolygon(xss, yss, 3);
        g2.strokeLine(curX, curY + curH - 10, curX + curW - 50, curY + curH - 10);
        g2.strokeLine(xss[0], yss[0], xss[1], yss[1]);
        g2.setFont(Font.loadFont(getClass().getResource("fonts/AndroidInsomnia.ttf").toExternalForm(), 25));
        g2.setFill(Color.TEAL);
        if (index < 10) {
          g2.fillText(index + "", curX + curW - 20, curY + curH - 10);
        } else {
          g2.fillText(index + "", curX + curW - 31, curY + curH - 10);
        }
      }
      WritableImage imge = new WritableImage(BreakoutRunFX.width, BreakoutRunFX.height);
      BreakoutScreenFX.transferImg = g2.getCanvas().snapshot(new SnapshotParameters(), imge);
    }
    g.setGlobalAlpha(1.0d);
  }

  private void animate() {
    if (isGoingFull && !isFullScreen) {
      if (x - xIncr - 20 <= 0 && y - yIncr - 20 <= 0 || width + xIncr * 2 + 20 >= BreakoutRunFX.width && height + yIncr * 2 + 20 >= BreakoutRunFX.height) {
        isFullScreen = true;
        xIncr = 100 * BreakoutRunFX.width / BreakoutRunFX.height;
        yIncr = 100;
      } else {
        zoomOutVec.animate();
        xIncr = zoomOutVec.getX() * BreakoutRunFX.width / BreakoutRunFX.height;
        yIncr = zoomOutVec.getX();
      }
    }
    if (shouldGlow) {
      if (lineWidth < 3.5) {
        lineWidth += 0.2;
      } else {
        lineWidth = 3.5;
      }
      if (glow < 0.5) {
        glow += 0.05;
      } else {
        glow = 0.5;
      }
    } else {
      if (lineWidth > 1.5) {
        lineWidth -= 0.1;
      } else {
        lineWidth = 1.5;
      }
      if (glow > 0) {
        glow -= 0.075;
      } else {
        glow = 0;
      }
    }

    if (shouldExpand) {
      if (MenuScreenFX.blur + 2.5d < 50) {
        MenuScreenFX.blur += 2.5d;
      } else {
        MenuScreenFX.blur = 50;
      }
    } else if (shouldShrink) {
      if (MenuScreenFX.blur - 2.5d > 0) {
        MenuScreenFX.blur -= 2.5d;
      } else {
        MenuScreenFX.blur = 0;
      }
    }

    if (shouldExpand) {
      if (!(x == finalX && y == finalY || width == finalW && height == finalH)) {
        if (finalX > startX) {
          if (x - dx < finalX) {
            x += dx;
          } else {
            x = finalX;
          }
        } else if (finalX < startX) {
          if (x + dx > finalX) {
            x += dx;
          } else {
            x = finalX;
          }
        }
        if (finalY > startY) {
          if (y - dy < finalY) {
            y += dy;
          } else {
            y = finalY;
          }
        } else if (finalY < startY) {
          if (y + dy > finalY) {
            y += dy;
          } else {
            y = finalY;
          }
        }
        if (width + dWidth < finalW) {
          width += dWidth;
        } else {
          width = finalW;
        }
        if (height + dHeight < finalH) {
          height += dHeight;
        } else {
          height = finalH;
        }
      }
    } else if (shouldShrink) {
      if (!(x == startX && y == startY || width == startW && height == startH)) {
        if (finalX > startX) {
          if (x + dx > startX) {
            x -= dx;
          } else {
            x = startX;
            stopShrink();
          }
        } else if (finalX < startX) {
          if (x - dx < startX) {
            x -= dx;
          } else {
            x = startX;
            stopShrink();
          }
        }
        if (finalY > startY) {
          if (y + dy > startY) {
            y -= dy;
          } else {
            y = startY;
            stopShrink();
          }
        } else if (finalY < startY) {
          if (y - dy < startY) {
            y -= dy;
          } else {
            y = startY;
            stopShrink();
          }
        }
        if (width - dWidth > startW) {
          width -= dWidth;
        } else {
          width = startW;
          stopShrink();
        }
        if (height - dHeight > startH) {
          height -= dHeight;
        } else {
          height = startH;
          stopShrink();
        }
      }
    }
  }

  public int getIndex() {
    return index;
  }

  public Bounds getBounds() {
    Rectangle rect = new Rectangle(x, y, width, height);
    return rect.getBoundsInLocal();
  }

  public void makeGlow() {
    shouldGlow = true;
  }

  public void stopGlow() {
    shouldGlow = false;
  }

  public void makeExpand() {
    if (index <= MenuScreenFX.lvlsUnlocked) {
      shouldExpand = true;
    }
  }

  public void makeShrink() {
    shouldShrink = true;
    if (!MenuScreenFX.b.contains(BreakoutScreenFX.mouseClickedPos.getX(), BreakoutScreenFX.mouseClickedPos.getY())) {
      BreakoutScreenFX.mouseClickedPos.setLocation(0, 0);
    }
  }

  public boolean shouldExpand() {
    return shouldExpand;
  }

  public boolean shouldGlow() {
    return shouldGlow;
  }

  public boolean shouldShrink() {
    return shouldShrink;
  }

  public boolean shouldBeDrawnOnUpperLayer() {
    return x != startX || shouldExpand;
  }

  public void stopExpand() {
    shouldExpand = false;
  }

  public void stopShrink() {
    shouldShrink = false;
  }

  public boolean isFullScreen() {
    return isFullScreen;
  }

  public void makeGoFull() {
    if (index <= MenuScreenFX.lvlsUnlocked) {
      isGoingFull = true;
    }
  }

  public boolean isFillingScreen() {
    return isGoingFull;
  }
}

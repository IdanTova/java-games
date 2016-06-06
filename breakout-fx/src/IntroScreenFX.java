import animationsFX.*;
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import org.age.animation.Animator;
import org.age.math.PhysicsVector;

import java.util.ArrayList;

public class IntroScreenFX {
  private BreakoutRunFX runFX;
  private BreakoutScreenFX gameFX;
  private int counter = 0;
  private double blurRadius = 100;
  private double glow = 0;
  private ColorToColorAnimatorFX anim;
  private OutFadeAnimatorFX anim2;
  private OutFadeAnimatorFX anim3;
  private InFadeAnimatorFX anim4;
  private Image image = BreakoutScreenFX.loadFXImage("assets/background/background1.jpg");
  ArrayList<DotLightFX> dots;
  ArrayList<FireFlyFX> fires;
  private LineParticleFX line1;
  private LineParticleFX line2;
  private LineParticleFX line3;
  private LineParticleFX line4;
  private LineParticleFX line5;
  private LineParticleFX line6;
  private LineParticleFX line7;

  public IntroScreenFX(BreakoutRunFX runFX, BreakoutScreenFX gameFX) {
    dots = new ArrayList<DotLightFX>();
    fires = new ArrayList<FireFlyFX>();
    this.runFX = runFX;
    anim = new ColorToColorAnimatorFX(Color.BEIGE, Color.RED, 50, 0.5f);
    anim2 = new OutFadeAnimatorFX(0.01f, 0.5f);
    anim3 = new OutFadeAnimatorFX(0.03f);
    anim4 = new InFadeAnimatorFX(0.125f);
    double offset = 8.5;
    line1 = new LineParticleFX(0, 5*runFX.height/12 - 11, runFX.width, 5*runFX.height/12 - 11, 4, Color.TEAL, 4);
    line2 = new LineParticleFX(0, 7*runFX.height/12 + 51, runFX.width, 7*runFX.height/12 + 51, 4, Color.TEAL, 4);
    line3 = new LineParticleFX(runFX.width, 5*runFX.height/12 - 22, 0, 5*runFX.height/12 - 22, 4, Color.TEAL, 4);
    line4 = new LineParticleFX(runFX.width, 7*runFX.height/12 + 62, 0, 7*runFX.height/12 + 62, 4, Color.TEAL, 4);
    line5 = new LineParticleFX(runFX.width, 5*runFX.height/12 + 28 - offset, runFX.width - 300, 5*runFX.height/12 + 28 - offset, 35, Color.TEAL, 10);
    line6 = new LineParticleFX(runFX.width, 6*runFX.height/12 + 28 - offset, runFX.width - 300, 6*runFX.height/12 + 28 - offset, 35, Color.TEAL, 10);
    line7 = new LineParticleFX(runFX.width, 7*runFX.height/12 + 28 - offset, runFX.width - 300, 7*runFX.height/12 + 28 - offset, 35, Color.TEAL, 10);
    this.gameFX = gameFX;
  }

  public void animate(double t) {
    runFX.getScene().setCursor(Cursor.NONE);
    if(blurRadius > 0) {
      blurRadius--;
    } else if(glow < 1){
      glow += 0.01f;
    } else {
      glow = 1;
    }
    counter++;
    if(counter < 140 && counter > 80) {
      double x = runFX.width * Math.random();
      double y = runFX.height * Math.random();
      double x1 = runFX.width * Math.random();
      double y1 = runFX.height * Math.random();
      double x2 = runFX.width * Math.random();
      double y2 = runFX.height * Math.random();
      dots.add(new DotLightFX(x, y, 0.025f, getRandomColor()));
      dots.add(new DotLightFX(x1, y1, 0.025f, getRandomColor()));
      dots.add(new DotLightFX(x2, y2, 0.025f, getRandomColor()));
    }

    for(int i = 0; i < dots.size(); i++) {
      if(!dots.get(i).isAnimating()) {
        dots.remove(i);
      }
    }
    for(int i = 0; i < fires.size(); i++) {
      if(!fires.get(i).isAnimating()) {
        fires.remove(i);
      }
    }
    if(counter > 260) {
      gameFX.transferFiresToMenu(fires);
      runFX.state = BreakoutRunFX.BreakoutState.MENU;
    }
  }

  public void paint(GraphicsContext g) {
    g.setGlobalAlpha(1.0f);
    g.drawImage(image, -200, -200, runFX.width + 400, runFX.height + 400);
    g.setGlobalAlpha(1);
    if(blurRadius > 0) {
      g.setEffect(new GaussianBlur(blurRadius));
    } else {
      g.setEffect(new Glow(glow));
    }
    g.setFont(Font.loadFont(getClass().getResource("fonts/AndroidInsomnia.ttf").toExternalForm(), 100));
    g.setStroke(Color.BEIGE);
    g.setFill(new Color(Color.BEIGE.getRed(), Color.BEIGE.getGreen(), Color.BEIGE.getBlue(), 0.5));
    if(counter > 110) {
      anim.animate(g);
      g.setStroke(new Color(anim.getColor().getRed(), anim.getColor().getGreen(), anim.getColor().getBlue(), 1.0f));
      if(counter > 170) {
        anim2.animate(g);
        g.setGlobalAlpha(1.0f);
        g.setFill(new Color(anim.getColor().getRed(), anim.getColor().getGreen(), anim.getColor().getBlue(), anim2.getAlpha()));
      }
    }
    g.setGlobalAlpha(1);
    g.fillText("Neon \nBreakout", 5, runFX.height / 2);
    g.strokeText("Neon \nBreakout", 5, runFX.height / 2);
    for (DotLightFX dot : dots) {
      dot.animate(g);
    }
    if(counter > 220) {
      g.setEffect(new Glow(0.25));
      g.setGlobalAlpha(1);
      g.setLineWidth(4.5);
      line1.animate(g);
      line2.animate(g);
      line3.animate(g);
      line4.animate(g);
    }

    if(counter > 230) {
      anim3.animate(g);
      line5.setColor(new Color(line5.getColor().getRed(), line5.getColor().getGreen(), line5.getColor().getBlue(), anim3.getAlpha()));
      line6.setColor(new Color(line6.getColor().getRed(), line6.getColor().getGreen(), line6.getColor().getBlue(), anim3.getAlpha()));
      line7.setColor(new Color(line7.getColor().getRed(),line7.getColor().getGreen(), line7.getColor().getBlue(), anim3.getAlpha()));
      g.setEffect(new GaussianBlur(18));
      if(anim3.isAnimating()) {
        line5.animate(g);
        line6.animate(g); // FIXME
        line7.animate(g);
      }

      for(int i = 5; i < 8; i++) {
        double offset = 4;
        PhysicsVector velocity = new PhysicsVector(1, (float) (Math.random() * 2 * Math.PI));
        PhysicsVector acceleration = new PhysicsVector(0.6, (float) (3 * Math.PI / 2));
        fires.add(new FireFlyFX(Color.TEAL, runFX.width, i * runFX.height / 12 + 28 - offset, velocity, acceleration, 0.03, 10));
      }

      if(counter > 232) {
        if (anim4.isAnimating()) {
          anim4.animate(g);
        }
        g.setEffect(new Glow(0.25));
        g.setGlobalAlpha(1);
        g.setFont(Font.loadFont(getClass().getResource("fonts/AndroidInsomnia.ttf").toExternalForm(), 25));
        g.setStroke(Color.TEAL);
        g.setLineWidth(1.5);
        g.strokeText("Start Game", runFX.width - 300, 5 * runFX.height / 12 + 27.5);
        g.strokeText("Options", runFX.width - 278, 6 * runFX.height / 12 + 27.5);
        g.strokeText("Exit", runFX.width - 255, 7 * runFX.height / 12 + 27.5);
      }
    }
    for (FireFlyFX fire : fires) {
      fire.animate(g);
    }
  }

  private Color getRandomColor() {
    return new Color(Math.random(), Math.random(), Math.random(), 1);
  }
}



class LineParticleFX extends AnimatorFX {
  private Color color;
  private double startX;
  private double startY;
  private double endX;
  private double endY;
  private double lineWidth;
  private double lifeSpan;
  private double counter;

  LineParticleFX(double startX, double startY, double endX, double endY, double lineWidth, Color color, double lifeSpan) {
    this.startX = startX;
    this.startY = startY;
    this.endX = endX;
    this.endY = endY;
    this.lineWidth = lineWidth;
    this.color = color;
    this.lifeSpan = lifeSpan;
  }

  public void animate(GraphicsContext g) {
    g.setGlobalAlpha(color.getOpacity());
    g.setStroke(color);
    double lineWid = lineWidth*(lifeSpan - 4*counter)/lifeSpan;
    if(lineWid < 0) {
     lineWid = 0;
    }
    g.setLineWidth(lineWid);
    g.setLineCap(StrokeLineCap.ROUND);
    g.strokeLine(startX, startY, startX + (endX - startX)*(counter/lifeSpan), startY + (endY - startY)*(counter/lifeSpan));
    if(counter == lifeSpan) {
      isAnimating = false;
    } else {
      counter++;
    }
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public Color getColor() {
    return color;
  }
}


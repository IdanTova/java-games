import animationsFX.AnimatorFX;
import animationsFX.InFadeAnimatorFX;
import animationsFX.OutFadeAnimatorFX;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;

/**
 * Created by PetruscaFamily on 4/2/2016.
 */
public class DotLightFX extends AnimatorFX {
  private double x;
  private double y;
  private Color color;
  private InFadeAnimatorFX anim1;
  private OutFadeAnimatorFX anim2;
  private int lifeCounter;
  private int lifeCounterMax;

  public DotLightFX(double x, double y, Color color) {
    this.x = x;
    this.y = y;
    this.color = color;
    anim1 = new InFadeAnimatorFX(0.01f);
    anim2 = new OutFadeAnimatorFX(0.01f);
    lifeCounterMax = (int)(Math.random() * 8) + 5;
  }

  public DotLightFX(double x, double y, float da, Color color) {
    this.x = x;
    this.y = y;
    this.color = color;
    anim1 = new InFadeAnimatorFX(da);
    anim2 = new OutFadeAnimatorFX(da);
  }

  public void animate(GraphicsContext g) {
    if(anim1.isAnimating()) {
      anim1.animate(g);
    } else if(lifeCounter < lifeCounterMax) {
      lifeCounter++;
      g.setGlobalAlpha(1);
    } else if(anim2.isAnimating()){
      anim2.animate(g);
    }
    if(isAnimating()) {
      g.setFill(color);
      g.fillArc(x, y, 5, 5, 0, 360, ArcType.CHORD);
    }
  }

  public Color getColor() {
    return color;
  }

  public boolean isAnimating() {
    return anim2.isAnimating();
  }
}

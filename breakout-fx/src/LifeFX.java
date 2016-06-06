import animationsFX.AnimatorFX;
import animationsFX.OutFadeAnimatorFX;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class LifeFX extends AnimatorFX {
  private int yOffset;
  private int i = 0;
  private OutFadeAnimatorFX anim;
  private boolean isKilled = false;

  public LifeFX(int i) {
    this.i = i;
    anim = new OutFadeAnimatorFX(0.01f, 0.5f);
  }

  public void animate(GraphicsContext g) {
    if(isKilled && anim.isAnimating()) {
      anim.animate(g);
    }
    g.setGlobalAlpha(1.0f);
    g.setFill(new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), anim.getAlpha())); //FIXME
    g.fillArc(3*1300/4 - 239 - 50*i + 14*(0.5-anim.getAlpha())/0.5, 15.5 + 14*(0.5-anim.getAlpha())/0.5 - yOffset, 28*anim.getAlpha()/0.5, 28*anim.getAlpha()/0.5, 0, 360, ArcType.CHORD);
    g.setStroke(new Color(Color.AQUAMARINE.getRed(), Color.AQUAMARINE.getGreen(), Color.AQUAMARINE.getBlue(), 0.8));
    g.setLineWidth(5);
    g.strokeArc(3*1300/4 - 239 - 50*i, 15.5 - yOffset, 27, 27, 0, 360, ArcType.CHORD);
  }

  public boolean isAnimating() {
    return true;
  }

  public void kill() {
    isKilled = true;
  }

  public void setYOffset(int y) {
    yOffset = y;
  }
}

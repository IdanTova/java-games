package org.age.util;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

/**
 * Created by PetruscaFamily on 3/13/2016.
 */
public class String2D {
  private double width;
  private double height;
  private String str;
  private GlyphVector vector;
  private Shape shape;
  private Font font;
  FontRenderContext context;

  public String2D(String str, Font font) {
    this.str = str;
    this.font = font;
    context = new FontRenderContext(null, true, true);
    vector = font.createGlyphVector(context, str);
  }

  public void paint(Graphics2D g, double x, double y, double scaleX, double scaleY, double theta, double anchorX, double anchorY) {
    width = scaleX * (vector.getGlyphPixelBounds(str.length() - 1, context, 0, 0).getMaxX() - vector.getGlyphPixelBounds(0, context, 0, 0).getMinX());
    height = scaleY * (vector.getGlyphPixelBounds(str.length() - 1, context, 0, 0).getMaxY() - vector.getGlyphPixelBounds(0, context, 0, 0).getMinY());
    GeneralPath path = new GeneralPath();
    for (int i = 0; i < str.length(); i++) {
      Shape outline = vector.getGlyphOutline(i, 0, 0);
      Shape scaled = AffineTransform.getScaleInstance(scaleX, scaleY).createTransformedShape(outline);
      Shape translated = AffineTransform.getTranslateInstance(x - width/2, y + height/2).createTransformedShape(scaled);
      Shape rotated = AffineTransform.getRotateInstance(theta, anchorX, anchorY).createTransformedShape(translated);
      path.append(rotated, true);
    }
    shape = path;
    g.fill(path);
  }

  public void paint(Graphics2D g, AffineTransform scaleTrans, AffineTransform rotateTrans, AffineTransform translateTrans) {
    width = scaleTrans.getScaleX() * (vector.getGlyphPixelBounds(str.length() - 1, context, 0, 0).getMaxX() - vector.getGlyphPixelBounds(0, context, 0, 0).getMinX());
    height = scaleTrans.getScaleY() * (vector.getGlyphPixelBounds(str.length() - 1, context, 0, 0).getMaxY() - vector.getGlyphPixelBounds(0, context, 0, 0).getMinY());
    GeneralPath path = new GeneralPath();
    for (int i = 0; i < str.length(); i++) {
      Shape outline = vector.getGlyphOutline(i, 0, 0);
      Shape scaled = scaleTrans.createTransformedShape(outline);
      Shape translated = translateTrans.createTransformedShape(scaled);
      Shape rotated = rotateTrans.createTransformedShape(translated);
      path.append(rotated, true);
    }
    shape = path;
    g.fill(path);
  }

  public double getWidth() {
    return width;
  }

  public double getHeight() {
    return height;
  }

  public String getStr() {
    return str;
  }

  public void setFont(Font font) {
    this.font = font;
  }

  public Shape getShape() {
    return shape;
  }

  public Font getFont() {
    return font;
  }
}

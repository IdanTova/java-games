import org.age.panel.AbstractGamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

public class AsteroidScreen extends AbstractGamePanel {
  public static final double RAD = Math.PI / 360;
  public static final double DEG = 1/ RAD;
  public static final int H = 768;
  public static final int W = 1024;
  ArrayList<Asteroid> asteroids = new ArrayList<>();
  ArrayList<Asteroid> excessAsteroids = new ArrayList<>();
  ArrayList<Missile> missiles = new ArrayList<>();
  ArrayList<Particle> particles = new ArrayList<>();
  ArrayList<Saucer> saucers = new ArrayList<>();
  AsteroidRun run;
  SpaceShip ship;
  private int pieceCounter;
  private int saucerCounter;
  private int missileCounter;
  int life = 3;
  int maxAmount = 5;

  public AsteroidScreen(AsteroidRun run) {
    this.run = run;
    ship = new SpaceShip(new Point2D.Double(200.0, 200.0));
  }

  public void processInput(boolean[] keys) {
    missileCounter++;
    if(keys[KeyEvent.VK_RIGHT] && !ship.isExploding()) {
      ship.setAngle(ship.getAngle() + 14 * RAD);
    }
    if(keys[KeyEvent.VK_LEFT] && !ship.isExploding()) {
      ship.setAngle(ship.getAngle() - 14 * RAD);
    }
    if(keys[KeyEvent.VK_UP]) {
      ship.accelerate();
      ship.startAccelerating();
    }
    if(!keys[KeyEvent.VK_UP]) {
      ship.decelerate();
      ship.stopAccelerating();
    }
    if(keys[KeyEvent.VK_Q]) {
      if (missileCounter >= 5) {
        ship.shootMissile(missiles);
        missileCounter = 0;
      }
//      keys[KeyEvent.VK_Q] = false; //FIXME
    }
  }

  public void paint(Graphics2D g) {
    g.setStroke(new BasicStroke(1.5f));
    g.setColor(Color.black);
    g.fillRect(0, 0, W, H);

    ship.paint(g);
    for (Asteroid asteroid : asteroids) {
      asteroid.paint(g);
    }
    for (Missile missile : missiles) {
      missile.paint(g);
    }
    for (Particle particle : particles) {
      particle.paint(g);
    }
    for (Saucer saucer : saucers) {
      saucer.paint(g);
    }
  }

  public void animate() {
    for (Asteroid asteroid : asteroids) {
      asteroid.moveStep();
      if(asteroid.position.getX() > 1100) {
        asteroid.position.setLocation(0, asteroid.position.getY());
      }
      if(asteroid.position.getX() < -100) {
        asteroid.position.setLocation(W, asteroid.position.getY());
      }
      if(asteroid.position.getY() > 800) {
        asteroid.position.setLocation(asteroid.position.getX(), 0);
      }
      if(asteroid.position.getY() < -100) {
        asteroid.position.setLocation(asteroid.position.getX(), H);
      }
    }
    for (Saucer saucer : saucers) {
      saucer.moveStep();
      if(saucer.EastHead.getX() > 1100) {
        saucer.EastHead.setLocation(0, saucer.EastHead.getY());
      }
      if(saucer.EastHead.getX() < -100) {
        saucer.EastHead.setLocation(W, saucer.EastHead.getY());
      }
      if(saucer.EastHead.getY() > 800) {
        saucer.EastHead.setLocation(saucer.EastHead.getX(), 0);
      }
      if(saucer.EastHead.getY() < -100) {
        saucer.EastHead.setLocation(saucer.EastHead.getX(), H);
      }
    }
    ship.moveStep();
    if(ship.position.getX() > W) {
      ship.position.setLocation(0, ship.position.getY());
    }
    if(ship.position.getX() < 0) {
      ship.position.setLocation(W, ship.position.getY());
    }
    if(ship.position.getY() > H) {
      ship.position.setLocation(ship.position.getX(), 0);
    }
    if(ship.position.getY() < 0) {
      ship.position.setLocation(ship.position.getX(), H);
    }

    pieceCounter++;
    if(pieceCounter == 100 && asteroids.size() < maxAmount) {
      generateAsteroids();
      pieceCounter = 0;
    }

    for (Missile missile : missiles) {
      missile.moveStep();
    }
    for (Particle particle : particles) {
      particle.moveStep();
    }
    saucerCounter++;
    if(saucerCounter == 250 && saucers.size() < maxAmount) {
      generateSaucers();
      saucerCounter = 0;
    }

    checkCollisions();
    if(ship.isDead()) {
      ship = new SpaceShip(new Point2D.Double(200.0, 200.0));
      life--;
    }

    missiles.removeIf(m -> !isInBounds(m.position));
    particles.removeIf(p -> p.durationCounter >= p.duration);
    saucers.removeIf(s -> s.state == Saucer.SaucerState.DEAD);
    if(!excessAsteroids.isEmpty()) {
      asteroids.addAll(excessAsteroids);
      excessAsteroids.clear();
    }
  }

  private void checkCollisions() {
    Iterator<Asteroid> aIter = asteroids.iterator();
    while (aIter.hasNext()) {
      Asteroid asteroid = aIter.next();
      Area areaA = new Area(ship.shape);
      Area areaB = new Area(asteroid.shape);
      areaA.intersect(areaB);
      if(!areaA.isEmpty() && ship.lifeCounter > 79 && !ship.isExploding() && !ship.isDead()) {
        ship.startExploding();
        aIter.remove();
        breakAsteroid(asteroid);
      }
    }
    aIter = asteroids.iterator();
    while (aIter.hasNext()) {
      Asteroid asteroid = aIter.next();
      Saucer saucer = null;
      boolean isHit = false;
      Iterator<Saucer> sIter = saucers.iterator();
      while (sIter.hasNext()) {
        saucer = sIter.next();
        Area areaA = new Area(asteroid.shape);
        Area areaB = new Area(saucer.shape);
        areaA.intersect(areaB);
        if(!areaA.isEmpty()) {
          sIter.remove();
          for(int i = 0; i < 10; i++) {
            generateParticles(saucer);
          }
          isHit = true;
        }
      }
      if (isHit) {
        aIter.remove();
        breakAsteroid(asteroid);
        for(int i = 0; i < 10; i++) {
          generateParticles(saucer);
        }
      }
    }
    aIter = asteroids.iterator();
    while (aIter.hasNext()) {
      Asteroid asteroid = aIter.next();
      boolean isHit = false;
      Iterator<Missile> mIter = missiles.iterator();
      while (mIter.hasNext()) {
        Missile missile = mIter.next();
        Area areaA = new Area(asteroid.shape);
        Area areaB = new Area(missile.shape);
        areaA.intersect(areaB);
        if(!areaA.isEmpty()) {
          mIter.remove();
          isHit = true;
        }
      }
      if (isHit) {
        aIter.remove();
        breakAsteroid(asteroid);
      }
    }
    Iterator<Saucer> sIter = saucers.iterator();
    while (sIter.hasNext()) {
      Saucer saucer = sIter.next();
      boolean isHit = false;
      Iterator<Missile> mIter = missiles.iterator();
      while (mIter.hasNext()) {
        Missile missile = mIter.next();
        Area areaA = new Area(saucer.shape);
        Area areaB = new Area(missile.shape);
        areaA.intersect(areaB);
        if(!areaA.isEmpty()) {
          mIter.remove();
          isHit = true;
        }
      }
      if (isHit) {
        sIter.remove();
        for(int i = 0; i < 10; i++) {
          generateParticles(saucer);
        }
      }
    }
    Iterator<Saucer> saucerItr = saucers.iterator();
    while (saucerItr.hasNext()) {
      Saucer saucer = saucerItr.next();
      Saucer saucer2 = null;
      boolean isHit = false;
      Iterator<Saucer> saucerItr2 = saucers.iterator();
      while (saucerItr2.hasNext()) {
        saucer2 = saucerItr2.next();
        Area areaA = new Area(saucer.shape);
        Area areaB = new Area(saucer2.shape);
        areaA.intersect(areaB);
        if(!areaA.isEmpty() && !(saucer.equals(saucer2))) {
          saucerItr2.remove();
          for(int i = 0; i < 10; i++) {
            generateParticles(saucer2);
          }
          isHit = true;
        }
      }
      if (isHit && !(saucer.equals(saucer2))) {
        saucerItr.remove();
        for(int i = 0; i < 10; i++) {
          generateParticles(saucer);
        }
      }
    }
    sIter = saucers.iterator();
    while (sIter.hasNext()) {
      Saucer saucer = sIter.next();
      Area areaA = new Area(ship.shape);
      Area areaB = new Area(saucer.shape);
      areaA.intersect(areaB);
      if(!areaA.isEmpty() && ship.lifeCounter > 79 && !ship.isExploding() && !ship.isDead()) {
        ship.startExploding();
        sIter.remove();
        for(int i = 0; i < 10; i++) {
          generateParticles(saucer);
        }
      }
    }
  }

  private void generateAsteroids() {
    Point2D position = generatePoint();
    Asteroid.Type type = generateType();
    double speed = generateSpeed();
    double angle = generateAngle();
    Asteroid asteroid1 = new Asteroid(position, Asteroid.Size.LARGE, type, speed, angle);
    asteroids.add(asteroid1);
  }

  private void generateSaucers() {
    int num = (int)(Math.random() * 2 + 1);
    Point2D position = generatePoint();
    double speed = generateSpeed();
    double angle = generateAngle();
    Saucer.SaucerState type = Saucer.SaucerState.LARGE;
    if(num == 1) {
      type = Saucer.SaucerState.LARGE;
    } else if (num == 2) {
      type = Saucer.SaucerState.SMALL;
    }

    Saucer saucer = new Saucer(position, type , speed, angle);
    saucers.add(saucer);
  }

  public void breakAsteroid(Asteroid asteroid) {
    Asteroid.Size size = asteroid.size;
    if(size == Asteroid.Size.LARGE) {
      size = Asteroid.Size.MEDIUM;
    }else if (size == Asteroid.Size.MEDIUM) {
      size = Asteroid.Size.SMALL;
    }

    if(!(asteroid.size == Asteroid.Size.SMALL)) {
      Asteroid ast1 = new Asteroid(new Point2D.Double(asteroid.position.getX(), asteroid.position.getY()),
          size, asteroid.type, generateSpeed(), generateAngle());
      Asteroid ast2 = new Asteroid(new Point2D.Double(asteroid.position.getX(), asteroid.position.getY()),
          size, asteroid.type, generateSpeed(), generateAngle());
      excessAsteroids.add(ast1);
      excessAsteroids.add(ast2);
    }

    for(int i = 0; i < 10; i++) {
      generateParticles(asteroid);
    }
  }

  private void generateParticles(Asteroid asteroid) {
    Point2D point = new Point2D.Double(asteroid.position.getX(), asteroid.position.getY());
    Particle particle = new Particle(point, Particle.ParticleType.DOT, generateSpeed(), generateAngle(), generateDuration(asteroid.size));
    particles.add(particle);
  }

  private void generateParticles(Saucer saucer) {
    Point2D point = new Point2D.Double(saucer.centerPoint.getX(), saucer.centerPoint.getY());
    Particle particle = new Particle(point, Particle.ParticleType.DOT, generateSpeed(), generateAngle(), generateDuration(saucer.state));
    particles.add(particle);
  }

  private Asteroid.Type generateType() {
//    int num = (int)(Math.random() * 3); //FIXME
    return Asteroid.Type.TYPE_RANDOM;
//    if(num == 0) {
//      return Asteroid.Type.TYPE_1;
//    } else if (num == 1) {
//      return Asteroid.Type.TYPE_2;
//    } else if (num == 2) {
//      return Asteroid.Type.TYPE_3;
//    } else {
//      return null;
//    }
  }

  private int generateDuration(Asteroid.Size size) {
    int scale = 0;
    if(size == Asteroid.Size.SMALL) {
      scale = 1;
    } else if(size == Asteroid.Size.MEDIUM) {
      scale = 3;
    } else if(size == Asteroid.Size.LARGE) {
      scale = 5;
    }
    return (int)((Math.random() * 4 + 10) * scale);
  }

  private int generateDuration(Saucer.SaucerState state) {
    int scale = 0;
    if(state == Saucer.SaucerState.SMALL) {
      scale = 3;
    } else if(state == Saucer.SaucerState.LARGE) {
      scale = 5;
    }
    return (int)((Math.random() * 4 + 10) * scale);
  }

  private double generateAngle() {
    return Math.random() * 2 * Math.PI;
  }

  private double generateSpeed() {
    return Math.random() * 2 + 1;
  }

  private Asteroid.Size generateSize() {
    int num = (int)(Math.random() * 3); //FIXME
    if(num == 0) {
      return Asteroid.Size.LARGE;
    } else if (num == 1) {
      return Asteroid.Size.MEDIUM;
    } else if (num == 2) {
      return Asteroid.Size.SMALL;
    } else {
      return null;
    }
  }

  private boolean isInBounds(Point2D p) {
    return p.getX() > 0 && p.getX() < W && p.getY() > 0 && p.getY() < H;
  }

  private Point2D generatePoint() {
    double x = Math.random() * 1200 - 100;
    double y = Math.random() * 900 - 100;

    if((x > W || x < 0) && (y > H || y < 0)) {
      return new Point2D.Double();
    } else {
      return generatePoint();
    }
  }
}

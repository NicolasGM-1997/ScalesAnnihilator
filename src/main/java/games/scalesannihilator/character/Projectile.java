package games.scalesannihilator.character;

import static games.scalesannihilator.app.App.actualLvl;
import games.scalesannihilator.app.UtilMath;
import games.scalesannihilator.gameplay.DisplayUI;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Projectile extends Character {
   
   public enum Emisor {PLAYER, FISH};
   public static double RANGE = 250;
   private Emisor emisor;
   private Sphere internSp;
   private Sphere externSp;
   private double radius;
   private Point3D start;
   private Point3D actual;
   private Point3D limit;
   
   public Projectile(Statistics stats, double radius, Point3D target, Emisor emisor){
      super();
      
      this.radius = radius;
      this.emisor = emisor;
      this.limit = target;
      
      PhongMaterial mat = new PhongMaterial();
      mat.setDiffuseColor(Color.rgb(100,200,150,0.2));
      mat.setSpecularColor(Color.WHITE);
      
      internSp = new Sphere(radius / 2);
      externSp = new Sphere(radius);
      
      internSp.setId("internSp");
      externSp.setId("externSp");
      
      internSp.setMaterial(mat);
      externSp.setMaterial(mat);
      
      Statistics newStats = new Statistics(stats);
      setStats(newStats);
      
      getBody().getChildren().addAll(internSp, externSp);
      setTranslateX(getStats().getSpawnX());
      setTranslateY(getStats().getSpawnY());
      setTranslateZ(getStats().getSpawnZ());
      
      start = new Point3D(getTranslateX(), getTranslateY(), getTranslateZ());
      actual = limit.subtract(start).normalize();
      
      animated();
   }
   
   public void updateMove(double delta){
      setTranslateX(getTranslateX() + actual.getX() * (getStats().getVelSwim()) * delta);
      setTranslateY(getTranslateY() + actual.getY() * (getStats().getVelSwim()) * delta);
      setTranslateZ(getTranslateZ() + actual.getZ() * (getStats().getVelSwim()) * delta);
   }
   
   public void checkCollision(Character ch){
      
      double distLimit = UtilMath.distancePoint(limit, localToScene(Point3D.ZERO));
      
      if(distLimit < 2){
         ((ProjectileGroup) getParent()).addRemoveProjectile(this);
      }
      
      double dist = UtilMath.distanceOtherParent(ch, this);
      if(dist > 10) return;
      
      Bounds pr = externSp.localToScene(externSp.getBoundsInLocal());
      Bounds tg;
      
      if(dist < 10){
         if(ch instanceof Player p){
            tg = p.getBackbone().localToScene(p.getBoundsInLocal());
            if(tg.intersects(pr)){
               p.getStats().newLife(getStats().getAttack());
            }
         } else if(ch instanceof Fish f){
            tg = f.getCollision().localToScene(f.getBoundsInLocal());
            if(tg.intersects(pr)){
               double totalDamage = getStats().damageWithoutFury(ch);
               f.getStats().newLife(-totalDamage);
               f.animatedDamage();
               DisplayUI.addShowInfo(
                  actualLvl.getInterface2D(),
                  "damage-player",
                  totalDamage,
                  actualLvl.getInterface2D().getWidth() / 2, 
                  actualLvl.getInterface2D().getHeight() / 2
               );
               ((ProjectileGroup) getParent()).addRemoveProjectile(this);
            }
         }
         
      }
   }
   
   public void animated(){
      ScaleTransition scaleIntern = new ScaleTransition(Duration.millis(250), internSp);
      scaleIntern.setFromX(0.5);
      scaleIntern.setFromY(0.5);
      scaleIntern.setFromZ(0.5);
      scaleIntern.setToX(1.5);
      scaleIntern.setToY(1.5);
      scaleIntern.setToZ(1.5);
      scaleIntern.setAutoReverse(true);
      scaleIntern.setCycleCount(Animation.INDEFINITE);
      scaleIntern.play();
      
      ScaleTransition scaleExtern = new ScaleTransition(Duration.millis(250), externSp);
      scaleExtern.setFromX(1.1);
      scaleExtern.setFromY(1.1);
      scaleExtern.setFromZ(1.1);
      scaleExtern.setToX(0.9);
      scaleExtern.setToY(0.9);
      scaleExtern.setToZ(0.9);
      scaleExtern.setAutoReverse(true);
      scaleExtern.setCycleCount(Animation.INDEFINITE);
      scaleExtern.play();
   }
   
   public static Projectile addVector(Character ch){
      
      double yaw = Math.toRadians(ch.getRotateY().getAngle());
      double pitch = Math.toRadians(ch.getRotateX().getAngle());
      
      Point3D fordward = new Point3D(
         Math.sin(yaw) * Math.cos(pitch), -Math.sin(pitch), Math.cos(yaw) * Math.cos(pitch)
      ).normalize().multiply(RANGE);
      
      Point3D destiny = ch.localToScene(Point3D.ZERO).add(fordward);
      
      Statistics stats = new Statistics();
      stats.setSpawnX(ch.getTranslateX());
      stats.setSpawnY(ch.getTranslateY());
      stats.setSpawnZ(ch.getTranslateZ());
      stats.setAttack(ch.getStats().getAttack());
      stats.setVelSwim(100);
      Emisor emisor = null;
      
      if(ch instanceof Player){
         emisor = Emisor.PLAYER;
      } else if(ch instanceof Fish){
         emisor = Emisor.FISH;
      }
      
      Projectile pr = new Projectile(stats, 2, destiny, emisor);
      
      return pr;
   }
   
}

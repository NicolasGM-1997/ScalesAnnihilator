package games.scalesannihilator.app;

import static games.scalesannihilator.app.App.actualLvl;
import static games.scalesannihilator.app.App.mainMenu;
import games.scalesannihilator.character.Player;
import games.scalesannihilator.character.Projectile;
import games.scalesannihilator.enviroment.Enviroment;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.input.KeyCode;

public class Peripheral {
   
   public static Set<KeyCode> keysPressed = new HashSet<>();   
   public static void movePlayer(Player ply, Enviroment env, double delta){
      
      double swim = ply.getStats().getVelSwim();
      
      double velocityAnimated = 10 * delta;
      double velocityFordward = swim * delta;
      double velocityDeep = swim / 1.5 * delta;
      double velocityReverse = swim / 2 * delta;
      double velocityRotateX = swim / 3 * delta;
      double velocityRotateY = swim / 2.5 * delta;
      
      if(ply.isMap()){
         velocityFordward /= 10;
         ply.getStats().newLife(-ply.getStats().getMaxLife() * 0.05 * delta);
      }

      ply.swimming(velocityAnimated);
      env.seeLimits(ply);
         
      if(keysPressed.contains(KeyCode.W)){
              
         ply.moveFordward(velocityFordward);

         ply.setTranslateX(Math.max(env.getMinX(), Math.min(env.getMaxX(), ply.getTranslateX())));
         ply.setTranslateZ(Math.max(env.getMinZ(), Math.min(env.getMaxZ(), ply.getTranslateZ())));

         ply.setSwim(true);
      }
      if(keysPressed.contains(KeyCode.S)){
         ply.moveFordward(-velocityReverse);
      }
      if(keysPressed.contains(KeyCode.D)){
         ply.rotateY(velocityRotateY);
      }
      if(keysPressed.contains(KeyCode.A)){
         ply.rotateY(-velocityRotateY);
      }
      if(keysPressed.contains(KeyCode.Q)){
         ply.rotateX(velocityRotateX);
      }
      if(keysPressed.contains(KeyCode.E)){
         ply.rotateX(-velocityRotateX);
      }
      if(keysPressed.contains(KeyCode.UP)){
         ply.moveY(-velocityDeep);
         ply.setSwim(true);
      }
      if(keysPressed.contains(KeyCode.DOWN)){
         ply.moveY(velocityDeep);
         ply.setSwim(true);
      }
      if(keysPressed.contains(KeyCode.LEFT)){
         ply.getCamera().zoomout(delta);
      }
      if(keysPressed.contains(KeyCode.RIGHT)){
         ply.getCamera().zoomin(delta);
      }
      if(keysPressed.contains(KeyCode.C)){
         ply.getCamera().orbitCircle(delta, 100, 1);
         ply.getCamera().setOrbit(true);
      }
      if(keysPressed.contains(KeyCode.P)){
         actualLvl.getInterface2D().lookup("#scrollImprove").setVisible(true);
      }
      if(keysPressed.contains(KeyCode.O)){
         actualLvl.getInterface2D().lookup("#scrollImprove").setVisible(false);
      }
      if(keysPressed.contains(KeyCode.SPACE)){ 
         if(ply.getMaxDeltaAttack() < ply.getDeltaAttack()){
            Projectile pr = Projectile.addVector(ply);
            actualLvl.getProjectiles().addProjectile(pr);
            ply.setDeltaAttack(0);
         }
      }

      if(!keysPressed.contains(KeyCode.C) && ply.getCamera().isOrbit()){
         ply.getCamera().recalibrate();
         ply.getCamera().setTranslateX(0);
         ply.getCamera().setTranslateY(-25);
         ply.getCamera().setTranslateZ(-75);
         ply.getCamera().setOrbit(false);
      }
      if(!keysPressed.contains(KeyCode.W)){
         ply.setSwim(false);
      }
      
      if(ply.getTranslateY() > 300){
         double terrainY = ply.heightMap(velocityFordward, env);

         if (ply.getTranslateY() > terrainY) {
            ply.setTranslateY(terrainY);
            ply.setMap(true);
         } else {
            ply.setMap(false);
         }  
      }
   }
   
   public static void setRunningGame(){
      if(keysPressed.contains(KeyCode.ESCAPE)){
         ProcessGame.pauseGameplay = true;
      }
      if(keysPressed.contains(KeyCode.P)){
         ProcessGame.pauseGameplay = false;
      }
      if(keysPressed.contains(KeyCode.DELETE)){
         mainMenu.gameReturnPlayer();
         System.out.println("loopid:"+ProcessGame.loop);
      }
   }
   
   public static Set<KeyCode> getKeysPressed(){
      return keysPressed;
   }   
}
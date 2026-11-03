package games.scalesannihilator.character;

import games.scalesannihilator.app.UtilMath;
import games.scalesannihilator.character.Statistics.Pattern;
import games.scalesannihilator.gameplay.DisplayUI;
import games.scalesannihilator.gameplay.Level;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Fish extends Character{
   
   private Player player;
   private double angCircle = 0;
   private double initialAngY = 0;
   private double detectionRange = 100;
   
   private AnchorPane infoPane = new AnchorPane();
   private Region barHealth;
   
   private boolean fordward = true;
   private boolean repose = true;
   private boolean chasing = false;
   
   private int targetPoint = 0;
   private int maxRegion = 0;
   private int heightRegion = 0;
   
   private Color normalFish = Color.WHITE;
   private Color regenFish = Color.GREEN;
   private Color bloodFish = Color.SALMON;
   private Color speedFish = Color.BLUE;
   private Color tankFish = Color.RED;
   private Color specularFish = Color.WHITE;
   
   public Fish(Player player, Statistics stats) {
      super();
      
      this.player = player;
      Statistics newStat = new Statistics(stats);
      setStats(newStat);
      
      barHealth = new Region();
      
      Box backbone = null;
      Box boxCollision = null;
      
      switch(getStats().getSize()){
         case SMALL -> {
            backbone = new Box(1,1,3);
            boxCollision = new Box(0.9, 0.9, 2.8);
            maxRegion = 5;
            heightRegion = 2;
         }
         case BASIC -> {
            backbone = new Box(2.5, 2.5, 6);
            boxCollision = new Box(2.3, 2.3, 5.7);
            maxRegion = 7;
            heightRegion = 4;
         }
         case BIG -> {
            backbone = new Box(7, 7, 12);
            boxCollision = new Box(6.6, 6.6, 11);
            maxRegion = 9;
            heightRegion = 8;
         }
         case BOSS -> {
            backbone = new Box(12, 12, 25);
            boxCollision = new Box(11, 11, 23);
            maxRegion = 11;
            heightRegion = 10;
         }

      }
      
      if(getStats().getPattern() == Pattern.LINEAR){
         double difY = Math.random() * 360;
         initialAngY = difY;
      }
      
      PhongMaterial mat = new PhongMaterial();
      
      mat.setDiffuseColor(returnTypeColor());
      mat.setSpecularColor(specularFish);
      backbone.setMaterial(mat);
      backbone.setId("Backbone");
      boxCollision.setId("Collision");
      boxCollision.setVisible(false);
      
      getBody().getChildren().addAll(backbone, boxCollision);
      
      setTranslateX(getStats().getSpawnX());
      setTranslateY(getStats().getSpawnY());
      setTranslateZ(getStats().getSpawnZ());
      
      setMaxDeltaAttack(1 / getStats().getAttack());
      setInitializePane();
   }
   
   //// getBackbone
   
   public Box getBackbone(){
      return (Box) getBody().lookup("#Backbone");
   }
   
   public Box getCollision(){
      return (Box) getBody().lookup("#Collision");
   }
   
   public Region getBarHealth(){
      return barHealth;
   }
   
   //// statPane
   
   public void setInitializePane(){
      DisplayUI.setClassStyle(infoPane, "info-fish");
      infoPane.setMaxWidth(maxRegion);
      infoPane.setPrefSize(maxRegion, 0.25);
      infoPane.setTranslateY(-heightRegion);
      infoPane.setTranslateX(-maxRegion / 2);
      infoPane.setTranslateZ(0);
      
      infoPane.getTransforms().setAll(
         new Rotate(-getRotateX().getAngle(), Rotate.X_AXIS),
         new Rotate(-getRotateY().getAngle(), Rotate.Y_AXIS),
         new Rotate(-getRotateZ().getAngle(), Rotate.Z_AXIS)  
      );
      
      getChildren().add(infoPane);
      setInitialStatus(infoPane);
   }
   
   public void setInitialStatus(AnchorPane parent){
      barHealth.setMaxWidth(maxRegion);
      barHealth.setPrefSize(maxRegion, 0.25);
      
      DisplayUI.setClassStyle(barHealth, "bar-fish-green");      
      parent.getChildren().add(barHealth);
   }
   
   public void actualizeStatus(){
      
      double dist = UtilMath.distanceOtherParent(barHealth, player.getCamera());
      
      if(dist >= detectionRange * 2) {
         infoPane.setVisible(false);
         return;
      }
      
      infoPane.setVisible(true);
      
      UtilMath.yawAndPitch();
      infoPane.setRotationAxis(Rotate.Y_AXIS);
      infoPane.setRotate(-getRotateY().getAngle() + UtilMath.yaw);
      
      double percentLife = getStats().getLife() / getStats().getMaxLife();
      barHealth.setPrefWidth(percentLife * barHealth.getMaxWidth());
      
      if(percentLife < 0.25){
         barHealth.getStyleClass().clear();
         DisplayUI.setClassStyle(barHealth, "bar-fish-red");
      } else if(percentLife < 0.5){
         barHealth.getStyleClass().clear();
         DisplayUI.setClassStyle(barHealth, "bar-fish-yellow");
      } else {
         barHealth.getStyleClass().clear();
         DisplayUI.setClassStyle(barHealth, "bar-fish-green");
      }
   }
   
   //// moveFish
   
   public void updateRepose(double delta){
      switch(getStats().getPattern()){
         case CIRCLE ->{
            angCircle += getStats().getVelSwim() * delta / 30;
            double newX = getStats().getSpawnX() + Math.cos(angCircle) * 35;
            double newZ = getStats().getSpawnZ() + Math.sin(angCircle) * 35;
            
            double dx = newX - getTranslateX();
            double dz = newZ - getTranslateZ();
            
            setTranslateX(newX);
            setTranslateZ(newZ);
            
            double yaw = Math.toDegrees(Math.atan2(dx, dz));
            getRotateY().setAngle(yaw);
            
         }
         case LINEAR -> {

            double speed = getStats().getVelSwim() * delta;
            double distance = UtilMath.distanceSpawn(this);
            UtilMath.yawAndPitch();
            
            if (fordward) {

                double angleRad = Math.toRadians(initialAngY);
                double moveX = Math.sin(angleRad) * speed;
                double moveZ = Math.cos(angleRad) * speed;

                setTranslateX(getTranslateX() + moveX);
                setTranslateZ(getTranslateZ() + moveZ);
                getRotateY().setAngle(initialAngY);

                if (distance >= 50) {
                    fordward = false;
                }

            } else {

                if (distance <= speed) {

                    setTranslateX(getStats().getSpawnX());
                    setTranslateZ(getStats().getSpawnZ());
                    fordward = true;

                } else {

                    double dirX = UtilMath.dx / distance;
                    double dirZ = UtilMath.dz / distance;
                    setTranslateX(getTranslateX() + dirX * speed);
                    setTranslateZ(getTranslateZ() + dirZ * speed);
                }

                getRotateY().setAngle(UtilMath.yaw);
            }
        }  
         case TRIANGLE ->{
            double[][] trianglePoints = {
               {0,0,0},{100,0,0},{50,0,100}
            };
            double speed = getStats().getVelSwim() * delta;
            double[] target = trianglePoints[targetPoint];
            double targetX = getStats().getSpawnX() + target[0];
            double targetZ = getStats().getSpawnZ() + target[2];
            double dx = targetX - getTranslateX();
            double dz = targetZ - getTranslateZ();
            double dist = Math.sqrt(dx*dx + dz*dz);
            
            if(dist < 2){
               targetPoint = (targetPoint + 1) % trianglePoints.length;   
            } else {
               setTranslateX(getTranslateX() + dx/dist * speed);
               setTranslateZ(getTranslateZ() + dz/dist * speed);
               double yaw = Math.toDegrees(Math.atan2(dx, dz));
               getRotateY().setAngle(yaw);
            }
         }
      }
   }
   
   public void updateChasing(double delta, double dist){
      
      double velocity = getStats().getVelSwim() * delta;
      
      setTranslateX(getTranslateX() + UtilMath.dx/dist * velocity);
      setTranslateY(getTranslateY() + UtilMath.dy/dist * velocity);
      setTranslateZ(getTranslateZ() + UtilMath.dz/dist * velocity);
      
      UtilMath.yawAndPitch();
      
      getRotateY().setAngle(UtilMath.yaw);
      getRotateX().setAngle(UtilMath.pitch);
   }
   
   public void updateReturn(double delta){
      double dist = UtilMath.distanceSpawn(this);
      
      UtilMath.yawAndPitch();
      
      getRotateY().setAngle(UtilMath.yaw);
      getRotateX().setAngle(UtilMath.pitch);
      
      double velocity =  getStats().getVelSwim() * delta;
      
      if(dist > 2){
         setTranslateX(getTranslateX() + UtilMath.dx/dist * velocity);
         setTranslateY(getTranslateY() + UtilMath.dy/dist * velocity);
         setTranslateZ(getTranslateZ() + UtilMath.dz/dist * velocity);
      } else {
         getRotateX().setAngle(0);
         getRotateY().setAngle(0);
         repose = true;
      }
   }
   
   public void moveFish(Level lvl, double delta){

      double dist = UtilMath.distanceOtherParent(player, this);

      chasing = (dist <= detectionRange);

      if(chasing){
         updateChasing(delta, dist);
         repose = false;
      } else if(repose){
         updateRepose(delta);
      } else{
         updateReturn(delta);
      }
      
      avoidOverlap(lvl);    
   }
   
   public void avoidOverlap(Level lvl){
      for (Node n: lvl.getShoal().getChildren()){
         if(n instanceof Fish other){

            double dist = UtilMath.distanceSelfParent(this, other);
            
            if(other == this) continue;
            if(dist > 50) continue;
            
            double minDist = getBoundFish(getBackbone()) + getBoundFish(other.getBackbone()) - 0.5;

            if (dist < minDist && dist > 0) {
               double push = (minDist - dist) * 1;
               setTranslateX(getTranslateX() + UtilMath.dx/dist * push);
               setTranslateY(getTranslateY() + UtilMath.dy/dist * push);
               setTranslateZ(getTranslateZ() + UtilMath.dz/dist * push);
               other.setTranslateX(other.getTranslateX() - UtilMath.dx/dist * push);
               other.setTranslateY(other.getTranslateY() - UtilMath.dy/dist * push);
               other.setTranslateZ(other.getTranslateZ() - UtilMath.dz/dist * push);
            }
         }
      }
      
      double dist = UtilMath.distanceSelfParent(this, player);
      if(dist > 50) return;
      double minDist = getBoundFish(getBackbone()) + getBoundPlayer(player.getBackbone()) - 0.5;
      
      if (dist < minDist && dist > 0) {
         
         if(getMaxDeltaAttack() < getDeltaAttack()){
            double totalDamage = getStats().damageWithFury(player);
            player.getStats().newLife(-totalDamage);
            player.animatedDamage();
            DisplayUI.addShowInfo(
               lvl.getInterface2D(),
               "damage-fish",
               totalDamage,
               lvl.getInterface2D().getWidth() / 2, 
               lvl.getInterface2D().getHeight() / 2
            );
            setDeltaAttack(0);
         }
         
         double push = (minDist-dist) * 0.5;
        
         setTranslateX(getTranslateX() + UtilMath.dx/dist * push);
         setTranslateY(getTranslateY() + UtilMath.dy/dist * push);
         setTranslateZ(getTranslateZ() + UtilMath.dz/dist * push);
      }
   }
   
   //// Colisiones
   
   public double getBoundFish(Box b){
      return Math.sqrt(
         Math.pow(b.getWidth()/2, 2) +
         Math.pow(b.getHeight()/2, 2) +
         Math.pow(b.getDepth()/2, 2)
      );
   }
   
   public double getBoundPlayer(Cylinder c){
      return Math.sqrt(
         Math.pow(c.getRadius()/2, 2) +
         Math.pow(c.getHeight()/2, 2)
      );
   }
   
   public Color returnTypeColor(){
      Color newColor = Color.WHITE;
      switch(getStats().getType()){
         case NORMAL -> newColor = normalFish;
         case BLOOD -> newColor = bloodFish;
         case REGENERATE -> newColor = regenFish;
         case SPEED -> newColor = speedFish;
         case TANK -> newColor = tankFish;
      }
      return newColor;
   }
   
   public void animatedDamage(){
      for(Node n: getBody().getChildren()){
         if(n instanceof Shape3D s){
            if(s.getId() == null) continue;
            if(!s.getId().contains("Collision")){
               if(s.getMaterial() instanceof PhongMaterial m){
                  
                  Timeline timeline = new Timeline(
                     new KeyFrame(Duration.ZERO, new KeyValue(m.diffuseColorProperty(), Color.WHITE)),
                     new KeyFrame(Duration.ZERO, new KeyValue(m.specularColorProperty(), Color.WHITE)), 
                     new KeyFrame(Duration.millis(100), new KeyValue(m.diffuseColorProperty(), returnTypeColor())),
                     new KeyFrame(Duration.millis(100), new KeyValue(m.specularColorProperty(), specularFish))
                  );
                  
                  timeline.play();
               }
            }
         }
      }
   }
}

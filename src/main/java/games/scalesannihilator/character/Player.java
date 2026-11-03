package games.scalesannihilator.character;

import games.scalesannihilator.app.LoadFileGame;
import games.scalesannihilator.app.Camera;
import games.scalesannihilator.app.UtilMath;
import games.scalesannihilator.enviroment.Enviroment;
import games.scalesannihilator.enviroment.Terrain;
import games.scalesannihilator.gameplay.DisplayUI;
import games.scalesannihilator.gameplay.Level;

import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Player extends Character{
   
   private ArrayList<Shape3D> bodyparts;
   private ArrayList<Shape3D> tailparts;
   private ArrayList<Shape3D> collisions = new ArrayList<>();
   private ArrayList<Point3D> pbody = new ArrayList<>();
   private ArrayList<Point3D> ptail = new ArrayList<>();
   private PhongMaterial bodyMat;
   private PhongMaterial collisionMat;
   private Color diffusePlayer = Color.YELLOW;
   private Color specularPlayer = Color.DARKBLUE;
   private double animationMove = 0;
   private boolean swim = false;
   private boolean map = false;
   private Camera camera;
   
   public Player(Camera camera){
      super();
      bodyparts();
      
      try{
         LoadFileGame.loadStats("/data/spawns.csv", 0);
         setStats(LoadFileGame.plyStats);
      } catch(Exception ex){
         ex.printStackTrace();
      } 
      
      this.camera = camera;
      this.camera.transport(0, -25, -75);
      this.camera.rotateX(-10);
      this.camera.setInitialX(-10);   
      
      addBodyParts();
      
      setMaxDeltaAttack(1.0 / getStats().getVelAtk());
      getChildren().add(this.camera);
      
      setTranslateX(getStats().getSpawnX());
      setTranslateY(getStats().getSpawnY());
      setTranslateZ(getStats().getSpawnZ());
   }
   
   //// BodyParts
   
   public void bodyparts(){
      
      bodyparts = new ArrayList<>();
      tailparts = new ArrayList<>();
      
      Sphere head = new Sphere(1.5);
      MeshView dorsalFin = GeneratePrismas.createCone(1, 2, 5);
      MeshView leftFin = GeneratePrismas.createCone(0.5f, 4, 3);
      MeshView rightFin = GeneratePrismas.createCone(0.5f, 4, 3);
      Cylinder backbone = new Cylinder(1.5, 6);
      
      leftFin.setId("Left");
      rightFin.setId("Right");
      backbone.setId("Backbone");
      
      Cylinder tail1 = new Cylinder(1.3, 1.5);
      Cylinder tail2 = new Cylinder(1.1, 1.5);
      Cylinder tail3 = new Cylinder(0.9, 1.5);
      Cylinder tail4 = new Cylinder(0.7, 1.5);
      Cylinder tail5 = new Cylinder(0.5, 1.5);
      Sphere tailFin = new Sphere(0.5);
      MeshView tailSupFin = GeneratePrismas.createTriangle(1, -2, 1);
      MeshView tailInfFin = GeneratePrismas.createTriangle(1, 2, 1);
      
      pbody.add(new Point3D(0,0,4));      
      pbody.add(new Point3D(0,-1.5,0));
      pbody.add(new Point3D(-2.5, 0, 0));
      pbody.add(new Point3D(2.5, 0, 0));
      pbody.add(new Point3D(0,0,0));
      
      ptail.add(new Point3D(0,0,-3.75));
      ptail.add(new Point3D(0,0,-5.25));
      ptail.add(new Point3D(0,0,-6.75));
      ptail.add(new Point3D(0,0,-8.25));
      ptail.add(new Point3D(0,0,-9.75));
      ptail.add(new Point3D(0,0,-10.25));
      ptail.add(new Point3D(0,1,-10.25));
      ptail.add(new Point3D(0,-1,-10.25));
      
      bodyparts.add(head);      
      bodyparts.add(dorsalFin);
      bodyparts.add(leftFin);
      bodyparts.add(rightFin);
      bodyparts.add(backbone);
      
      tailparts.add(tail1);
      tailparts.add(tail2);
      tailparts.add(tail3);
      tailparts.add(tail4);
      tailparts.add(tail5);
      tailparts.add(tailFin);
      tailparts.add(tailSupFin);
      tailparts.add(tailInfFin);
   }   
   
   public void addBodyParts(){
      
      int index = 0;
      
      bodyMat = new PhongMaterial();
      bodyMat.setDiffuseColor(diffusePlayer);
      bodyMat.setSpecularColor(specularPlayer);
      bodyMat.setSpecularPower(8);
      
      collisionMat = new PhongMaterial();
      collisionMat.setDiffuseColor(Color.rgb(100, 100, 100, 0.5));
      collisionMat.setSpecularColor(Color.WHITE); 
      
      for(Shape3D s: bodyparts){
         
         if(s instanceof Cylinder){
            s.setRotationAxis(Rotate.X_AXIS);
            s.setRotate(90);
         }
         if(s.getId() != null){
            if(s.getId().equals("Left")){
               s.setRotationAxis(Rotate.Z_AXIS);
               s.setRotate(-90);
            } else if(s.getId().equals("Right")){
               s.setRotationAxis(Rotate.Z_AXIS);
               s.setRotate(90);
            }
         }
         
         s.setTranslateX(pbody.get(index).getX());
         s.setTranslateY(pbody.get(index).getY());
         s.setTranslateZ(pbody.get(index).getZ());
         s.setMaterial(bodyMat);
         getBody().getChildren().add(s);
         
         index++;
      }
      
      index = 0;
      
      for (Shape3D s: tailparts) {
         if(s instanceof Cylinder){
            s.setRotationAxis(Rotate.X_AXIS);
            s.setRotate(90);
         }
         
         s.setTranslateX(ptail.get(index).getX());
         s.setTranslateY(ptail.get(index).getY());
         s.setTranslateZ(ptail.get(index).getZ());
         s.setMaterial(bodyMat);
         getBody().getChildren().add(s);
         
         index++;
      }
      
      Sphere head = new Sphere(2);
      head.setMaterial(collisionMat);
      head.setTranslateZ(4);
      head.setId("HeadCollision");
      
      Sphere body = new Sphere(3);
      body.setMaterial(collisionMat);
      body.setId("BodyCollision");
      
      Sphere tail = new Sphere(2);
      tail.setMaterial(collisionMat);
      tail.setTranslateZ(-10);
      tail.setId("TailCollision");
      
      collisions.add(head);
      collisions.add(body);
      collisions.add(tail);
      
      for(Shape3D s: collisions){
         s.setVisible(false);
         getBody().getChildren().add(s);
      }   
   }
   
   //// getbackbone
   
   public Cylinder getBackbone(){
      return (Cylinder) getBody().lookup("#Backbone");
   }
   
   public Sphere getHeadCollision(){
      return (Sphere) getBody().lookup("#HeadCollision");
   }
   
   public Sphere getTailCollision(){
      return (Sphere) getBody().lookup("#TailCollision");
   }
   
   //// MovePlayer
      
   public void moveFordward(double velocity){
      
      Transform transform = getLocalToParentTransform();
      double dirX = transform.getMxz();
      double dirY = transform.getMyz();
      double dirZ = transform.getMzz();
      
      double deltaX = velocity * dirX;
      double deltaY = velocity * dirY;
      double deltaZ = velocity * dirZ;
      
      moveX(deltaX);
      moveY(deltaY);
      moveZ(deltaZ);
   }
      
   public void swimming(double delta){
     
      if(swim){
         animationMove += delta * 2;  
      } else {
         animationMove += delta * 0.2;  
      }      
         
      double amplitude = 0.2;
      double amplitudeFin = 20.0;
      double oscilation = Math.sin(animationMove / 3);
     
      for (int i = 0; i < tailparts.size(); i++) {
         Shape3D part = tailparts.get(i);
         Point3D point = ptail.get(i);
         double phaseDelay = i * 0.5;
         double offset = Math.sin(animationMove - phaseDelay) * amplitude;
         part.setTranslateX(point.getX() + offset);
         part.setTranslateY(point.getY());
         part.setTranslateZ(point.getZ());
      }
      
      for (Node fin: getBody().getChildren()){
         if(fin.getId() != null){
             if(fin.getId().equals("Left")){
                fin.setRotationAxis(Rotate.Z_AXIS);
                fin.setRotate(-90 + (oscilation * amplitudeFin));
             } else if(fin.getId().equals("Right")){
                fin.setRotationAxis(Rotate.Z_AXIS);
                fin.setRotate(90 - (oscilation * amplitudeFin));
             }
         }
      }
   }
   
   public double heightMap(double velocity, Enviroment env){
      double posX = getTranslateX();
      double posZ = getTranslateZ();

      double nextX = posX + velocity * getLocalToParentTransform().getMxz();
      double nextZ = posZ + velocity * getLocalToParentTransform().getMzz();
      double terrainY = Terrain.getHeightCoords(env.actualTerrain(), nextX, nextZ);
      
      return terrainY;
   }
   
   //// Collision Damage Fishes
   
   public void damageFishes(Level lvl){
      
      Bounds hb = getHeadCollision().localToScene(getHeadCollision().getBoundsInLocal());
      Bounds tb = getTailCollision().localToScene(getTailCollision().getBoundsInLocal());
      
      for (Node n: lvl.getShoal().getChildren()) {
         if(n instanceof Fish f){
            if(UtilMath.distanceOtherParent(this, f) > 50) continue;
            Bounds fb = f.getCollision().localToScene(f.getCollision().getBoundsInLocal());
            
            if(hb.intersects(fb) || tb.intersects(fb)){
               
               if(getMaxDeltaAttack() < getDeltaAttack()){
                  getStats().newFury(2);
                  double totalDamage = getStats().damageWithFury(f);
                  f.getStats().newLife(-totalDamage);
                  DisplayUI.addShowInfo(
                     lvl.getInterface2D(), 
                     "damage-player", 
                     totalDamage, 
                     lvl.getInterface2D().getWidth() / 2, 
                     lvl.getInterface2D().getHeight() / 2
                  );
                  setDeltaAttack(0);
                  f.animatedDamage();
               }
            }
         }
      }
      
   }
   
   public void animatedDamage(){
      ScaleTransition scale = new ScaleTransition(Duration.millis(150), getBody());
      scale.setFromX(1);
      scale.setFromY(1);
      scale.setFromZ(1);
      scale.setToX(1.1);
      scale.setToY(1.1);
      scale.setToZ(1.1);
      scale.setAutoReverse(true);
      scale.setCycleCount(1);
      scale.play();
      
      for(Node n: getBody().getChildren()){
         if(n instanceof Shape3D s){
            if(s.getId() == null) continue;
            if(!s.getId().contains("Collision")){
               if(s.getMaterial() instanceof PhongMaterial m){
                  
                  Timeline timeline = new Timeline(
                     new KeyFrame(Duration.ZERO, new KeyValue(m.diffuseColorProperty(), Color.RED)),
                     new KeyFrame(Duration.ZERO, new KeyValue(m.specularColorProperty(), Color.RED)), 
                     new KeyFrame(Duration.millis(200), new KeyValue(m.diffuseColorProperty(), diffusePlayer)),
                     new KeyFrame(Duration.millis(200), new KeyValue(m.specularColorProperty(), specularPlayer))
                  );
                  
                  timeline.play();
               }
            }
         }
      }
   }
   
   public void regenerate(double delta){
      getStats().newLife(getStats().getMaxLife() * 0.05 * delta);
      getStats().newFury(getStats().getMaxFury() * -0.2 * delta);
   }
   
   //// PrintPosition
   
   public String position(){
      return String.format(
         "%.1f : %.1f : %.1f : %.1f°X : %.1f°Y : %.1f°Z", 
         getTranslateX(), getTranslateY(), getTranslateZ(),
         Math.abs((getRotateX().getAngle() % 360) + 360) % 360, 
         Math.abs((getRotateY().getAngle() % 360) + 360) % 360,
         Math.abs((getRotateZ().getAngle() % 360) + 360) % 360);
   }
}

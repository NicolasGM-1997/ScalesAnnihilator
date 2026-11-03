package games.scalesannihilator.character;

import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Character extends Group{
   
   private Group body;
   private Statistics stats;
   
   private Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
   private Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
   private Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);
   
   private double velGravity = 0;
   
   private double angX = 0;
   private double angY = 0;
   private double initX = 0;
   private double initY = 0;
   
   private double maxDeltaAttack = 0;
   private double deltaAttack = 0;
   
   public Character(){
      body = new Group();
      stats = new Statistics();
      
      getChildren().add(body);
      getTransforms().addAll(rotateZ, rotateY, rotateX);
   }
   
   //// MoveCaracter
   
   public void moveX(double X){
      setTranslateX(getTranslateX() + X);
   }
   
   public void moveY(double Y){
      setTranslateY(getTranslateY() + Y);
   }
      
   public void moveZ(double Z){
      setTranslateZ(getTranslateZ() + Z);  
   }
   
   public void transport(double X, double Y, double Z){
      setTranslateX(X);
      setTranslateY(Y);
      setTranslateZ(Z);
   }
   
   public void rotateX(double factor){
      rotateX.setAngle(rotateX.getAngle() + factor);
      rotateX.setAngle(Math.max(-30, Math.min(30, rotateX.getAngle())));
   }
   
   public void rotateY(double factor){
      rotateY.setAngle(rotateY.getAngle() + factor);
   }
   
   public void rotateZ(double factor){
      rotateZ.setAngle(rotateZ.getAngle() + factor);
   }
  
}

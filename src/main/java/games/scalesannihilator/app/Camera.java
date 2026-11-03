package games.scalesannihilator.app;

import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Camera extends PerspectiveCamera{
   
   private Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
   private Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
   private boolean orbit = false;
   private double factor = 1;
   private double angle = 0;
   private double rotationX = 0;
   private double rotationY = 0;
   private double initialX = 0;
   private double initialY = 0;
   
   public Camera(boolean perspective){
      super(perspective);
      getTransforms().addAll(rotateX, rotateY);
   }
   
   public Camera(boolean perspective, double renderNear, double renderFar){
      super(perspective);
      this.setNearClip(renderNear);
      this.setFarClip(renderFar);
      getTransforms().addAll(rotateX, rotateY);
   }
   
   //// MoveCamera
   
   public void moveX(double X){
      setTranslateX(getTranslateX() + X);
   }
   
   public void moveY(double Y){
      setTranslateY(getTranslateY() + Y);
   }
      
   public void moveZ(double Z){
      setTranslateZ(getTranslateZ() + Z);
   }
   
   public void rotateX(double X){
      rotateX.setAngle(rotateX.getAngle() + X);
   }
   
   public void rotateY(double Y){
      rotateY.setAngle(rotateY.getAngle() + Y);
   }
   
   public void move(double X, double Y, double Z){
      moveX(X);
      moveY(Y);
      moveZ(Z);
   }
   
   public void transport(double X, double Y, double Z){
      setTranslateX(X);
      setTranslateY(Y);
      setTranslateZ(Z);
   }
   
   public void zoomin(double delta){
      factor -= 1 * delta;
      factor = Math.max(0.8, factor);
      setTranslateZ(-75 * factor); 
   }
   
   public void zoomout(double delta){
      factor += 0.5 * delta;
      factor = Math.min(1.2, factor);
      setTranslateZ(-75 * factor);
   }
   
   public void recalibrate(){
      rotationX = initialX;
      rotationY = initialY;
      rotateX.setAngle(rotationX);
      rotateY.setAngle(rotationY);
   }
   
   public void invertAxisX(){
      getTransforms().add(new Scale(-1,1,1));
   }
   
   public void invertAxisY(){
      getTransforms().add(new Scale(1,-1,1));
   }
   
   public void invertAxisZ(){
      getTransforms().add(new Scale(1,1,-1));
   }
   
   public void orbitCamera(double deltaX, double deltaY){
      rotationY += deltaX * 0.1;
      rotationX -= deltaY * 0.1;
      
      rotationX = Math.max(-10, Math.min(10, rotationX));
      rotationY = Math.max(-10, Math.min(10, rotationY));
      
      rotateX.setAngle(rotationX);
      rotateY.setAngle(rotationY);
   }
   
   public void orbitCircle(double delta, double fixedRadius, double height){
      
      angle += 2 * delta;
      double camX = fixedRadius * Math.cos(angle);
      double camZ = fixedRadius * Math.sin(angle);
      setTranslateX(camX);
      setTranslateY(height);
      setTranslateZ(camZ);
      double yaw = Math.toDegrees(Math.atan2(-camX, -camZ));
      double distance = Math.sqrt(camX * camX + camZ * camZ);
      double pitch = Math.toDegrees(Math.atan2(-height, distance));

      rotateY.setAngle(yaw);
      rotateX.setAngle(-pitch);
   }
   
   public String position(){
      return String.format(
         "X: %.1f | Y: %.1f | Z: %.1f | X°: %.1f | Y°: %.1f" , getTranslateX(), getTranslateY(), getTranslateZ(), 
         Math.abs((rotateX.getAngle() % 360) + 360) % 360, Math.abs((rotateY.getAngle() + 360) % 360));
   }
}

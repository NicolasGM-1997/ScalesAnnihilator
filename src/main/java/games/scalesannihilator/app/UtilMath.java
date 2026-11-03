package games.scalesannihilator.app;

import games.scalesannihilator.character.Fish;
import javafx.geometry.Point3D;
import javafx.scene.Node;

public class UtilMath {
   
   public static double dx;
   public static double dy;
   public static double dz;
   public static double yaw;
   public static double pitch;
   
   public static double distanceSelfParent(Node n1, Node n2){
      dx = n1.getTranslateX() - n2.getTranslateX();
      dy = n1.getTranslateY() - n2.getTranslateY();
      dz = n1.getTranslateZ() - n2.getTranslateZ();
      return Math.sqrt(dx*dx + dy*dy + dz*dz);
   }
   
   public static double distanceOtherParent(Node n1, Node n2){
      Point3D p1 = n1.localToScene(Point3D.ZERO);
      Point3D p2 = n2.localToScene(Point3D.ZERO);
      return distancePoint(p1, p2);
   }
   
   public static double distanceSpawn(Fish f){
      dx = f.getStats().getSpawnX() - f.getTranslateX();
      dy = f.getStats().getSpawnY() - f.getTranslateY(); 
      dz = f.getStats().getSpawnZ() - f.getTranslateZ(); 
      return Math.sqrt(dx*dx + dy*dy + dz*dz);
   }
   
   public static double distancePoint(Point3D p1, Point3D p2){
      dx = p1.getX() - p2.getX();
      dy = p1.getY() - p2.getY();
      dz = p1.getZ() - p2.getZ();
      return Math.sqrt(dx*dx + dy*dy + dz*dz);
   }
   
   public static double distanceNodeAndPoint(Node n, Point3D p){
      dx = n.getTranslateX() - p.getX();
      dy = n.getTranslateY() - p.getY();
      dz = n.getTranslateZ() - p.getZ();
      return Math.sqrt(dx*dx + dy*dy + dz*dz);
   }
   
   public static void yawAndPitch(){
      yaw = Math.toDegrees(Math.atan2(dx, dz));
      pitch = Math.toDegrees(Math.atan2(-dy, Math.sqrt(dx*dx + dz*dz)));
   }   
}

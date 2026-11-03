package games.scalesannihilator.character;

import javafx.geometry.Point3D;
import javafx.scene.shape.Arc;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RespawnTime {
   
   private Statistics stats;
   private int index;
   private Point3D point;
   private double respawnTime;
   private double currentTime;
   private Arc arc;
   
   public RespawnTime(){}
   
}

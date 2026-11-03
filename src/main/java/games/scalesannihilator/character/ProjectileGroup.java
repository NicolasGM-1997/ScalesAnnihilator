package games.scalesannihilator.character;

import java.util.ArrayList;
import javafx.scene.Group;

public class ProjectileGroup extends Group {
   
   private ArrayList<Projectile> projectiles = new ArrayList<>();
   private ArrayList<Projectile> removedProjectiles = new ArrayList<>();
   
   public ProjectileGroup(){}
   
   public void addProjectile(Projectile prj){
      projectiles.add(prj);
      getChildren().add(prj);
   }
   
   public void removeProjectiles(){
      if(!removedProjectiles.isEmpty()){
         for(Projectile p: removedProjectiles){
            getChildren().remove(p);
         }
         removedProjectiles.clear();
      }
   }
   
   public void addRemoveProjectile(Projectile prj){
      removedProjectiles.add(prj);
   }   
}

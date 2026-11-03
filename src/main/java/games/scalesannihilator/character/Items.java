package games.scalesannihilator.character;

import games.scalesannihilator.app.UtilMath;
import games.scalesannihilator.gameplay.DisplayUI;
import games.scalesannihilator.gameplay.Level;
import javafx.animation.ScaleTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Items extends Group{
   
   public enum Type {POINTS};
   
   public static Color pointColor = Color.rgb(150,50,50,0.6);
   
   private Type type;
   private double value;
   
   public Items(Type type){
      this.type = type;
      animatedItem();
   }
   
   //// Animations
   
   public void animatedItem(){
      switch(type){
         case POINTS -> animatedPoint();
      }
   }
   
   public void animatedPoint(){
      ScaleTransition scale = new ScaleTransition(Duration.millis(250), this);
      scale.setFromX(0.9);
      scale.setFromY(0.9);
      scale.setFromZ(0.9);
      scale.setToX(1.1);
      scale.setToY(1.1);
      scale.setToZ(1.1);
      scale.setAutoReverse(true);
      scale.setCycleCount(4 * 30);
      scale.play();
      
      scale.setOnFinished(e -> {
         if(getParent() != null){
            ((ItemsGroup) getParent()).addRemoveItem(this);
         }
      });
   }
   
   //// collision
   
   public void setPlayerCollision(Player player, Level lvl){
      
      if(UtilMath.distanceOtherParent(player, this) > 10) return;
      
      Sphere ps = (Sphere) lookup("#bodyPoint");
      Bounds hb = player.getHeadCollision().localToScene(player.getHeadCollision().getBoundsInLocal());
      Bounds body = ps.localToScene(ps.getBoundsInLocal());
      
      if(hb.intersects(body)){
         player.getStats().newStomach(getValue());
         DisplayUI.addShowInfo(
            lvl.getInterface2D(),
            "add-stomach",
            getValue(),
            lvl.getInterface2D().getWidth() / 2,
            lvl.getInterface2D().getHeight() / 2
         );
         ((ItemsGroup) getParent()).addRemoveItem(this);
      }
   }
   
   //// Generate Items
   
   public static Items itemPoint(Fish f){
      Items item = new Items(Type.POINTS);
      item.setValue(f.getStats().getPrice());
      
      Point3D point = f.localToParent(Point3D.ZERO);
      
      PhongMaterial mat = new PhongMaterial();
      mat.setDiffuseColor(pointColor);
      mat.setSpecularColor(Color.GRAY);
      
      Sphere body = new Sphere(1);
      body.setMaterial(mat);
      body.setId("bodyPoint");
      
      item.getChildren().add(body);
      
      item.setTranslateX(point.getX());
      item.setTranslateY(point.getY());
      item.setTranslateZ(point.getZ());
      
      return item;
   }   
}

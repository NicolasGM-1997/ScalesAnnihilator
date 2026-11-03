
package games.scalesannihilator.gameplay;

import static games.scalesannihilator.app.App.actualLvl;
import games.scalesannihilator.app.Camera;
import games.scalesannihilator.app.ProcessGame;
import games.scalesannihilator.app.UtilMath;
import games.scalesannihilator.character.Fish;
import games.scalesannihilator.character.ItemsGroup;
import games.scalesannihilator.character.Player;
import games.scalesannihilator.character.ProjectileGroup;
import games.scalesannihilator.character.ShoalFish;
import games.scalesannihilator.enviroment.Enviroment;

import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.SubScene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Level extends StackPane{
   
   protected SubScene world;
   protected Group root3D;
   protected ShoalFish shoal;
   protected ProjectileGroup projectiles;
   protected ItemsGroup items;
   protected GaussianBlur blur;
   protected AnchorPane interface2D;
   protected Pane filterWater;
   protected PointLight light;
   protected AmbientLight ambient;
   protected Enviroment enviroment;
   protected Camera playerCamera;
   protected Player player;
   protected Rectangle transition;
   protected double mouseRotateX;
   protected double mouseRotateY;
   protected boolean underOcean = true;
   protected boolean entryScene = true;
   
   public Level(double width, double height){
   
      transition = new Rectangle(width, height);
      transition.setFill(Color.BLACK);
      transition.setOpacity(0);
      transition.setMouseTransparent(true);
      
      interface2D = new AnchorPane();
      interface2D.setPrefSize(width, height);
      interface2D.setId("#interface2D");
   }   
   
   //// loopFunctions
   
   public void initializeLoop(){
      ProcessGame.loopGame(this);
      ProcessGame.pauseGameplay = false;
   }
   
   public void returnLoop(){
      ProcessGame.loop.start();
      ProcessGame.pauseGameplay = false;
   }
   
   public void pauseLoop(){
      ProcessGame.loop.stop();
      ProcessGame.pauseGameplay = true;
   }
   
   public void detectSurface(double gravity, double delta){

      double limitSurface = -30;

      if(player != null){
         
         if(player.getTranslateY() <= limitSurface){
           
            player.setVelGravity(player.getVelGravity() + gravity * delta);
            player.moveY(player.getVelGravity() * delta);

            if(player.getTranslateY() > limitSurface){
               player.setTranslateY(-27);
               player.setVelGravity(0);
            }
            underOcean = true;  
         } else{
            underOcean = false;
         }
      }

      if(!shoal.getChildren().isEmpty()){

         for(Node n: shoal.getChildren()){
            if(n instanceof Fish f){
               if(f.getTranslateY() <= limitSurface){
                  f.setVelGravity(f.getVelGravity() + gravity * delta);
                  f.moveY(f.getVelGravity() * delta);

                   if(f.getTranslateY() > limitSurface){
                     f.setTranslateY(-27);
                     f.setVelGravity(0);
                  }
               } 
            }
         }
      }
      
      if(underOcean && enviroment != null){
         filterWater.setBackground(new Background(new BackgroundFill(
              Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY
           )));
         world.setFill(Color.web("66aaff"));
      } else {
         filterWater.setBackground(new Background(new BackgroundFill(
            Color.rgb(0,100,200,0.1), CornerRadii.EMPTY, Insets.EMPTY
         )));
         world.setFill(Color.web("5599ee"));
      } 
   }
   
   public void cameraBlur(){
      
      if(player == null) return;
      
      Point3D cam = playerCamera.localToScene(Point3D.ZERO);

      for(Node n: shoal.getChildren()){
         if(n instanceof Fish f){
            
            double dist = UtilMath.distanceNodeAndPoint(f, cam);

            if(dist > player.getCamera().getFarClip() / 3){
               f.setEffect(blur);
            }else{
               f.setEffect(null);
            }
         }
      }
   }
}

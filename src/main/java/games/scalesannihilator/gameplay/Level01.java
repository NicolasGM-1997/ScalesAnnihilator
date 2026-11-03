package games.scalesannihilator.gameplay;


import games.scalesannihilator.app.Camera;
import games.scalesannihilator.app.LoadFileGame;
import games.scalesannihilator.app.Peripheral;
import games.scalesannihilator.app.ProcessGame;
import games.scalesannihilator.character.Fish;
import games.scalesannihilator.character.ItemsGroup;
import games.scalesannihilator.character.Player;
import games.scalesannihilator.character.ProjectileGroup;
import games.scalesannihilator.character.ShoalFish;
import games.scalesannihilator.character.Statistics;
import games.scalesannihilator.enviroment.Enviroment;

import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.effect.GaussianBlur;

import static games.scalesannihilator.app.App.global;

public class Level01 extends Level{
   
   public Level01(double width, double height, Camera camera){    
      super(width, height);
      playerCamera = camera;
  
      root3D = new Group();
      player = new Player(playerCamera);
      shoal = new ShoalFish();
      projectiles = new ProjectileGroup();
      items = new ItemsGroup();
      
      light = new PointLight(Color.WHITE);
      ambient = new AmbientLight(Color.DARKBLUE);
      filterWater = new Pane();
      enviroment = new Enviroment();
      blur = new GaussianBlur(0.25);
      
      light.setTranslateY(-500);
      light.setTranslateZ(500);
      light.setTranslateX(100);
      ambient.setTranslateY(500);
      filterWater.setPrefSize(width, height);
      filterWater.setBackground(new Background(new BackgroundFill(
         Color.rgb(0,100,200,0.1), CornerRadii.EMPTY, Insets.EMPTY
      )));
      filterWater.setMouseTransparent(true);
      
      root3D.getChildren().addAll(light, ambient);
       
      world = new SubScene(root3D, width, height, true, SceneAntialiasing.DISABLED);
      world.setCamera(camera);
      world.setFill(Color.TRANSPARENT);
      
      interface2D.getChildren().addAll(transition, filterWater);
      DisplayUI.gamePlayUI(player, interface2D);
      System.out.println(System.identityHashCode(interface2D));
     
      this.getChildren().addAll(world, interface2D);
      
      initialWorld();
      redimention();
      playerCamera();
   }
   
   //// setworld
   
   public void redimention(){
      world.widthProperty().bind(global.widthProperty());
      world.heightProperty().bind(global.heightProperty());
      interface2D.setMaxHeight(world.getHeight());
      interface2D.setMaxWidth(world.getWidth());
   }

   public void addObjectWorld(Group obj){
      root3D.getChildren().add(obj);
   }

   public void initialWorld(){
      addWorldFishes(player);
      addObjectWorld(shoal);
      addObjectWorld(projectiles);
      addObjectWorld(items);
      addObjectWorld(player);
      addObjectWorld(enviroment);
   }
   
   //// Keyboard entry
   
   public void playerCamera(){      
      
      if(!ProcessGame.pauseGameplay){
         global.setOnKeyPressed(event -> {
            Peripheral.getKeysPressed().add(event.getCode());
         });

         global.setOnKeyReleased(event -> {
            Peripheral.getKeysPressed().remove(event.getCode());
         });

         global.setOnMousePressed(event -> {
            mouseRotateX = event.getSceneX();
            mouseRotateY = event.getSceneY();
            player.setAngX(player.getRotateX().getAngle());
            player.setAngY(player.getRotateY().getAngle());
         });

         global.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - mouseRotateX;
            double deltaY = event.getSceneY() - mouseRotateY;

            mouseRotateX = event.getSceneX();
            mouseRotateY = event.getSceneY();

            if(player != null){
               player.getCamera().orbitCamera(deltaX, deltaY);
            }
         });

         global.setOnMouseReleased(event -> {
            if(player != null){
               player.getCamera().recalibrate();
            }
         }); 
      }
   }   
   
   public void addWorldFishes(Player player){
      
      try{
         LoadFileGame.resetStats();
         LoadFileGame.loadStats("/data/spawns.csv", 1);
      } catch(Exception ex){
         ex.printStackTrace();
      }
      
      int index = 0;
      
      for(Statistics s: LoadFileGame.stats){
         
         double originalX = s.getSpawnX();
         double originalY = s.getSpawnY();
         double originalZ = s.getSpawnZ();
         
         Point3D zone = new Point3D(originalX, originalY, originalZ);
         
         s.setZone(zone);
         shoal.getZones().add(zone);
         
         for(int i = 0; i < LoadFileGame.groups.get(index); i++){
                        
            double difX = (Math.random() * 64) - 32;
            double difY = (Math.random() * 64) - 32;
            double difZ = (Math.random() * 64) - 32;
            
            s.setSpawnX(s.getSpawnX() + difX);
            s.setSpawnY(s.getSpawnY() + difY);
            s.setSpawnZ(s.getSpawnZ() + difZ);

            addIndividualFish(player, s);
            
            s.setSpawnX(originalX);
            s.setSpawnY(originalY);
            s.setSpawnZ(originalZ);
         }
         index++;
      }
      
      shoal.createZones();
   }
   
   public void addIndividualFish(Player ply, Statistics stats){
      Fish fish = new Fish(ply, stats);
      shoal.addNewFish(fish);
   }
}
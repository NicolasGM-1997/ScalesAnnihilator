package games.scalesannihilator.gameplay;

import games.scalesannihilator.app.ProcessGame;
import games.scalesannihilator.character.Player;

import javafx.util.Duration;
import java.util.ArrayList;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class DisplayUI {
   
   private static Runtime rt = Runtime.getRuntime();
   
   //// Main Menu UI
   
   public static void mainMenuUI(AnchorPane parent){
      ArrayList<Region> controls = new ArrayList<>();
      
      AnchorPane img = new AnchorPane();
      img.setEffect(new GaussianBlur(16));
          
      Button btnPlay = new Button("PLAY");
      btnPlay.setFocusTraversable(false);
      btnPlay.setPrefSize(128, 48);
      btnPlay.setId("btnPlay");
      
      Button btnStat = new Button("STAT");
      Button btnSave = new Button("SAVE");
      Button btnLoad = new Button("LOAD");
      
      Button btnExit = new Button("EXIT");
      btnExit.setFocusTraversable(false);
      btnExit.setPrefSize(96, 36);
      btnExit.setId("btnExit");
      
      setClassStyle(img, "img-back");
      setClassStyle(btnPlay, "btn-play");
      setClassStyle(btnExit, "btn-exit");
      
      controls.add(img);
      controls.add(btnPlay);
      controls.add(btnExit);
      
      for(Region c: controls){
         parent.getChildren().add(c);
      }
      
      Platform.runLater(() -> {
         img.setPrefSize(parent.getWidth(), parent.getHeight());
         
         setPositionT1(img, "", 0, 0);
         setPositionT1(btnPlay, "btnPlay", 
            (parent.getHeight()/2) - (btnPlay.getHeight()/2), (parent.getWidth()/2) - (btnPlay.getWidth()/2));
         setPositionT1(btnExit, "btnExit",
            (parent.getHeight()/2) - (btnExit.getHeight()/2) + 60, (parent.getWidth()/2) - (btnExit.getWidth()/2));
      });
   }
   
   //// GamePlayUI
   
   public static void gamePlayUI(Player player, AnchorPane parent){
      ArrayList<Region> controls = new ArrayList<>();
      
      Label infoFps = new Label("");
      Label infoRam = new Label("");
      Label infoMs = new Label("");
      Label infoPly = new Label(player.position());
      Label infoCam = new Label(player.getCamera().position());  
      Label deepPly = new Label(playerDeep(player));
      Label infoLiv = new Label("\u2795");
      Label infoFry = new Label("\ud83d\udd25");
      Label infoAtk = new Label("Atk: " + player.getStats().getAttack());
      Label infoDef = new Label("Def: " + player.getStats().getDefense());
      Label infoStm = new Label("Stm: " + player.getStats().getStomach() + "/" + player.getStats().getMaxStomach());
              
      Region rectLife = new Region();
      rectLife.setPrefSize(100, 15);
      
      Region rectFury = new Region();
      rectFury.setPrefSize(100,15);
      
      Region life = new Region();
      life.setPrefSize(96, 11);
      life.setMaxWidth(96);
      
      Region fury = new Region();
      fury.setPrefSize(96, 11);
      fury.setMaxWidth(96);
      
      setClassStyle(infoFps, "info-player");
      setClassStyle(infoMs, "info-player");
      setClassStyle(infoRam, "info-player");
      setClassStyle(infoPly, "info-player");
      setClassStyle(infoCam, "info-player");
      setClassStyle(deepPly, "deep-info");
      setClassStyle(infoLiv, "info-life");
      setClassStyle(infoFry, "info-fury");
      setClassStyle(infoAtk, "info-stats");
      setClassStyle(infoDef, "info-stats");
      setClassStyle(infoStm, "info-stats");
      
      setClassStyle(rectLife, "rect-stats");
      setClassStyle(rectFury, "rect-stats");
      setClassStyle(life, "bar-life");
      setClassStyle(fury, "bar-fury");
      
      controls.add(infoFps);
      controls.add(infoMs);
      controls.add(infoRam);
      controls.add(infoPly);
      controls.add(infoCam);
      controls.add(deepPly);
      controls.add(infoLiv);
      controls.add(infoFry);
      controls.add(infoAtk);
      controls.add(infoDef);
      controls.add(infoStm);
      
      controls.add(rectLife);
      controls.add(rectFury);
      controls.add(life);
      controls.add(fury);
      
      for (Region c: controls) {
         parent.getChildren().add(c);
      }
      
      Platform.runLater(() -> {
         setPositionT2(infoFps, "FPS", 10, 10);
         setPositionT2(infoMs, "MS", 30, 10);
         setPositionT2(infoRam, "RAM", 50, 10);
         setPositionT2(infoPly, "Player", 70, 10);
         setPositionT2(infoCam, "Camera", 90, 10);
         setPositionW1(deepPly, parent, "Deep", 100, 10);
         setPositionT1(infoLiv, "Heart", 10, 10);
         setPositionT1(infoFry, "Fire", 34, 10);
         setPositionT1(infoAtk, "Attack", 55, 10);
         setPositionT1(infoDef, "Defense", 75, 10);
         setPositionT1(infoStm, "Stomach", 95, 10);
         
         setPositionT1(rectLife, "RLife", 10, 30);
         setPositionT1(rectFury, "RFury", 35, 30);
         setPositionT1(life, "Life", 12, 32);
         setPositionT1(fury, "Fury", 37, 32);
         
         improvePlayer(parent);
      });      
   }
   
   //// Pane actualize
   
   public static void improvePlayer(AnchorPane parent){
      
      double width = parent.getWidth() - 256;
      double height = parent.getHeight() - 256;
      
      ScrollPane scroll = new ScrollPane();
      scroll.setPrefSize(width, height);
      scroll.setVisible(false);
      
      AnchorPane pane = new AnchorPane();
      pane.setPrefSize(width - 24, height * 2);
      
      Button btnClose = new Button("X");
      btnClose.setPrefSize(24,24);
      btnClose.setAlignment(Pos.CENTER);
      
      setClassStyle(scroll, "scroll-improve");
      setClassStyle(pane, "improve-player");
      setClassStyle(btnClose, "btn-close-improve");
      
      pane.getChildren().add(btnClose);
      scroll.setContent(pane);
      
      createItem(pane, "SAVE", 0);
      
      parent.getChildren().add(scroll);
      
      setPositionT1(scroll, "scrollImprove", 128, 128);
      setPositionT1(pane, "improvePlayer", 0, 0);
      setPositionT2(btnClose, "btnCloseImprove", 10, 4);
   }
   
   //// incremental stats
   
   public static void createItem(AnchorPane parent, String item, int gap){
      
      double saves = 0;
      double initialY = 16;
      double nameX = 16;
      double actualX = 128;
      double incrementX = 256;
      Label nameItem = new Label("");
      Label actualStat = new Label("");
      Button increment = new Button("");
      
      setPositionT1(nameItem, item + "Name", initialY + gap, nameX);
      setPositionT1(actualStat, item + "Stat", initialY + gap, actualX);
      setPositionT1(increment, item + "Button", initialY + gap, incrementX);
      
      AnchorPane pane = new AnchorPane();
      
      switch(item){
         case "SAVE" -> {
            nameItem.setText("SAVE STATUS");
            actualStat.setText("save # " + saves);
            increment.setText("SAVE");
         }  
      }
      
      pane.getChildren().add(nameItem);
      pane.getChildren().add(actualStat);
      pane.getChildren().add(increment);
      parent.getChildren().add(pane);
   }
   
   //// actualize gameplay UI
   
   public static void actualizeGUI(Player player, AnchorPane pane){

      if(player != null){
         for(var node : pane.getChildren()){
            if(node.getId() != null){
               if(node instanceof Label l){
                  switch(node.getId()){
                     case "Player" -> l.setText(player.position());
                     case "Camera" -> l.setText(player.getCamera().position());
                     case "Deep" -> l.setText(playerDeep(player));
                     case "Stomach" -> {
                        player.getStats().absoluteStomach();
                        l.setText("Stm: " + player.getStats().getStomach() + "/" + player.getStats().getMaxStomach());
                     }
                  }
               }
               
               if(node instanceof Region r){
                  switch(node.getId()){
                     case "Life" -> {
                        player.getStats().absoluteLife();
                        r.setPrefWidth(player.getStats().percentLife(r.getMaxWidth()));
                     }
                     case "Fury" -> {
                        player.getStats().absoluteFury();
                        r.setPrefWidth(player.getStats().percentFury(r.getMaxWidth()));
                     }
                  }
               }
            }
         } 
      }         
   }
   
   //// Actualize gameplay FPS, RAM, MS
   
   public static void actualizeFPS(AnchorPane pane){

      for(var node : pane.getChildren()){
         if(node.getId() != null){
            if(node instanceof Label l){
               switch(node.getId()){
                  case "FPS" -> l.setText(String.format("%.0f fps", 1 / ProcessGame.FFPS));
                  case "MS" -> l.setText(String.format("%.2f ms", ProcessGame.MS));
                  case "RAM" -> l.setText(String.format("%.2f MB",((rt.totalMemory() - rt.freeMemory()) / Math.pow(2,20))));
               }
            }
         }
      } 
   }
   
   
   //// Actualizd damage
   
   public static void addShowInfo(AnchorPane parent, String style, double damage, double X, double Y){
      Label infoDamage = new Label(String.format("%.1f", damage));
      
      TranslateTransition moveUp = new TranslateTransition(Duration.seconds(1), infoDamage);
      moveUp.setByY(-32);
      
      FadeTransition fade = new FadeTransition(Duration.seconds(2), infoDamage);
      fade.setFromValue(1);
      fade.setToValue(0);
      fade.setCycleCount(1);
      
      ParallelTransition combo = new ParallelTransition(infoDamage, fade, moveUp);
      combo.play();
      
      infoDamage.setPrefSize(96,48);
      infoDamage.setEffect(new DropShadow(5, Color.BLACK));
      infoDamage.setAlignment(Pos.CENTER);
      
      int difX = (int) (Math.random() * 65) - 32;
      int difY = (int) (Math.random() * 65) - 32;
      
      double posX = (X + difX) - infoDamage.getWidth() / 2;
      double posY = (Y + difY) - infoDamage.getHeight() / 2;
      
      setPositionT1(infoDamage, "x", posY, posX);
      setClassStyle(infoDamage, style);
      
      parent.getChildren().add(infoDamage);
      combo.setOnFinished(e -> parent.getChildren().remove(infoDamage));
   }
   
   
   //// establecer pocisiones regiones
   
   public static void setPositionT1(Region region, String id, double top, double left){
      region.setId(id);
      AnchorPane.setLeftAnchor(region, left);
      AnchorPane.setTopAnchor(region, top);
   }
   
   public static void setPositionT2(Region region, String id, double top, double right){
      region.setId(id);
      AnchorPane.setRightAnchor(region, right);
      AnchorPane.setTopAnchor(region, top);
   }
   
   public static void setPositionW1(Region region, AnchorPane parent, String id, double width, double top){
      region.setId(id);
      region.setPrefWidth(width);
      AnchorPane.setLeftAnchor(region, (parent.getWidth() / 2) - (width / 2));
      AnchorPane.setTopAnchor(region, top);
   }
   
   
   //// Modify Styles
   
   public static void setStyle(Region region, String... properties){
      StringBuilder sb = new StringBuilder();
      
      for (String style : properties) {
         String clean = style.trim();
         if(!clean.isEmpty()){
            sb.append("-fx-");
            sb.append(clean);
            if(!clean.endsWith(";")){
               sb.append(";");
            }
            sb.append(" ");
         }
      }
      region.setStyle(sb.toString().trim()); 
   }
   
   public static void setClassStyle(Region region, String clazz){
      region.getStyleClass().add(clazz);
   }
   
   public static void setClassesStyle(Region region, String... classes){
      for(String clazz: classes){
         region.getStyleClass().add(clazz);
      }
   }
   
   public static String playerDeep(Player player){
      return "\u2B07 " + (int) (Math.floor(player.getTranslateY() / 7.5) + 4) + " m";
   }
}

package games.scalesannihilator.gameplay;

import static games.scalesannihilator.app.App.global;
import static games.scalesannihilator.app.App.actualLvl;
import static games.scalesannihilator.app.App.pauseLvl;
import games.scalesannihilator.app.Camera;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainMenu extends Level{
   
   private Button btnPlay;
   private Button btnExit;
   
   public MainMenu(double width, double height){
      super(width, height);
      
      DisplayUI.mainMenuUI(interface2D);      
      getChildren().add(interface2D);
      
      interface2D.getChildren().add(transition);
      
      setBtnPlay(width, height);
      setBtnExit();
   }
   
   public void setBtnPlay(double width, double height){
      btnPlay = (Button) interface2D.lookup("#btnPlay");
      btnPlay.setOnAction(e -> {
         
         btnPlay.setDisable(true);
         
         FadeTransition fadeOut = new FadeTransition(Duration.millis(250), transition);
         fadeOut.setFromValue(0);
         fadeOut.setToValue(1);
         
         fadeOut.setOnFinished(f -> {

            PauseTransition wait = new PauseTransition(Duration.millis(250));
            wait.setOnFinished(w -> {
               
               entryScene = false;
               
               if(pauseLvl == null){
                  Level01 lvl = new Level01(width, height, new Camera(true, 0.5, 1500));
                  actualLvl = lvl;
                  global.setRoot(actualLvl);
                  actualLvl.initializeLoop();
               } else {
                  actualLvl = pauseLvl;
                  global.setRoot(pauseLvl);
                  actualLvl.returnLoop();
               }
               
               if(actualLvl.isEntryScene()){
                  actualLvl.getTransition().setOpacity(1);
               }
               
               FadeTransition fadeIn = new FadeTransition(Duration.millis(2500), actualLvl.getTransition());
               fadeIn.setFromValue(1);
               fadeIn.setToValue(0);
               fadeIn.play();
                
            });
            wait.play();
         });
         
         fadeOut.play();  
      });
   }
   
   public void setBtnExit(){
      btnExit = (Button) interface2D.lookup("#btnExit");
      btnExit.setOnAction(e -> {
         System.exit(0);
      });
   }
   
   public void gameOverPlayer(){

      btnPlay.setDisable(false);
      actualLvl.pauseLoop();
      
      global.setRoot(this);
      actualLvl = this;
      pauseLvl = null;
      
      VBox popup = new VBox();
      popup.setPrefSize(256, 64);
      DisplayUI.setClassStyle(popup, "game-over");

      Label text = new Label("Game Over");
      text.setAlignment(Pos.CENTER);
      text.setMaxWidth(256);
      popup.getChildren().add(text);

      interface2D.getChildren().add(popup);

      Platform.runLater(() -> {
         DisplayUI.setPositionT1(popup, "popup-GO", 
            (interface2D.getHeight() / 2) - (popup.getHeight() / 2), (interface2D.getWidth() / 2) - (popup.getWidth() / 2));
      });      

      PauseTransition wait = new PauseTransition(Duration.seconds(2));
      wait.setOnFinished(w -> {
         interface2D.getChildren().remove(popup);
      });

      wait.play();
      returnMenu(5);
   }
   
   public void returnMenu(int seconds){
      
      if(!entryScene){
         FadeTransition fadeIn = new FadeTransition(Duration.seconds(seconds), transition);
         fadeIn.setFromValue(1);
         fadeIn.setToValue(0);
         fadeIn.play();
         entryScene = true;
      } 
   }
   
   public void gameReturnPlayer(){
      btnPlay.setDisable(false);
      
      actualLvl.pauseLoop();
      pauseLvl = actualLvl;
      global.setRoot(this);
      actualLvl = this;
      
      returnMenu(3);
   }
   
}

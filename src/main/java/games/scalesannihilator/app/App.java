package games.scalesannihilator.app;

import games.scalesannihilator.gameplay.Level;
import games.scalesannihilator.gameplay.MainMenu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application{
   
   private int APPWIDTH = 832;
   private int APPHEIGHT = 624;
   
   public static Scene global;
   public static Stage mainStage;
   public static MainMenu mainMenu;
   public static Level actualLvl;
   public static Level pauseLvl;
   
   @Override
   public void start(Stage stage) throws Exception {
      
      mainMenu = new MainMenu(APPWIDTH, APPHEIGHT);
      
      mainStage = stage;
      actualLvl = mainMenu;
      pauseLvl = null;
      global = new Scene(mainMenu, APPWIDTH, APPHEIGHT);
      global.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
      
      stage.setScene(global);
      stage.setTitle("Scales Annihilator");
      stage.show();
      
      stage.setResizable(false);
      stage.setOnCloseRequest(event -> {
         System.exit(0);
      });      
   }
}

package games.scalesannihilator.app;

import static games.scalesannihilator.app.App.actualLvl;
import static games.scalesannihilator.app.App.mainMenu;
import games.scalesannihilator.character.Fish;
import games.scalesannihilator.character.Items;
import games.scalesannihilator.character.Items.Type;
import games.scalesannihilator.character.Projectile;
import games.scalesannihilator.character.Projectile.Emisor;
import games.scalesannihilator.gameplay.DisplayUI;
import games.scalesannihilator.gameplay.Level;
import games.scalesannihilator.gameplay.MainMenu;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
import javafx.scene.Node;

public class ProcessGame {
   
   public static long lastTime = 0;
   public static double delta = 0;   
   public static double gravity = 200;
   public static double TFPS = 60;
   public static double FFPS = 1 / TFPS;
   public static double MS = 0;
   public static double startMS = 0;
   public static double endMS = 0;
   public static double accumulator = 0;
   public static double accumulatorGUI = 0;
   public static double accumulatorFPS = 0;
   public static double intervalGUI = 1 / 5f;
   public static double intervalFPS = 1 / 1.25f;
   public static double elapsedTime = 0;
   public static AnimationTimer loop;
   public static boolean pauseGameplay = false;
   
   public static void loopGame(Level lvl){
            
      loop = new AnimationTimer(){
         @Override public void handle(long now){
            
            if(actualLvl instanceof MainMenu) return;
            
            if(lastTime == 0){
               lastTime = now;
               return;
            }
            
            delta = (now - lastTime) / 1e9;            
            lastTime = now;
            accumulator += delta;
            accumulatorGUI += delta;
            accumulatorFPS += delta;
            elapsedTime += delta;           
            
            while(accumulator >= FFPS){
               
               startMS = System.nanoTime();
               Peripheral.setRunningGame();
              
               if(!pauseGameplay){                
                  
                  actualLvl.getPlayer().setDeltaAttack(actualLvl.getPlayer().getDeltaAttack() + FFPS);
                  Peripheral.movePlayer(actualLvl.getPlayer(), actualLvl.getEnviroment(), FFPS);
                  actualLvl.getPlayer().damageFishes(actualLvl);
                                    
                  for(Node n: actualLvl.getShoal().getChildren()){
                     if(n instanceof Fish f){
                        f.setDeltaAttack(f.getDeltaAttack() + FFPS);
                        f.moveFish(actualLvl, FFPS);
                        f.actualizeStatus();
 
                        if(f.getStats().getLife() <= 0){
                           actualLvl.getShoal().addRemoveFish(f);
                           actualLvl.getItems().addItem(Items.itemPoint(f));
                        }
                     } 
                  }                  
                  
                  for (Node node: actualLvl.getProjectiles().getChildren()) {
                     if(node instanceof Projectile p){
                        
                        p.updateMove(delta);
                        
                        if(p.getEmisor() == Emisor.FISH){
                           p.checkCollision(actualLvl.getPlayer());
                        } else if(p.getEmisor() == Emisor.PLAYER){
                           for(Node n: actualLvl.getShoal().getChildren()){
                              if(n instanceof Fish f){
                                 p.checkCollision(f);
                              }
                           }
                        }
                     }
                  }
                  
                  for(Node node: actualLvl.getItems().getChildren()){
                     if(node instanceof Items i){
                        if(i.getType() == Type.POINTS){
                           i.setPlayerCollision(actualLvl.getPlayer(), actualLvl);
                        }
                     }
                  }
                  
                  actualLvl.getShoal().actualize(actualLvl.getPlayer(), FFPS);
                  actualLvl.getProjectiles().removeProjectiles();
                  actualLvl.getItems().removeItems();
                  actualLvl.detectSurface(gravity, FFPS);
                  actualLvl.cameraBlur();
                  actualLvl.getEnviroment().actualOcean().actualizeWaves(elapsedTime);
                  
                  if(elapsedTime > 1e9){
                     elapsedTime = 0;
                  }
                                 
                  actualLvl.getPlayer().regenerate(FFPS);
                  
                  if(actualLvl.getPlayer().getStats().getLife() <= 0){
                     mainMenu.gameOverPlayer();
                  } 
               }
               accumulator -= FFPS;
               endMS = System.nanoTime();
               MS = (endMS - startMS) / 1e6;
            }
            
            if(accumulatorGUI >= intervalGUI){
               DisplayUI.actualizeGUI(actualLvl.getPlayer(), actualLvl.getInterface2D());
               accumulatorGUI = 0;
            }
            
            if(accumulatorFPS >= intervalFPS){
               DisplayUI.actualizeFPS(actualLvl.getInterface2D());
               accumulatorFPS = 0;
            }
         }         
      };
      loop.start();
   }
}

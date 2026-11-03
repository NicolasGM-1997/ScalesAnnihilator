package games.scalesannihilator.character;

import games.scalesannihilator.app.UtilMath;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Arc;
import javafx.scene.transform.Rotate;

public class ShoalFish extends Group{
   
   private ArrayList<Fish> removedFishes = new ArrayList<>();
   private ArrayList<Point3D> zones = new ArrayList<>();
   private ArrayList<Queue<RespawnTime>> respawnsZones = new ArrayList<>();
   private ArrayList<RespawnTime> eliminated = new ArrayList<>();
   private ArrayList<Arc> arcs = new ArrayList<>();
   
   public ShoalFish(){}
   
   //// Getters
   
   public ArrayList getZones(){
      return zones;
   }
   
   //// functions
   
   public void addNewFish(Fish f){
      getChildren().add(f);
   }
   
   public void addRemoveFish(Fish f){
      removedFishes.add(f);
   }
   
   public void removeFishes(){
      for(Fish f: removedFishes){
         respawnFish(f);
         getChildren().remove(f);
      }
      removedFishes.clear();
   }
   
   public void removeSpawns(){
      if(!eliminated.isEmpty()){
         for (RespawnTime rt: eliminated) {
            rt.getArc().setLength(0);
            respawnsZones.get(rt.getIndex()).poll();
         }
         eliminated.clear();
      }
   }
   
   public void respawnFish(Fish f){
   
      double size = 0;
      double type = 0;
      
      switch(f.getStats().getSize()){
         case SMALL -> size = 1;
         case BASIC -> size = 1.25;
         case BIG -> size = 1.75;
         case BOSS -> size = 2.5;
      }
      
      switch(f.getStats().getType()){
         case NORMAL -> type = 1.5;
         case BLOOD -> type = 2.5;
         case REGENERATE -> type = 3;
         case SPEED -> type = 1;
         case TANK -> type = 3.5;
      }
      
      double respawn = 3 + (size * type);
      Point3D point = nearestPoint(f.localToScene(Point3D.ZERO));
      int indexHook = hookPoint(point);
      
      RespawnTime rt = new RespawnTime();
      rt.setStats(f.getStats());
      rt.setIndex(indexHook);
      rt.setPoint(point);
      rt.setRespawnTime(respawn);
      rt.setCurrentTime(0);
      rt.setArc(arcs.get(indexHook));
      rt.getArc().setVisible(true);
      
      respawnsZones.get(indexHook).offer(rt);

   }
   
   public void createZones(){
      for(Point3D p: zones){
         
         respawnsZones.add(new LinkedList<>());
         
         Arc arc = new Arc();
         arc.getStyleClass().add("spawns");
         arc.setRadiusX(6);
         arc.setRadiusY(6);
         
         arc.setTranslateX(p.getX());
         arc.setTranslateY(p.getY());
         arc.setTranslateZ(p.getZ());
         
         arcs.add(arc);
         getChildren().add(arc);
      }
   }
   
   public void actualizeZones(Player player, double delta){
      
      for(Queue<RespawnTime> q: respawnsZones){
         if(q.isEmpty()) continue;
      
         RespawnTime rt = q.peek();

         rt.setCurrentTime(rt.getCurrentTime() + delta);
         double percent = rt.getCurrentTime() / rt.getRespawnTime();

         rt.getArc().setLength(-360 * percent);

         double dist = UtilMath.distanceOtherParent(rt.getArc(), player.getCamera());
         UtilMath.yawAndPitch();
         
         rt.getArc().setRotationAxis(Rotate.Y_AXIS);
         rt.getArc().setRotate(UtilMath.yaw);

         rt.getArc().setVisible(dist <= 500);

         if(rt.getCurrentTime() >= rt.getRespawnTime()){
            eliminated.add(rt);

            Fish newFish = new Fish(player, rt.getStats());
            newFish.getStats().setLife(newFish.getStats().getMaxLife());
            getChildren().add(newFish);
         }  
      }      
   }
   
   public Point3D nearestPoint(Point3D fp){
      Point3D near = new Point3D(0,0,0);
      double minDist = Double.MAX_VALUE;
      
      for(Point3D p : zones){
         double dist = UtilMath.distancePoint(p, fp);
         if(dist < minDist){
            minDist = dist;
            near = p;
         }
      }
      
      return near;
   }
   
   public int hookPoint(Point3D np){
      for (int i = 0; i < zones.size(); i++) {
         Point3D p = zones.get(i);
         if(p.equals(np)) return i;
      }
      return -1;
   }
   
   public void actualize(Player player, double delta){     
      actualizeZones(player, delta);
      removeFishes();
      removeSpawns();
   }
}

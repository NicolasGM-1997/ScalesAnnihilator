package games.scalesannihilator.enviroment;

import games.scalesannihilator.character.Player;
import static games.scalesannihilator.enviroment.Terrain.loadVertices;

import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;

public class Enviroment extends Group{
   
   private OceanCeiling ocean;
   private Box northBox;
   private Box eastBox;
   private Box southBox;
   private Box westBox;
   private double maxX;
   private double minX;
   private double maxZ;
   private double minZ;
   
   public Enviroment(){
      ocean = new OceanCeiling();
      ocean.setId("Ocean");
      ocean.setTranslateY(-30);
      ocean.setCullFace(CullFace.NONE);
      ocean.setBlendMode(BlendMode.MULTIPLY);
      
      MeshView level1 = Terrain.loadTerrain("/models/terreno1.obj");
      level1.setId("Terrain");
      level1.setTranslateY(500);
      level1.setScaleX(20);
      level1.setScaleY(-20);
      level1.setScaleZ(20);
      Terrain.loadVertices(level1);
      
      PhongMaterial terrainMat = new PhongMaterial();
      terrainMat.setDiffuseColor(Color.LIGHTGREEN);
      level1.setMaterial(terrainMat);
      
      maxX = 1000;
      maxZ = 1000;
      minX = -1000;
      minZ = -1000;
      
      PhongMaterial boxMat = new PhongMaterial();
      boxMat.setDiffuseColor(Color.rgb(255,50,50,0.4));
      
      northBox = new Box(2000, 700, 10);
      northBox.setTranslateZ(1005);
      northBox.setTranslateY(300);
      northBox.setMaterial(boxMat);
      northBox.setVisible(false);
      
      southBox = new Box(2000, 700, 10);
      southBox.setTranslateZ(-1005);
      southBox.setTranslateY(300);
      southBox.setMaterial(boxMat);
      southBox.setVisible(false);
      
      eastBox = new Box(10, 700, 2000);
      eastBox.setTranslateX(1005);
      eastBox.setTranslateY(300);
      eastBox.setMaterial(boxMat);
      eastBox.setVisible(false);
      
      westBox = new Box(10, 700, 2000);
      westBox.setTranslateX(-1005);
      westBox.setTranslateY(300);
      westBox.setMaterial(boxMat);
      westBox.setVisible(false);
      
      getChildren().addAll(level1, northBox, southBox, eastBox, westBox, ocean);
   }
   
   //// Getters

   public double getMaxX() {
      return maxX;
   }

   public double getMinX() {
      return minX;
   }

   public double getMaxZ() {
      return maxZ;
   }

   public double getMinZ() {
      return minZ;
   }
   
   //// Setters

   public void setMaxX(double maxX) {
      this.maxX = maxX;
   }

   public void setMinX(double minX) {
      this.minX = minX;
   }

   public void setMaxZ(double maxZ) {
      this.maxZ = maxZ;
   }

   public void setMinZ(double minZ) {
      this.minZ = minZ;
   }
   
   //// More Functions
   
   public void seeLimits(Player player){
      northBox.setVisible((player.getTranslateZ() >= maxZ - 100));
      southBox.setVisible((player.getTranslateZ() <= minZ + 100));
      eastBox.setVisible((player.getTranslateX() >= maxX - 100));
      westBox.setVisible((player.getTranslateX() <= minX + 100));
   }
   
   public MeshView actualTerrain(){
      return (MeshView) lookup("#Terrain");
   }
   
   public OceanCeiling actualOcean(){
      return (OceanCeiling) lookup("#Ocean");
   }
   
}

package games.scalesannihilator.enviroment;

import java.net.URL;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import org.fxyz3d.importers.obj.ObjImporter;
import org.fxyz3d.importers.Model3D;

public class Terrain extends Group{
   
   public static float[] actualVertices;
   public static double worldX[];
   public static double worldY[];
   public static double worldZ[];
   
   public static MeshView loadTerrain(String path){
      
      ObjImporter importer = new ObjImporter();
      Model3D model = null;
      
      try{
         URL loadModel = Terrain.class.getResource(path);
         model = importer.load(loadModel);
      } catch(Exception ex){
         ex.printStackTrace();
      }

      MeshView terrain = (MeshView) model.getRoot().getChildren().get(0);      
      return terrain;
   }
   
   public static void loadVertices(MeshView terrain){
      
      actualVertices = ((TriangleMesh) terrain.getMesh()).getPoints().toArray(null);
      
      worldX = new double[actualVertices.length / 3];
      worldY = new double[actualVertices.length / 3];
      worldZ = new double[actualVertices.length / 3];
      
      int index = 0;
      
      for (int i = 0; i < actualVertices.length; i+=3) {
         Point3D p = terrain.localToParent(actualVertices[i], actualVertices[i+1], actualVertices[i+2]);
         worldX[index] = p.getX();
         worldY[index] = p.getY();
         worldZ[index] = p.getZ();         
         index++;
      }
   }
   
   public static double getHeightCoords(MeshView terrain, double x, double z){
      
      if(!(terrain.getMesh() instanceof TriangleMesh)){
         return 0.0;
      }
      
      double heightNear = 0.0;
      double distanceMin = Double.MAX_VALUE;
      
      for (int i = 0; i < worldX.length; i++) {
         
         double dx = x - worldX[i];
         double dz = z - worldZ[i];
         double distanceSq = (dx*dx) + (dz*dz);
         
         if(distanceSq < distanceMin){
            distanceMin = distanceSq;
            heightNear = worldY[i];
         }
      }
      
      return heightNear - 10;
   }
   
}

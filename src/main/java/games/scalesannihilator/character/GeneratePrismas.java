package games.scalesannihilator.character;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class GeneratePrismas {

   public static MeshView createTriangle(float width, float height, float depth){
      TriangleMesh mesh = new TriangleMesh();
      
      float w2 = width/2;
      float h2 = height/2;
      float d2 = depth/2;
      
      mesh.getPoints().addAll(
         0, -h2, d2,
         -w2, h2, d2,
         w2, h2, d2,
         0, -h2, -d2,
         -w2, h2, -d2,
         w2, h2, -d2
      );
      
      mesh.getTexCoords().addAll(0,0);
      mesh.getFaces().addAll(
         0,0,2,0,1,0,
         3,0,4,0,5,0,
         0,0,1,0,4,0,  0,0,4,0,3,0,
         0,0,3,0,5,0,  0,0,5,0,2,0,
         1,0,2,0,5,0,  1,0,5,0,4,0
      );
      
      return new MeshView(mesh);
   }
   
   public static MeshView createCone(float radius, float height, int segments){
      TriangleMesh mesh = new TriangleMesh();
      float h2 = height/2;
      
      mesh.getPoints().addAll(0, -h2, 0);
      mesh.getPoints().addAll(0, h2, 0);
      
      for (int i = 0; i < segments; i++) {
         double angle = 2 * Math.PI * i / segments;
         float x = (float) (radius * Math.cos(angle));
         float z = (float) (radius * Math.sin(angle));
         mesh.getPoints().addAll(x, h2, z);
      }
      
      mesh.getTexCoords().addAll(0,0);
      
      for (int i = 0; i < segments; i++) {
         int currentBasept = 2 + i;
         int nextBasept = 2 + ((i + 1) % segments);
         
         mesh.getFaces().addAll(0,0,currentBasept,0,nextBasept,0);
         mesh.getFaces().addAll(1,0,nextBasept,0,currentBasept,0);
         
      }
      
      return new MeshView(mesh);
   }
   
}

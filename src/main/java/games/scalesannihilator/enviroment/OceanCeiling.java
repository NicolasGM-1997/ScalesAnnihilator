package games.scalesannihilator.enviroment;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class OceanCeiling extends MeshView {

   private TriangleMesh mesh;
   private int divitionsX = 45;
   private int divitionsZ = 45;
   private float width = 5000;
   private float large = 5000;
   private float[] vertices;

   public OceanCeiling() {
      mesh = new TriangleMesh();

      int numVertices = (divitionsX + 1) * (divitionsZ + 1);
      float[] points = new float[numVertices * 3];
      int index = 0;
      for (int z = 0; z <= divitionsZ; z++) {
         for (int x = 0; x <= divitionsX; x++) {
            points[index++] = (x * width / divitionsX) - (width / 2);
            points[index++] = 0; 
            points[index++] = (z * large / divitionsZ) - (large / 2);
         }
      }
      
      vertices = points.clone();
      
      float[] texCoords = {0,0,1,0,0,1,1,1};
      int numFaces = divitionsX * divitionsZ * 2;
      int[] faces = new int[numFaces * 6];
      int fIndex = 0;
      for (int z = 0; z < divitionsZ; z++) {
         for (int x = 0; x < divitionsX; x++) {
            int p0 = z * (divitionsX + 1) + x;
            int p1 = p0 + 1;
            int p2 = p0 + (divitionsX + 1);
            int p3 = p2 + 1;

            // Triángulo 1 (Puntos y mapeo de textura)
            faces[fIndex++] = p0; faces[fIndex++] = 0;
            faces[fIndex++] = p2; faces[fIndex++] = 2;
            faces[fIndex++] = p1; faces[fIndex++] = 1;

            // Triángulo 2
            faces[fIndex++] = p1; faces[fIndex++] = 1;
            faces[fIndex++] = p2; faces[fIndex++] = 2;
            faces[fIndex++] = p3; faces[fIndex++] = 3;
         }
      }
      
      mesh.getPoints().setAll(points);
      mesh.getTexCoords().setAll(texCoords);
      mesh.getFaces().setAll(faces);
      mesh.getVertexFormat().getNormalIndexOffset();
      
      float[] normalInitial = new float[(divitionsX + 1) * (divitionsZ + 1) * 3];
      mesh.getNormals().setAll(normalInitial);
      
      this.setMesh(mesh);
      
      PhongMaterial mat = new PhongMaterial();
      mat.setDiffuseColor(Color.rgb(255,255,255,0.1));
      mat.setSpecularColor(Color.rgb(100,100,255));
      mat.setSpecularPower(128);
      this.setMaterial(mat);
   }
   
   public void actualizeWaves(double delta){
      float[] actualPoints = mesh.getPoints().toArray(null);
      int index = 0;
      
      for (int z = 0; z <= divitionsZ; z++) {
         for (int x = 0; x <= divitionsX; x++) {
            int yIndex = index + 1;
            float originalX = vertices[index];
            float originalZ = vertices[index + 2];

            // Fórmula matemática para simular olas complejas combinando Senos
            float ola = (float) (Math.sin(originalX * 0.05 + delta * 2.0) * 18.0 + 
                                 Math.sin(originalZ * 0.05 + delta * 1.5) * 14.0);

            actualPoints[yIndex] = vertices[yIndex] + ola;
            index += 3;  
         }
      }
      
      mesh.getPoints().setAll(actualPoints);
   }
}

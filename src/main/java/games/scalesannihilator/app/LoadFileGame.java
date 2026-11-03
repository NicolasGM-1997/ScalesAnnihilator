package games.scalesannihilator.app;

import games.scalesannihilator.character.Statistics;
import games.scalesannihilator.character.Statistics.Pattern;
import games.scalesannihilator.character.Statistics.Size;
import games.scalesannihilator.character.Statistics.Type;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LoadFileGame {
   
   public static ArrayList<Statistics> stats = new ArrayList<>();
   public static ArrayList<Integer> groups = new ArrayList<>(); 
   public static Statistics plyStats = new Statistics();
   private static int idMax = 13;
   private static int idXYZ = 17;
   private static int enemy = 20;
   
   public static void resetStats(){
      stats.clear();
      groups.clear();
      plyStats = new Statistics();
   }
   
   public static void loadStats(String path, int level) throws IOException{
      
      InputStream is = LoadFileGame.class.getResourceAsStream(path);
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      
      String line = "";
      
      while((line = br.readLine()) != null){
                  
         String[] fields = line.split(",");
         
         if(fields[0].equals("XX")) continue;
         if(fields[0].equals("PLY")){
            
            plyStats.setLife(Double.parseDouble(fields[2]));
            plyStats.setStomach(Double.parseDouble(fields[3]));
            plyStats.setEnergy(Double.parseDouble(fields[4]));
            plyStats.setFury(Double.parseDouble(fields[5]));
            plyStats.setAttack(Double.parseDouble(fields[6]));
            plyStats.setMinDamage(Double.parseDouble(fields[7]));
            plyStats.setPercentFury(Double.parseDouble(fields[8]));
            plyStats.setDefense(Double.parseDouble(fields[9]));
            plyStats.setVelSwim(Double.parseDouble(fields[10]));
            plyStats.setVelAtk(Double.parseDouble(fields[11]));
            plyStats.setPrice(Double.parseDouble(fields[12]));
            plyStats.setMaxLife(Double.parseDouble(fields[idMax]));
            plyStats.setMaxStomach(Double.parseDouble(fields[idMax+1]));
            plyStats.setMaxEnergy(Double.parseDouble(fields[idMax+2]));
            plyStats.setMaxFury(Double.parseDouble(fields[idMax+3]));
            
         }
         
         if(fields[0].contains("GR")){
            
            if(fields[0].endsWith(String.valueOf(level))){
               Statistics stat = new Statistics();
               groups.add(Integer.parseInt(fields[1]));
                            
               stat.setLife(Double.parseDouble(fields[2]));
               stat.setStomach(Double.parseDouble(fields[3]));
               stat.setEnergy(Double.parseDouble(fields[4]));
               stat.setFury(Double.parseDouble(fields[5]));
               stat.setAttack(Double.parseDouble(fields[6]));
               stat.setMinDamage(Double.parseDouble(fields[7]));
               stat.setPercentFury(Double.parseDouble(fields[8]));
               stat.setDefense(Double.parseDouble(fields[9]));
               stat.setVelSwim(Double.parseDouble(fields[10]));
               stat.setVelAtk(Double.parseDouble(fields[11]));
               stat.setPrice(Double.parseDouble(fields[12]));
               stat.setMaxLife(Double.parseDouble(fields[idMax]));
               stat.setMaxStomach(Double.parseDouble(fields[idMax+1]));
               stat.setMaxEnergy(Double.parseDouble(fields[idMax+2]));
               stat.setMaxFury(Double.parseDouble(fields[idMax+3]));
               stat.setSpawnX(Double.parseDouble(fields[idXYZ]));
               stat.setSpawnY(Double.parseDouble(fields[idXYZ+1]));
               stat.setSpawnZ(Double.parseDouble(fields[idXYZ+2]));
               stat.setPattern(Pattern.valueOf(fields[enemy]));
               stat.setSize(Size.valueOf(fields[enemy+1]));
               stat.setType(Type.valueOf(fields[enemy+2]));
               
               stats.add(stat);
            }
         }
      }
   }
}
